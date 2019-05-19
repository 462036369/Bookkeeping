package bookkeepingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import bookkeepingClient.Main;
import bookkeepingClient.model.BookkeepingExpenditureModel;
import bookkeepingClient.model.Log;
import bookkeepingClient.model.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ChartController implements Initializable{
	@FXML PieChart AllChart;
	@FXML Label caption;
	@FXML RadioButton AllButton;
	@FXML RadioButton TypeButton;
	@FXML RadioButton TimeButton;
	private static final HashMap<String,Integer> typeMap = new HashMap<String,Integer>();
	private static final HashMap<String,Integer> timeMap = new HashMap<String,Integer>();
	private ObservableList allList = FXCollections.observableArrayList();
	private ObservableList typeList = FXCollections.observableArrayList();
	private ObservableList timeList = FXCollections.observableArrayList();
	private ToggleGroup group = new ToggleGroup();
	private ObservableList logList = Util.getList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int expenditureNum = 0;
		int inComeNum = 0;
		Iterator it = logList.iterator();
		while(it.hasNext()) {
			Log log = (Log)it.next();
			if(typeMap.get(log.getType()) == null) {
				typeMap.put(log.getType(), 1);
			}else {
				int n = typeMap.get(log.getType()) + 1;
				typeMap.put(log.getType(), n);
			}
			if(log.getExpenditureOrIncome().equals("收入")) {
				inComeNum++;
			}else {
				if(timeMap.get(log.getDate().substring(0, 7)) == null) {
					timeMap.put(log.getDate().substring(0, 7), 1);
				}else {
					int n = timeMap.get(log.getDate().substring(0, 7)) + 1;
					timeMap.put(log.getDate().substring(0, 7), n);
				}
				expenditureNum++;
			}
		}
		Iterator<HashMap.Entry<String, Integer>> iterator = typeMap.entrySet().iterator();
		while(iterator.hasNext()) {
			HashMap.Entry<String, Integer> entry = iterator.next();
			typeList.add(new PieChart.Data(entry.getKey(), entry.getValue()));
		}
		iterator = timeMap.entrySet().iterator();
		while(iterator.hasNext()) {
			HashMap.Entry<String, Integer> entry = iterator.next();
			timeList.add(new PieChart.Data(entry.getKey(), entry.getValue()));
		}
		allList.add(new PieChart.Data("收入", inComeNum));
		allList.add(new PieChart.Data("支出", expenditureNum));
		caption.setTextFill(Color.WHITE);
		caption.setStyle("-fx-font: 18 arial;");
		AllChart.setData(allList);
		for (final PieChart.Data data : AllChart.getData()) {
		    data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
		        new EventHandler<MouseEvent>() {
		            @Override public void handle(MouseEvent e) {
		                caption.setLayoutX(e.getSceneX());
		                caption.setLayoutY(e.getSceneY());
		                caption.setText(String.valueOf(data.getPieValue()) + "%");
		             }
		        });
		}
		AllButton.setToggleGroup(group);
		AllButton.setUserData(0);
		TypeButton.setToggleGroup(group);
		TypeButton.setUserData(1);
		TimeButton.setToggleGroup(group);
		TimeButton.setUserData(2);
		AllButton.setSelected(true);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override			
			public void changed(ObservableValue<? extends Toggle> changed, Toggle oldVal, Toggle newVal){
				int n = (Integer)newVal.getUserData();
				caption.setText("");
				switch(n) {
				case 0:
					AllChart.setData(allList);
					for (final PieChart.Data data : AllChart.getData()) {
					    data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
					        new EventHandler<MouseEvent>() {
					            @Override public void handle(MouseEvent e) {
					                caption.setLayoutX(e.getSceneX());
					                caption.setLayoutY(e.getSceneY());
					                caption.setText(String.valueOf(data.getPieValue()) + "%");
					             }
					        });
					}
					break;
				case 1:
					AllChart.setData(typeList);
					for (final PieChart.Data data : AllChart.getData()) {
					    data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
					        new EventHandler<MouseEvent>() {
					            @Override public void handle(MouseEvent e) {
					                caption.setLayoutX(e.getSceneX());
					                caption.setLayoutY(e.getSceneY());
					                caption.setText(String.valueOf(data.getPieValue()) + "%");
					             }
					        });
					}
					break;
				case 2:
					AllChart.setData(timeList);
					for (final PieChart.Data data : AllChart.getData()) {
					    data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
					        new EventHandler<MouseEvent>() {
					            @Override public void handle(MouseEvent e) {
					                caption.setLayoutX(e.getSceneX());
					                caption.setLayoutY(e.getSceneY());
					                caption.setText(String.valueOf(data.getPieValue()) + "%");
					             }
					        });
					}
					break;
				}
			}		
		});

	}
	public void onClickHome() {
		BookkeepingExpenditureModel.onClickReturnOrIncomeExpenditure(0);
	}
	public void returnExample() {
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
}
