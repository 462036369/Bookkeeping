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

public class BookkeepingExpenditureController implements Initializable{
	private int type = 1;
	private String mark = "";
	private String[] Type = {"","food","traffic","shopping","live","entertainment","medicalCare","education","favor","lend","expenditureOther"};
	@FXML DatePicker Date;
	@FXML Pane Pane;
	@FXML TextArea NowMark;
	@FXML Button ReturnButton;
	@FXML Button InComeButton;
	@FXML TextField Text;
	@FXML Label TypeText;
	@FXML Pane BackLine;
	@FXML Button Food;
	@FXML Button Traffic;
	@FXML Button Shopping;
	@FXML Button Live;
	@FXML Button Entertainment;
	@FXML Button MedicalCare;
	@FXML Button Education;
	@FXML Button Favor;
	@FXML Button Lend;
	@FXML Button Other;
	@FXML Label FoodText;
	@FXML Label TrafficText;
	@FXML Label ShoppingText;
	@FXML Label LiveText;
	@FXML Label EntertainmentText;
	@FXML Label MedicalCareText;
	@FXML Label EducationText;
	@FXML Label FavorText;
	@FXML Label LendText;
	@FXML Label OtherText;
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
	public void keyBoardInput() {
		BookkeepingExpenditureModel.keyBoardInput(Text);
	}
	public void onClickIncome() {
		BookkeepingExpenditureModel.onClickReturnOrIncomeExpenditure(1);
	}
	
	private Button choiceButton(int type) {
		switch(type) {
		case 1:
			return Food;
		case 2:
			return Traffic;
		case 3:
			return Shopping;
		case 4:
			return Live;
		case 5:
			return Entertainment;
		case 6:
			return MedicalCare;
		case 7:
			return Education;
		case 8:
			return Favor;
		case 9:
			return Lend;
		case 10:
			return Other;
			default:
				return null;
		}
	}
	private Label choiceLabel(int type) {
		switch(type) {
		case 1:
			return FoodText;
		case 2:
			return TrafficText;
		case 3:
			return ShoppingText;
		case 4:
			return LiveText;
		case 5:
			return EntertainmentText;
		case 6:
			return MedicalCareText;
		case 7:
			return EducationText;
		case 8:
			return FavorText;
		case 9:
			return LendText;
		case 10:
			return OtherText;
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
		String color = BookkeepingExpenditureModel.getColor(this.type);
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
