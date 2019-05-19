package bookkeepingClient.model;

import java.time.LocalDate;

class User {
	private String userName;
	private int budget;
	private int budgetDate;
	private static User user = new User();
	private User() {
	}
	public static User getInstance() {
		return user;
	}
	public int getBudgetDate() {
		return this.budgetDate;
	}
	public void initInfo(String s) {
		String[] temp = s.split("`");
		this.userName = temp[0];
		this.budget = Integer.valueOf(temp[1]);
		this.budgetDate = this.budget / LocalDate.now().lengthOfMonth();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
		this.budgetDate = this.budget / LocalDate.now().lengthOfMonth();
	}
}
