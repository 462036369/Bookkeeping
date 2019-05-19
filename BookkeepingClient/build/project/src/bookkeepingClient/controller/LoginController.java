package bookkeepingClient.controller;

import java.io.IOException;

import bookkeepingClient.Main;
import bookkeepingClient.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {
	@FXML TextField UserName;
	@FXML PasswordField Password;
	@FXML Label LabelMsg;
	@FXML Button SignIn;
	@FXML Button Register;
	//DropShadow shadow = new DropShadow();
	
	public void onClickSignIn() {
		if(UserName.getText().trim().equals(""))
			LabelMsg.setText("请输入用户名");
		else if(Password.getText().isEmpty()) {
			LabelMsg.setText("请输入密码");
		}else {
			String msg = "Login`" + UserName.getText().trim() + "`" + Password.getText();
			Util.send(msg);
			msg = Util.receive();
			if(msg.equals("ErrorPassword")) {
				LabelMsg.setText("密码错误");
			}else if(msg.equals("NoUser")) {
				LabelMsg.setText("用户名不存在");
			}else {
				LabelMsg.setText("");
				loginSuccess();
			}
		}
	}
	
	private void loginSuccess() {
		String userInfo = Util.receive();
		String allType = Util.receive();
		String logs = Util.receive();
		String acts = Util.receive();
		Util.initUser(allType, userInfo,logs,acts);
		Scene scene;
		try {
			//scene = new Scene(FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/HomeView.fxml")));
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/bookkeepingClient/view/HomeView.fxml"));
            scene = new Scene(loader.load());
            HomeController controller = (HomeController)loader.getController();
            controller.init();
            Main.changeTitle("记账宝");
			Main.changeScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void onClickRegister() throws Exception {
		
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("注册");
		Pane pane = FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/RegisterView.fxml"));
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.showAndWait();
	}
}
