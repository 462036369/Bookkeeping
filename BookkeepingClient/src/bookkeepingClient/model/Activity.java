package bookkeepingClient.model;

public class Activity {
	private String activity;
	private String date;
	public Activity(String activity, String date) {
		this.activity = activity;
		this.date = date;
	}
	public Activity() {
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}