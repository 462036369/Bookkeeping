package bookkeepingClient.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import bookkeepingClient.Main;
import bookkeepingClient.model.Log;
import bookkeepingClient.model.Util;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FileController {
	@FXML Label OutText;
	@FXML Label Msg;
	@FXML Label InText;
	public void onClickOut() {
		DirectoryChooser directoryChooser=new DirectoryChooser();
		File file = directoryChooser.showDialog(new Stage());
		if(file == null) {
			return;
		}
		OutText.setText("导出位置：" + file.getPath());
	}
	public void onClickIn() {
		FileChooser fileChooser = new FileChooser();
		Stage selectFile = new Stage();
		selectFile.initModality(Modality.APPLICATION_MODAL);
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
        File file = fileChooser.showOpenDialog(selectFile);
        if(file == null) {
        	return;
        }
        InText.setText("导入位置：" + file.getPath());
	}
	@SuppressWarnings({ "unused", "rawtypes" })
	public void onClickOutFile() {
		if(OutText.getText().length() == 5) {
			Msg.setText("导出路径错误");
			return;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCellStyle style = wb.createCellStyle();
		HSSFSheet sheet = wb.createSheet("消费记录");
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 5));
		HSSFRow row1 = sheet.createRow((int)0);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellValue("消 费 记 录");
		HSSFFont font = wb.createFont();
		font.setFontName("仿宋_GB2312");
		font.setFontHeightInPoints((short) 16);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFont(font);
		cell1.setCellStyle(style1);
		sheet.setColumnWidth((short) 0, (short)6000);
		sheet.setColumnWidth((short) 1, (short)6000);
		sheet.setColumnWidth((short) 2, (short)6000);
		sheet.setColumnWidth((short) 3, (short)6000);
		sheet.setColumnWidth((short) 4, (short)6000);
		HSSFRow row = sheet.createRow((int)3);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("消费时间");
		cell = row.createCell(1);
		cell.setCellValue("具体用处");
		cell = row.createCell(2);
		cell.setCellValue("类型");
		cell = row.createCell(3);
		cell.setCellValue("时间");
		cell = row.createCell(4);
		cell.setCellValue("备注");
		Iterator it = Util.getList().iterator();
		int rownum = 5;
		while(it.hasNext()) {
			Log log = (Log)it.next();
			HSSFRow row2 = sheet.createRow((int)rownum);
			HSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(log.getMoney());
			cell2 = row2.createCell(1);
			cell2.setCellValue(log.getType());
			cell2 = row2.createCell(2);
			cell2.setCellValue(log.getExpenditureOrIncome());
			cell2 = row2.createCell(3);
			cell2.setCellValue(log.getDate());
			cell2 = row2.createCell(4);
			cell2.setCellValue(log.getMark());
			cell2 = row2.createCell(5);
			rownum++;
		}
		FileOutputStream fileOut;
		try
		{
		    File pathReport=new File(OutText.getText().substring(5));
		    if(!pathReport.exists()) {
		        pathReport.mkdir();
		    }
		    File fileReport=new File(OutText.getText().substring(5) + "\\消费记录.xls");
		    if(!fileReport.exists()) {
		        fileReport.createNewFile();
		    }
		    fileOut = new FileOutputStream(fileReport);
		    Msg.setText("导出成功");
		    wb.write(fileOut);
		    wb.close();
		    fileOut.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "rawtypes", "resource", "unchecked" })
	public void onClickInFile() {
		if(InText.getText().length() == 5) {
			Msg.setText("导入路径错误");
			return;
		}
		try {
        	ObservableList list = Util.getList();
        	list.clear();
        	FileInputStream fis = new FileInputStream(InText.getText().substring(5));
			Workbook wb = new HSSFWorkbook(fis);
			fis.close();
			Sheet sheet = wb.getSheetAt(0);
			for(Row r:sheet) {
				if(r.getRowNum() < 5) {
					continue;
				}
				r.getCell(0).setCellType(CellType.STRING);
				r.getCell(1).setCellType(CellType.STRING);
				r.getCell(2).setCellType(CellType.STRING);
				r.getCell(3).setCellType(CellType.STRING);
				r.getCell(4).setCellType(CellType.STRING);
				Log log = new Log(r.getCell(0).getStringCellValue(),r.getCell(1).getStringCellValue(),r.getCell(2).getStringCellValue(),
						new SimpleDateFormat("yyyy-MM-dd").parse(r.getCell(3).getStringCellValue()),r.getCell(4).getStringCellValue());
				list.add(log);
			}
			Msg.setText("导入成功，信息已更新");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void returnToExample() {
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


	

