package bookkeepingClient.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;

public class Log{
	public SimpleStringProperty money = new SimpleStringProperty();
	public SimpleStringProperty type = new SimpleStringProperty();
	public SimpleStringProperty expenditureOrIncome = new SimpleStringProperty();
	public SimpleStringProperty date = new SimpleStringProperty();
	public SimpleStringProperty mark = new SimpleStringProperty();
	private static final HashMap<String,String> typeMap = new HashMap<String,String>(){
		{
			put("education", "����");
			put("living", "�����");  
			put("entertainment", "����");  
			put("bonus", "����");  
			put("investment", "Ͷ������");  
			put("food", "����");  
			put("inComeOther", "��������");  
			put("wages", "����");  
			put("redPacket", "�պ��");  
			put("medicalCare", "ҽ��");  
			put("transfer", "ת��");  
			put("expenditureOther", "����֧��");  
			put("favor", "����");  
			put("borrow", "�����");  
			put("reimbursement", "����");  
			put("job", "��ְ");  
			put("lend", "�����");  
			put("live", "��ס");  
			put("traffic", "��ͨ");  
			put("shopping", "����");
		}
	};

	public Log(String money, String type, String expenditureOrIncome, Date date,String mark) {
		this.money.set(money);
		char c = type.charAt(0);
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			this.type.set(typeMap.get(type));
		else
			this.type.set(type);
		this.expenditureOrIncome.set(expenditureOrIncome);
		this.date.set(new SimpleDateFormat("yyyy-MM-dd").format(date));
		if(mark.equals(""))
			this.mark.set("��");
		else
			this.mark.set(mark);
	}
	public static HashMap<String,String> getMap(){
		return typeMap;
	}
	public String getMoney() {
		return money.get();
	}
	public void setMoney(String money) {
		this.money.set(money);
	}
	public String getType() {
		return type.get();
	}
	public void setType(String type) {
		char c = type.charAt(0);
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			this.type.set(typeMap.get(type));
		else
			this.type.set(type);
	}
	public String getExpenditureOrIncome() {
		return expenditureOrIncome.get();
	}
	public void setExpenditureOrIncome(String expenditureOrIncome) {
		this.expenditureOrIncome.set(expenditureOrIncome);
	}
	public String getDate() {
		return date.get();
	}
	public void setDate(String date) {
		this.date.set(date);
	}
	public String getMark() {
		return mark.get();
	}
	public void setMark(String mark) {
		this.mark.set(mark);
	}
	@Override
	public String toString() {
		return this.money.get() + "`" + this.type.get() + "`" + this.expenditureOrIncome.get() + "`" + this.date.get() + "`" + this.mark.get();
	}
}