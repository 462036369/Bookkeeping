package bookkeepingClient.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import bookkeepingClient.model.BookkeepingExpenditureModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class BookkeepingIncomeController implements Initializable{
	@FXML Pane Pane;
	@FXML Button ReturnButton;
	@FXML Button ExpenditureButton;
	private int type = 1;
	private String mark = "";
	private String[] Type = {"","wages","redPacket","living","bonus","reimbursement","job","borrow","investment","transfer","inComeOther"};
	@FXML DatePicker Date;
	@FXML TextArea NowMark;
	@FXML Button InComeButton;
	@FXML TextField Text;
	@FXML Label TypeText;
	@FXML Pane BackLine;
	@FXML Button Wages;
	@FXML Button RedPacket;
	@FXML Button Living;
	@FXML Button Bonus;
	@FXML Button Reimbursement;
	@FXML Button Job;
	@FXML Button Borrow;
	@FXML Button Investment;
	@FXML Button Transfer;
	@FXML Button InComeOther;
	@FXML Label WagesText;
	@FXML Label RedPacketText;
	@FXML Label LivingText;
	@FXML Label BonusText;
	@FXML Label ReimbursementText;
	@FXML Label JobText;
	@FXML Label BorrowText;
	@FXML Label InvestmentText;
	@FXML Label TransferText;
	@FXML Label InComeOtherText;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Date.setEditable(false);
		Date.setValue(LocalDate.now());
		Text.requestFocus();
		Text.positionCaret(0);
		NowMark.setEditable(false);
	}
	public void onClickReturn(){
		BookkeepingExpenditureModel.onClickReturnOrIncomeExpenditure(0);
	}
	public void onClickExpenditure() {
		BookkeepingExpenditureModel.onClickReturnOrIncomeExpenditure(2);
	}
	public void keyBoardInput() {
		BookkeepingExpenditureModel.keyBoardInput(Text);
	}
	private Label choiceLabel(int type) {
		switch(type) {
		case 1:
			return WagesText;
		case 2:
			return RedPacketText;
		case 3:
			return LivingText;
		case 4:
			return BonusText;
		case 5:
			return ReimbursementText;
		case 6:
			return JobText;
		case 7:
			return BorrowText;
		case 8:
			return InvestmentText;
		case 9:
			return TransferText;
		case 10:
			return InComeOtherText;
			default:
				return null;
		}
	}
	private Button choiceButton(int type) {
		switch(type) {
		case 1:
			return Wages;
		case 2:
			return RedPacket;
		case 3:
			return Living;
		case 4:
			return Bonus;
		case 5:
			return Reimbursement;
		case 6:
			return Job;
		case 7:
			return Borrow;
		case 8:
			return Investment;
		case 9:
			return Transfer;
		case 10:
			return InComeOther;
			default:
				return null;
		}
	}
	public void onClickType(ActionEvent event) {
		Label label = choiceLabel(this.type);
		Button button = (Button)event.getSource();
		String style = "-fx-background-image :url('/bookkeepingClient/view/CSS/Image/" + String.valueOf(Character.toUpperCase(Type[this.type].charAt(0))) + Type[this.type].substring(1) + ".png');" + "-fx-background-color :#fff;";
		BookkeepingExpenditureModel.changeImageByType(choiceButton(this.type),label,style);
		this.type = Integer.parseInt(button.getText());
		label = choiceLabel(this.type);
		style = "-fx-background-image :url('/bookkeepingClient/view/CSS/Image/" + String.valueOf(Character.toUpperCase(Type[this.type].charAt(0))) + Type[this.type].substring(1) + "Now.png');" + "-fx-background-color :#fff;";
		button.setStyle(style);
		String color = BookkeepingExpenditureModel.getColor(this.type + 10);
		Text.setStyle("-fx-background-color :#" + color + ";");
		label.setStyle("-fx-text-fill :#" + color + ";");
		BackLine.setStyle("-fx-background-color :#" + color +";");
		TypeText.setText(label.getText().replaceAll(" ",""));
		Text.requestFocus();
		Text.positionCaret(Text.getText().length());
	}
	public void onClickNum(ActionEvent event) {
		BookkeepingExpenditureModel.onClickNum(event, Date, Text, Type[this.type], this.mark);
	}
	public void addMarks() {
		String[] s = new String[1];
		s[0] = this.mark;
		BookkeepingExpenditureModel.addMarks(NowMark, this.mark, s);
		this.mark = s[0];
	}
	public void changeNowMark(KeyEvent event) {
		event.consume();
	}
}
