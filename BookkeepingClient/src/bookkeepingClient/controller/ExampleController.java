package bookkeepingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import bookkeepingClient.Main;
import bookkeepingClient.model.Log;
import bookkeepingClient.model.Logs;
import bookkeepingClient.model.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
class XCell extends TableCell<String, String> {
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(item);
        this.setTooltip(
           (empty || item==null) ? null : new Tooltip(item));
    }
}
public class ExampleController implements Initializable{
	@FXML TableView Table;
	@FXML TableColumn MoneyCol;
	@FXML TableColumn TypeCol;
	@FXML TableColumn ExpenditureOrIncomeCol;
	@FXML TableColumn DateCol;
	@FXML TableColumn MarkCol;
	@FXML TableColumn OperationCol;
	@FXML DatePicker ChoiceDate;
	@FXML Label ChoiceDateText;
	private ObservableList nowList;
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nowList = Util.getList();

		MoneyCol.setCellValueFactory(new PropertyValueFactory<>("money"));
		TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		ExpenditureOrIncomeCol.setCellValueFactory(new PropertyValueFactory<>("expenditureOrIncome"));
		DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		MarkCol.setCellValueFactory(new PropertyValueFactory<>("mark"));
		MoneyCol.impl_setReorderable(false);
		TypeCol.impl_setReorderable(false);
		ExpenditureOrIncomeCol.impl_setReorderable(false);
		DateCol.impl_setReorderable(false);
		MarkCol.impl_setReorderable(false);
		OperationCol.impl_setReorderable(false);
		OperationCol.setCellFactory((col) -> {
            TableCell<Log, String> cell = new TableCell<Log, String>() {
            	@Override
				public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Button delBtn = new Button("删除");
                        Button udtBtn = new Button("修改");
                        VBox box = new VBox();
                        box.getChildren().addAll(delBtn,udtBtn);
                        this.setGraphic(box);
                        delBtn.setOnMouseClicked(e -> {
                        	Log deleteLog = (Log)nowList.get(this.getIndex());
                        	if(LocalDate.parse(deleteLog.getDate()).getYear() == LocalDate.now().getYear() && LocalDate.parse(deleteLog.getDate()).getMonth() == LocalDate.now().getMonth()) {
                        		int[] dateBudget = Logs.getDateBudget();
                        		if(deleteLog.getExpenditureOrIncome().equals("支出")) {
                        			dateBudget[LocalDate.parse(deleteLog.getDate()).getDayOfMonth()] -= Integer.parseInt(deleteLog.getMoney());
                        		}else {
                        			dateBudget[LocalDate.parse(deleteLog.getDate()).getDayOfMonth()] += Integer.parseInt(deleteLog.getMoney());
                        		}
                        	}
               
                        	nowList.remove(deleteLog);
                        	Util.getList().remove(deleteLog);
                        });
                        udtBtn.setOnMouseClicked(e -> {
                        	updateRow(this);
                        });
                    }
                }	
            };
            return cell;
        });
		MarkCol.setCellFactory(new Callback<TableColumn<String,String>, TableCell<String,String>>() {
            @Override
            public TableCell<String, String> call(TableColumn<String, String> param) {
                return new XCell();
            }
        });
		MoneyCol.setCellFactory(new Callback<TableColumn<String,String>, TableCell<String,String>>() {
            @Override
            public TableCell<String, String> call(TableColumn<String, String> param) {
                return new XCell();
            }
        });
		ChoiceDate.setEditable(false);
		ChoiceDate.setValue(LocalDate.now());
		ChoiceDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,LocalDate newValue) {
				if(newValue.isBefore(oldValue)) {
					ChoiceDateText.setText(newValue.toString() + "~" + oldValue.toString());
					nowList = Util.getList(newValue,oldValue);
					Table.setItems(nowList);
				}else {
					ChoiceDateText.setText(oldValue.toString() + "~" + newValue.toString());
					nowList = Util.getList(oldValue,newValue);
					Table.setItems(nowList);
				}
			}
		});

		Table.setItems(Util.getList());
	}
	private void updateRow(TableCell<Log,String> cell) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		Scene scene;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/bookkeepingClient/view/UpdateRowView.fxml"));
            scene = new Scene(loader.load());
            UpdateRowController controller = (UpdateRowController)loader.getController();
            controller.init(cell,nowList);
			stage.setScene(scene);
			stage.setTitle("修改记录");
			stage.showAndWait();
			Table.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	public void onClickHome() {
		Scene scene;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/bookkeepingClient/view/HomeView.fxml"));
            scene = new Scene(loader.load());
            HomeController controller = (HomeController)loader.getController();
            controller.init();
			Main.changeScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void onClickChart() {
		Scene scene;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/bookkeepingClient/view/ChartView.fxml"));
            scene = new Scene(loader.load());
			Main.changeScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void onClickFile() {
		Scene scene;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/bookkeepingClient/view/FileView.fxml"));
            scene = new Scene(loader.load());
			Main.changeScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
