package bookkeepingClient.model;

import java.util.ArrayList;


public class Activities {
	private ArrayList<Activity> list = new ArrayList<>();
	private static Activities instance = new Activities();
	private Activities() {
	}
	public static Activities getInstance() {
		return instance;
	}
	public void init(String respond) {
		if(respond.equals("No record")) {
			return;
		}
		String[] acts = respond.split("`");
		for(String ele:acts) {
			String[] act = ele.split("&");
			this.list.add(new Activity(act[0],act[1]));
		}
	}
	public void add(String activity,String date) {
		this.list.add(new Activity(activity,date));
	}
	public ArrayList getList() {
		return this.list;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(Activity act:list) {
			s.append(act.getActivity() + "&" + act.getDate() + "`");
		}
		if(s.length() < 5) {
			s.append("No record");
		}
		return s.toString();
	}
}
