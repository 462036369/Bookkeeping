package bookkeepingClient.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.UnaryOperator;

import bookkeepingClient.Main;
import bookkeepingClient.controller.ExampleController;
import bookkeepingClient.controller.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookkeepingExpenditureModel {
	public static void onClickReturnOrIncomeExpenditure(int choice){
		Scene scene;
		try {
			FXMLLoader loader = new FXMLLoader();
			if(choice == 0)
				loader.setLocation(Main.class.getResource("/bookkeepingClient/view/HomeView.fxml"));
			else if(choice == 1)
				loader.setLocation(Main.class.getResource("/bookkeepingClient/view/BookkeepingIncomeView.fxml"));
			else if(choice == 2)
				loader.setLocation(Main.class.getResource("/bookkeepingClient/view/BookkeepingExpenditureView.fxml"));
            scene = new Scene(loader.load());
            if(choice == 0) {
	            HomeController controller = (HomeController)loader.getController();
	            controller.init();
            }
			Main.changeScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void keyBoardInput(TextField Text) {
		Text.setTextFormatter(new TextFormatter<String>(new UnaryOperator<Change>() {
			@Override
			public Change apply(Change t) {
				String s = t.getText();
				if(Text.getText().isEmpty() && s.equals("0")) {
					return null;
				}else if((s.matches("\\d*") && Text.getText().length() < 10) || s.equals("")) {
					return t;
				}
				return null;
			}
		}));
	}
	public static void changeImageByType(Button button,Label label,String style) {
			button.setStyle(style);
			label.setStyle("-fx-text-fill :#000;");
	}
	public static String getColor(int type) {
		switch(type) {
		case 1:
			return "ce8c23";
		case 2:
			return "88ab83";
		case 3:
			return "9b5d68";
		case 4:
			return "c36d44";
		case 5:
			return "6a80d2";
		case 6:
			return "da7882";
		case 7:
			return "2a7c49";
		case 8:
			return "963e55";
		case 9:
			return "983242";
		case 10:
			return "5c5d5d";
		case 11:
			return "D08135";
		case 12:
			return "DE3437";
		case 13:
			return "C36E23";
		case 14:
			return "77AC64";
		case 15:
			return "7D86D1";
		case 16:
			return "7D9A4D";
		case 17:
			return "9F3544";
		case 18:
			return "B45662";
		case 19:
			return "15CDA5";
		case 20:
			return "8A8A8A";
			default:
				return null;
		}
	}
	public static void onClickNum(ActionEvent event,DatePicker Date,TextField Text,String Type,String mark) {
		Button button = (Button)event.getSource();
		String s = button.getText();
		if(Text.getText().length() >= 10 && !s.equals("←") && !s.equals("重置") && !s.equals("确定")) {
			return;
		}
		if(s.equals("0") && !Text.getText().isEmpty()) {
			Text.setText(Text.getText() + "0");
		}else if(s.equals("重置")) {
			Text.setText("");
		}else if(s.equals("确定")) {
			submit(Date,Text,Type,mark);
		}else if(s.equals("←") && !Text.getText().isEmpty()) {
			String str = Text.getText();
			Text.setText(str.substring(0, str.length() - 1));
		}else if(!s.equals("←") && !s.equals("0")){
			Text.setText(Text.getText() + s);
		}
		Text.requestFocus();
		Text.positionCaret(Text.getText().length());
		
	}
	private static void submit(DatePicker Date,TextField Text,String Type,String mark) {
		String date;
		if(Date.getValue() == null) {
			date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}else {
			date = Date.getValue().toString();
		}
		String text = Text.getText().trim();
		
		if(text.isEmpty()) {
			return;
		}
		Util.addNumToType(Type, Integer.valueOf(text));
		try {
			Util.addLog(Integer.parseInt(text), Type, new SimpleDateFormat("yyyy-MM-dd").parse(date),mark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Text.setText("");
		
	}
	public static void addMarks(TextArea NowMark,String mark,String[] s) {
		NowMark.setTooltip(new Tooltip("当前备注"));
		Stage stage = new Stage();
		stage.setWidth(400);
		stage.setHeight(400);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("添加备注");
		Pane pane = new Pane();
		TextArea text = new TextArea(mark);
		Button button = new Button("确  定");
		text.setPrefWidth(350);
		text.setPrefHeight(300);
		text.setLayoutX(20);
		button.setPrefWidth(100);
		button.setPrefHeight(40);
		button.setLayoutY(310);
		button.setLayoutX(150);
		button.setOnAction(e->{
			s[0] = text.getText().trim();
			NowMark.setText(s[0]);
			stage.close();
		});
		pane.getChildren().addAll(text,button);
		stage.setScene(new Scene(pane));
		stage.showAndWait();
	}
}
