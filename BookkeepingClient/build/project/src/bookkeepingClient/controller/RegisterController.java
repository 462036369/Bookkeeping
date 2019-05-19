package bookkeepingClient.controller;

import bookkeepingClient.model.Util;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterController {
	@FXML PasswordField Password;
	@FXML PasswordField RePassword;
	@FXML TextField Email;
	@FXML TextField Phone;
	@FXML TextField UserName;
	@FXML Label LabelMsg;
	@FXML Pane Pane;
	public void informationVerification() {
		if(Email.getText().trim().isEmpty() || UserName.getText().trim().isEmpty() || Phone.getText().trim().isEmpty() || Password.getText().isEmpty()) {
			LabelMsg.setText("请填写全部信息");
		}else if(UserName.getText().trim().length() < 5 || UserName.getText().trim().length() > 20){
			LabelMsg.setText("用户名长度不得短于5位不得长于20位");
		}else if(Password.getText().trim().length() < 6 || Password.getText().trim().length() > 16){
			LabelMsg.setText("密码长度不得短于6位不得长于16位");
		}else if(!Password.getText().equals(RePassword.getText())) {
			LabelMsg.setText("两次密码不一致");
		}else if(!Email.getText().trim().isEmpty() && !Email.getText().trim().matches("[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}")){
			LabelMsg.setText("请输入正确的邮箱");
		}else if(!Phone.getText().trim().isEmpty() && !Phone.getText().trim().matches("1[3|7|4|5|8][0-9]\\d{4,8}")) {
			LabelMsg.setText("请输入正确的手机号");
		}else if(!Util.addUser(UserName.getText().trim(), Password.getText(), Email.getText().trim(), Phone.getText().trim())) {
			LabelMsg.setText("用户名已存在");
		}else {
			LabelMsg.setText("");
			Stage stage = (Stage)Pane.getScene().getWindow();
			stage.close();
		}
	} 
}
