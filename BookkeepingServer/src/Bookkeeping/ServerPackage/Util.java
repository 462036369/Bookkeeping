package Bookkeeping.ServerPackage;

public class Util {
	private static Server server = Server.getInstance();
	synchronized public static int selectId(String userName,String passWord){
		try {
			return server.selectId(userName, passWord);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	synchronized public static boolean addUser(String userName,String password,String email,String phone) {
		try {
			return server.addUser(userName, password, email, phone);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 
	synchronized public static void upDateUserType(String userType,int sqlId) {
		try {
			server.upDateUserType(userType, sqlId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	synchronized public static void insertLog(String logs,int sqlId) {
		try {
			server.insertLog(logs, sqlId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	synchronized public static void insertAct(String acts,int sqlId) {
		try {
			server.insertAct(acts, sqlId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	synchronized public static void upDateBudget(String budget,int sqlId) {
		try {
			server.upDateBudget(budget, sqlId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	synchronized public static String getBudget(int sqlId) {
			try {
				return server.getBudget(sqlId);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	synchronized public static String queryUser(int sqlId){
		try {
			return server.queryUser(sqlId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	synchronized public static String queryType(int sqlId){
		try {
			return server.queryType(sqlId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	synchronized public static String queryLog(int sqlId){
		try {
			return server.queryLog(sqlId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	synchronized public static String queryAct(int sqlId){
		try {
			return server.queryAct(sqlId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	synchronized public static void deleteLog(int sqlId){
		try {
			server.deleteLog(sqlId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
