package bookkeepingClient.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Logs {
	private ObservableList logList;
	private static Logs instance = new Logs();
	private User user = User.getInstance();
	private static int[] dateBudget = new int[LocalDate.now().lengthOfMonth() + 1];
	public static Logs getInstance() {
		return instance;
	}
	private Logs() {
		this.logList = FXCollections.observableArrayList();
	}
	public static int[] getDateBudget() {
		return dateBudget;
	}
	public ObservableList getLogList() {
		return this.logList;
	}
	public void initList(String respond) {
		if(respond.equals("No record")) {
			return;
		}
		String[] logs = respond.split("`");
		String[] Type = {"餐饮","交通","购物","居住","娱乐","医疗","教育","人情","借出款","其他支出"};
		for(String ele:logs) {
			String[] log = ele.split("&");
			String expenditureOrIncome = "收入";
			for(String t:Type) {
				if(log[1].equals(t)) {
					expenditureOrIncome = "支出";
				}
			}
			if(LocalDate.parse(log[2]).getYear() == LocalDate.now().getYear() && LocalDate.parse(log[2]).getMonth() == LocalDate.now().getMonth()){
				if(expenditureOrIncome.equals("支出"))
					this.dateBudget[LocalDate.parse(log[2]).getDayOfMonth()] += Integer.parseInt(log[0]);
				else
					this.dateBudget[LocalDate.parse(log[2]).getDayOfMonth()] -= Integer.parseInt(log[0]);
			}
			try {
				this.logList.add(new Log(log[0],log[1],expenditureOrIncome,new SimpleDateFormat("yyyy-MM-dd").parse(log[2]),log[3]));
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void addLog(int money,String type,Date date,String mark) {
		type = Log.getMap().get(type);
		String[] Type = {"餐饮","交通","购物","居住","娱乐","医疗","教育","人情","借出款","其他支出"};	
		String expenditureOrIncome = "收入";
		for(String t:Type) {
			if(type.equals(t)) {
				expenditureOrIncome = "支出";
			}
		}
		String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		boolean flag = false;
		if(LocalDate.parse(formatDate).getYear() == LocalDate.now().getYear() && LocalDate.parse(formatDate).getMonth() == LocalDate.now().getMonth()){
			if(expenditureOrIncome.equals("支出")) {
				flag = true;
				this.dateBudget[LocalDate.parse(formatDate).getDayOfMonth()] += money;
			}
			else {
				this.dateBudget[LocalDate.parse(formatDate).getDayOfMonth()] -= money;
			}
		}
		if(flag) {
			StringBuilder s = new StringBuilder("您");
			for(int i = 1;i <= LocalDate.now().lengthOfMonth();i++) {

				if(this.dateBudget[i] > user.getBudgetDate()) {
					if(s.indexOf(LocalDate.now().toString().substring(0, 8) + String.format("%02d", i)) < 0) {
						s.append(formatDate.substring(0, 8) + String.format("%02d", i) + " ");
					}
				}
			}
			if(s.length() > 3) {
				s.append("超出预计每日预算，请注意开支");
				Stage stage = new Stage();
				stage.setWidth(400);
				stage.setHeight(300);
				stage.initModality(Modality.APPLICATION_MODAL);
				Pane pane = new Pane();
				pane.setMaxHeight(300);
				pane.setMaxWidth(400);
				pane.setPrefHeight(300);
				pane.setPrefWidth(400);
				Button button = new Button("知道了");
				Label label = new Label(s.toString());
				label.setPrefHeight(250);
				label.setPrefWidth(380);
				label.setMaxHeight(250);
				label.setMaxWidth(380);
				label.setLayoutX(0);
				label.setLayoutY(0);
				label.setWrapText(true);
				button.setLayoutX(180);
				button.setLayoutY(230);
				button.setOnAction(e -> {
					stage.close();
				});
				pane.getChildren().addAll(label,button);
				stage.setScene(new Scene(pane));
				stage.showAndWait();
			}
		}
		this.logList.add(new Log(Integer.toString(money),type,expenditureOrIncome,date,mark));
	}
	@Override
	public String toString() {
		if(this.logList.size() == 0) {
			return "No record";
		}
		StringBuilder res = new StringBuilder();
		Iterator it = this.logList.iterator();
		while(it.hasNext()) {
			Log ele = (Log)it.next();
			res.append(ele.getMoney() + "&");
			res.append(ele.getType() + "&");
			res.append(ele.getDate() + "&");
			res.append(ele.getMark() + "`");
		}
		return res.toString();
	}
}
