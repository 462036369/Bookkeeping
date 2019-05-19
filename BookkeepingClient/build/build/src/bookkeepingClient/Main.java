package bookkeepingClient;

import java.io.IOException;

import bookkeepingClient.model.Client;
import bookkeepingClient.model.Util;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	private static Client client = Client.getInstance();
	private static Stage stage;
	private static Scene[] scene;
	public static Client getClient() {
		return client;
	}
	private void sendUser() {
		Util.send(Integer.valueOf(Util.getBudget()).toString());//·¢ËÍÔ¤Ëã
		Util.send(Util.getUserType().toString());
		Util.send(Util.getLogs().toString());
		Util.send(Util.getActivities().toString());
	}
	private void initStage(Stage stage) {
		stage.setOnCloseRequest(e->{
			Util.send("quit");
			if(stage.getTitle().equals("¼ÇÕË±¦")) {
				sendUser();
			}
			Util.send("NoLogin");
			client.close();
		});
		stage.setHeight(600);
		stage.setWidth(400);
		stage.setResizable(false);
		scene = new Scene[5];
		try {
			scene[0] = new Scene(FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/LoginView.fxml")));
			scene[1] = new Scene(FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/HomeView.fxml")));
			scene[2] = new Scene(FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/BookkeepingExpenditureView.fxml")));
			scene[3] = new Scene(FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/BookkeepingIncomeView.fxml")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stage.setTitle("¼ÇÕË±¦-µÇÂ½");
		stage.setScene(scene[0]);
		stage.show();
	}
	public static void changeTitle(String s) {
		stage.setTitle(s);
	}
	public static void changeScene(Scene scene) {
		stage.setScene(scene);
		stage.show();
		stage.getScene().setCursor(Cursor.DISAPPEAR);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		initStage(stage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
