package bookkeepingClient.controller;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import bookkeepingClient.model.Log;
import bookkeepingClient.model.Logs;
import bookkeepingClient.model.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class UpdateRowController{
	@FXML TextField MoneyText;
	@FXML ChoiceBox Type;
	@FXML Label TypeText;
	@FXML RadioButton Income;
	@FXML RadioButton Expenditure;
	@FXML DatePicker Date;
	@FXML TextArea Mark;
	@FXML Button Sure;
	@FXML Label MsgLabel;
	private final ObservableList<String> ExpenditureList = FXCollections.observableArrayList("����","��ͨ","����","��ס","����","ҽ��",
			"����","����","�����","����֧��");
	private final ObservableList<String> IncomeList = FXCollections.observableArrayList("����","�պ��","�����","����","����","��ְ",
			"�����","Ͷ������","ת��","��������");
	private final ToggleGroup group = new ToggleGroup();
	@SuppressWarnings("unchecked")
	public void init(TableCell<Log,String> cell,ObservableList nowList) {
		Log log = (Log)nowList.get(cell.getIndex());
		MoneyText.setText(log.getMoney());
		Expenditure.setToggleGroup(this.group);
		Expenditure.setUserData(0);
		Income.setToggleGroup(this.group);
		Income.setUserData(1);
		TypeText.setText(log.getType());
		if(log.getExpenditureOrIncome().equals("֧��")) {
			Expenditure.setSelected(true);
			Type.setItems(ExpenditureList);
			for(int i = 0;i < 10;i++) {
				if(ExpenditureList.get(i).equals(TypeText.getText())) {
					Type.getSelectionModel().select(i);
					break;
				}
			}
		}else {
			Income.setSelected(true);
			Type.setItems(IncomeList);
			for(int i = 0;i < 10;i++) {
				if(IncomeList.get(i).equals(TypeText.getText())) {
					Type.getSelectionModel().select(i);
					break;
				}
			}
		}
		
		Type.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if(newValue.toString().equals("-1"))
					return;
				TypeText.setText((String)Type.getItems().get(Integer.valueOf(newValue.toString())));
				
			}
		});

		Date.setValue(LocalDate.parse(log.getDate()));
		Date.setEditable(false);
		Mark.setText(log.getMark());
		MoneyText.setTextFormatter(new TextFormatter<String>(new UnaryOperator<Change>() {
			@Override
			public Change apply(Change t) {
				String s = t.getText();
				if((s.matches("\\d*") && MoneyText.getText().length() < 10) || s.equals("")) {
					return t;
				}
				return null;
			}
		}));
		group.selectedToggleProperty().addListener ((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
		    if (group.getSelectedToggle() != null) {
		    	int flag = Integer.valueOf(group.getSelectedToggle().getUserData().toString());
		    	if(flag == 0) {
		    		Type.setItems(ExpenditureList);
		    		Type.getSelectionModel().select(0);
		    		TypeText.setText(ExpenditureList.get(0));
		    	}else {
		    		Type.setItems(IncomeList);
		    		Type.getSelectionModel().select(0);
		    		TypeText.setText(IncomeList.get(0));
		    	}
		    }
		});
		Sure.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(MoneyText.getText().trim().isEmpty()) {
					MsgLabel.setText("��������");
				}else {
					int[] dateBudget = Logs.getDateBudget();
					if(LocalDate.parse(log.getDate()).getYear() == LocalDate.now().getYear() && LocalDate.parse(log.getDate()).getMonth() == LocalDate.now().getMonth()) {
						if(log.getExpenditureOrIncome().equals("֧��")) {
							dateBudget[LocalDate.parse(log.getDate()).getDayOfMonth()] -= Integer.parseInt(log.getMoney());
						}else {
							dateBudget[LocalDate.parse(log.getDate()).getDayOfMonth()] += Integer.parseInt(log.getMoney());
						}
					}
					MsgLabel.setText("");
					log.setType(TypeText.getText().trim());
					log.setMoney(MoneyText.getText().trim());
					int flag = Integer.valueOf(group.getSelectedToggle().getUserData().toString());
					if(flag == 0)
						log.setExpenditureOrIncome("֧��");
					else
						log.setExpenditureOrIncome("����");
					log.setDate(Date.getValue().toString());
					if(Mark.getText().trim().isEmpty()) {
						log.setMark("��");
					}else {
						log.setMark(Mark.getText().trim());
					}
					if(LocalDate.parse(log.getDate()).getYear() == LocalDate.now().getYear() && LocalDate.parse(log.getDate()).getMonth() == LocalDate.now().getMonth()) {
						if(log.getExpenditureOrIncome().equals("֧��")) {
							dateBudget[LocalDate.parse(log.getDate()).getDayOfMonth()] += Integer.parseInt(log.getMoney());
						}else {
							dateBudget[LocalDate.parse(log.getDate()).getDayOfMonth()] -= Integer.parseInt(log.getMoney());
						}
					}
				}
				
				Button button = (Button)event.getSource();
				Stage stage = (Stage)button.getScene().getWindow();
				stage.close();
			}
		});
	}
}
