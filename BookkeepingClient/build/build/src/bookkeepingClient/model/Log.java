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
			put("education", "教育");
			put("living", "生活费");  
			put("entertainment", "娱乐");  
			put("bonus", "奖金");  
			put("investment", "投资收益");  
			put("food", "餐饮");  
			put("inComeOther", "其他收入");  
			put("wages", "工资");  
			put("redPacket", "收红包");  
			put("medicalCare", "医疗");  
			put("transfer", "转账");  
			put("expenditureOther", "其他支出");  
			put("favor", "人情");  
			put("borrow", "借入款");  
			put("reimbursement", "报销");  
			put("job", "兼职");  
			put("lend", "借出款");  
			put("live", "居住");  
			put("traffic", "交通");  
			put("shopping", "购物");
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
			this.mark.set("无");
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