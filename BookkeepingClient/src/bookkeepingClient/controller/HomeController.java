package bookkeepingClient.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

import org.apache.poi.ss.usermodel.Table;

import bookkeepingClient.Main;
import bookkeepingClient.model.Activity;
import bookkeepingClient.model.Logs;
import bookkeepingClient.model.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {
	@FXML Pane Pane;
	@FXML Button MainButton;
	@FXML Label Budget;
	@FXML Label Report;
	@FXML Label BudgetMsg;
	public void init() {
		int budget = Util.getBudget();
		int[] dateBudget = Logs.getDateBudget();
		int sum = 0;
		for(int i = 1;i < dateBudget.length;i++) {
			sum += dateBudget[i];
		}
		if(sum < budget / 2) {
			BudgetMsg.setText("�����¾�֧������Ԥ��һ�룬���Էſ��ֽ�");
			BudgetMsg.setTextFill(Paint.valueOf("#000"));
		}else if(sum < budget * 3 / 4) {
			BudgetMsg.setText("�����¾�֧���ȽϽӽ�Ԥ�㣬�����ʵ�����֧��");
			BudgetMsg.setTextFill(Paint.valueOf("#000"));
		}else if(sum <= budget) {
			BudgetMsg.setText("�����¾�֧����Ҫ�ﵽԤ�㣬���Ծ����ܼ���֧��");
			BudgetMsg.setTextFill(Paint.valueOf("#f40"));
		}else {
			BudgetMsg.setText("�����¾�֧������Ԥ�㣬���Կ��ǳ���");
			BudgetMsg.setTextFill(Paint.valueOf("#f40"));
		}
		Budget.setText("��ǰ������Ԥ�㣺" + budget);
		ArrayList<Activity> list = Util.getActivities().getList();
		int num = 0;
		for(int i = 0;i < list.size() && num < 4;i++) {
			Activity act = list.get(i);
			if((LocalDate.parse(act.getDate()).isAfter(LocalDate.now()) || LocalDate.parse(act.getDate()).isEqual(LocalDate.now())
					) && LocalDate.parse(act.getDate()).isBefore(LocalDate.now().plusDays(3))) {
				Label label = new Label();
				label.setPrefSize(300, 25);
				label.setMaxSize(300, 25);
				label.setAlignment(Pos.CENTER);
				label.setTextFill(Paint.valueOf("#f90"));
				label.setText(act.getDate() + "  " + act.getActivity());
				label.setFont(Font.font("Timer New Roman",FontWeight.NORMAL, FontPosture.ITALIC, 17));
				label.setTooltip(new Tooltip(act.getDate() + "  " + act.getActivity()));
				label.setLayoutX(50);
				label.setLayoutY(380 + num * 30);
				Pane.getChildren().add(label);
				num++;
			}
		}
	}
	public void onClickMainButton() {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/bookkeepingClient/view/BookkeepingExpenditureView.fxml")));
			Main.changeScene(scene);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void onClickReport() {
		Scene scene;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/bookkeepingClient/view/ExampleView.fxml"));
        try {
			scene = new Scene(loader.load());
			Main.changeScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void changeBudget() {
		Stage stage = new Stage();
		stage.setWidth(120);
		stage.setHeight(120);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		TextField textField = new TextField();
		textField.setLayoutX(15);
		textField.setLayoutY(10);
		textField.setPrefWidth(100);
		textField.requestFocus();
		textField.setTextFormatter(new TextFormatter<String>(new UnaryOperator<Change>() {
			@Override
			public Change apply(Change t) {
				String s = t.getText();
				if((s.matches("\\d*") && textField.getText().length() < 8) || s.equals("")) {
					return t;
				}
				return null;
			}
		}));
		Button button = new Button("ȷ���޸�");
		button.setLayoutX(35);
		button.setLayoutY(50);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!textField.getText().trim().isEmpty()) {
					Budget.setText("��ǰ������Ԥ�㣺" + textField.getText());
					Util.setBudget(Integer.parseInt(textField.getText()));
					stage.close();
				}
			};
		});
		Pane pane = new Pane();
		pane.getChildren().addAll(textField,button);
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.showAndWait();
	}
	public void addActivity() {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(370);
		stage.setHeight(170);
		stage.setTitle("��ӻ��Ϣ/����¼");
		Pane pane = new Pane();
		TextArea text = new TextArea();
		Label textLabel = new Label("��ӻ��Ϣ/����¼");
		textLabel.setLayoutX(5);
		textLabel.setLayoutY(5);
		textLabel.setPrefWidth(150);
		textLabel.setAlignment(Pos.CENTER);
		text.setPrefSize(150, 100);
		text.setMaxSize(150, 100);
		text.setLayoutX(5);
		text.setLayoutY(25);
		DatePicker date = new DatePicker();
		Label dateLabel = new Label("ѡ������");
		dateLabel.setLayoutX(155);
		dateLabel.setLayoutY(5);
		dateLabel.setPrefWidth(120);
		dateLabel.setAlignment(Pos.CENTER);
		date.setLayoutX(160);
		date.setLayoutY(65);
		date.setPrefWidth(120);
		date.setValue(LocalDate.now());
		Button button = new Button("ȷ��");
		button.setLayoutX(290);
		button.setLayoutY(35);
		button.setPrefWidth(50);
		button.setPrefHeight(80);
		button.setOnAction(e -> {
			if(text.getText().trim().isEmpty()) {
				text.setText("��");
			}
			Util.getActivities().add(text.getText().trim(), date.getValue().toString());
			stage.close();
			init();
		});
		date.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,LocalDate newValue) {
				if(newValue.isBefore(LocalDate.now())) {
					date.setValue(LocalDate.now());
					Stage stage = new Stage();
					stage.setTitle("��ʾ");
					Pane pane = new Pane();
					Label label = new Label("�����/����¼������ѡ�����֮ǰ��ʱ��");
					label.setPrefSize(280, 50);
					label.setAlignment(Pos.CENTER);
					pane.getChildren().add(label);
					stage.setScene(new Scene(pane));
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();
				}
			}
		});
		date.setEditable(false);
		pane.getChildren().addAll(text,textLabel,date,dateLabel,button);
		stage.setScene(new Scene(pane));
		stage.showAndWait();

	}
}

