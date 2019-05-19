package bookkeepingClient.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class UserType {
	private HashMap<String,Integer> type;
	private static UserType userType = new UserType();
	
	private UserType() {
		this.type = new HashMap<>();
	}
	public static UserType getInstance() {
		return userType;
	}
	public void initMap(String respond) {
		String s[] = respond.split("`");
		for(String ele:s) {
			String temp[] = ele.split(":");
			this.type.put(temp[0], Integer.valueOf(temp[1]));
		}
	}
	@Override
	public String toString() {
		String res = "";
		Iterator it = this.type.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>)it.next();
			res = res + (String)entry.getKey() + ":" + (Integer)entry.getValue() + "`";
		}
		return res;
	}
	public void addNumToType(String type,int num) {
		int oldNum = this.type.get(type);
		this.type.put(type, num + oldNum);
	}
}
