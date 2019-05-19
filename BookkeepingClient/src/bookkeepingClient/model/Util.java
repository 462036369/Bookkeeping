package bookkeepingClient.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;

import bookkeepingClient.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Util {
	private static User user;
	private static UserType userType;
	private static Logs logs;
	private static Activities acts;
	private static final Client client = Main.getClient();
	public static void initUser(String s,String userInfo,String logsrespond,String actsrespond) {
		user = User.getInstance();
		user.initInfo(userInfo);
		userType = UserType.getInstance();
		userType.initMap(s);
		logs = Logs.getInstance();
		logs.initList(logsrespond);
		acts = Activities.getInstance();
		acts.init(actsrespond);
	}
	public static boolean addUser(String userName,String password,String email,String phone) {
		//÷Ï œºÚµ•–Ú¡–ªØ
		client.send("Register" + "`" + userName + "`" + password + "`" + email + "`" + phone);
		String msg = client.receive();
		if(msg.trim().equals("OK"))
			return true;
		else
			return false;
	}
	public static void addNumToType(String type,int num) {
		userType.addNumToType(type, num);
	}
	public static UserType getUserType() {
		return userType;
	}
	public static Logs getLogs() {
		return logs;
	}
	public static Activities getActivities() {
		return acts;
	}
	public static void addLog(int money,String type,Date date,String mark) {
		logs.addLog(money, type, date,mark);
	}
	public static void send(String msg) {
		client.send(msg);
	}
	public static String receive() {
		return client.receive();
	}
	public static ObservableList getList() {
		return logs.getLogList();
	}
	public static ObservableList getList(LocalDate beforeDate,LocalDate afterDate) {
		ObservableList list = FXCollections.observableArrayList();
		ObservableList logsList = logs.getLogList();
		Iterator it = logsList.iterator();
		while(it.hasNext()) {
			Log log = (Log)it.next();
			LocalDate logDate = LocalDate.parse(log.getDate());
			if((logDate.isBefore(afterDate) || logDate.isEqual(afterDate)) && (logDate.isAfter(beforeDate) || logDate.isEqual(beforeDate))) {
				list.add(log);
			}
		}
		return list;
	}
	public static int getBudget() {
		return user.getBudget();
	}
	public static void setBudget(int n) {
		user.setBudget(n);
	}
}

