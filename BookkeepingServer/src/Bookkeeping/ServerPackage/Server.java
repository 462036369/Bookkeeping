package Bookkeeping.ServerPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;


class Server {
	//���ݿ��ַ
	private static final String url = "jdbc:mysql://localhost:3306/bookkeeping?characterEncoding=GBK&serverTimezone=GMT%2B8";
	//�������ݿ���˻�
	private static final String sqlUserName = "root";
	//�������ݿ���˻�����
	private static final String sqlPassWord = "";
	
	//��Ϊϣ��ֻ����һ��Server��������Թ��췽��˽�л������õ������ģʽ
	private static Server instance = new Server();
	//�������׽���
	private ServerSocket serverSocket;
	//�����ӵĿͻ���
	private Socket clientSocket;
	//�ͻ����߳��б����㼰ʱ�����Ѿ��жϵ��߳�
	private ArrayList<ClientSocketThread> threadList;
	//���ݿ�����
	private Connection connection;
	//ִ��SQL������
	private Statement statement;
	private PreparedStatement pstmt;
	
	//���췽��˽�л�
	private Server(){
		try {
			this.serverSocket = new ServerSocket(8080);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		this.threadList = new ArrayList<>();
	}
	
	/**
	 * ��ñ����Ψһ����
	 * @return �����Ψһ����
	 */
	public static Server getInstance() {
		return instance;
	}
	
	/**
	 * �������ݿⲢ���ö�Ӧ���ӵ�Statement����
	 * @exception ����ʧ��ʱ�׳��쳣
	 */
	public void connectSQL() {
		//��������
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(url,sqlUserName,sqlPassWord);
			this.statement = this.connection.createStatement();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	/**
	 * ��ÿͻ�������
	 * @return �߳��б���
	 */
	public int getClientNum() {
		return this.threadList.size();
	}
	
	/**
	 * �����ͻ�������
	 * @return �µĿͻ��˵�����
	 */
	public Socket getNewConnection(){
		//�����ͻ������ӣ�������ӳɹ����ض�Ӧ�ͻ���Socket���ã����򷵻ؿմ������쳣
		Socket clientSocket = null; 
		try {
			clientSocket = this.serverSocket.accept();//�����������ȴ��ͻ�������	
		} catch (IOException e) {
			return null;
		}
		return clientSocket;
	}
	
	/**
	 * ���߳��б�������߳�
	 * @param clientSocketThread ׼������ӵ����̶߳���
	 */
	public void addThread(ClientSocketThread clientSocketThread) {
		for(int i = 0;i < this.threadList.size();i++) {
			if(!this.threadList.get(i).isConnect()) {
				this.threadList.remove(i);
				i--;
			}
		}
		this.threadList.add(clientSocketThread);
	}
	
	/**
	 * ͨ���û����������ѯid
	 *@return -1 �û�������
	 * @return 0 �������
	 * @return �û�id
	 */
	synchronized public int selectId(String userName,String passWord) throws Exception{
		String sql = "SELECT * FROM user WHERE userName='" + userName + "'";
		ResultSet rs = this.statement.executeQuery(sql);
		if(!rs.next()) {
			return -1;
		}else if(!rs.getString(3).equals(passWord)) {
			return 0;
		}else {
			return rs.getInt(1);
		}
		
	}
	synchronized public boolean addUser(String userName,String password,String email,String phone) throws Exception {
		String sql = "INSERT INTO user(userName,password,email,phone,budget) VALUES (?,?,?,?,3000)";
		String selectSql = "SELECT * FROM user WHERE userName='" + userName + "'";
		ResultSet rs = this.statement.executeQuery(selectSql);
		if(rs.next()) {
			return false;
		}
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		this.pstmt.setString(1, userName);
		this.pstmt.setString(2, password);
		this.pstmt.setString(3, email);
		this.pstmt.setString(4, phone);
		this.pstmt.executeUpdate();
		selectSql = "SELECT id FROM user WHERE userName='" + userName + "'";
		rs = this.statement.executeQuery(selectSql);
		sql = "INSERT INTO type (id) VALUES (?)";
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		rs.next();
		this.pstmt.setInt(1, rs.getInt(1));
		this.pstmt.executeUpdate();
		this.pstmt.close();
		return true;
	}
	synchronized public void upDateBudget(String budget,int sqlId) throws Exception{
		String sql = "UPDATE user SET budget='" + budget + "' WHERE id='" + sqlId + "'";
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		this.pstmt.executeUpdate();
		this.pstmt.close();
	}
	synchronized public void insertLog(String logs,int sqlId) throws Exception{
		if(logs.equals("No record")) {
			return;
		}
		String sql = "INSERT INTO log VALUES (?,?,?,?," + sqlId + ")";
		String[] logList = logs.split("`");
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		for(String ele:logList) {
			String[] log = ele.split("&");
			this.pstmt.setString(1,log[0]);
			this.pstmt.setString(2,log[1]);
			this.pstmt.setString(3,log[2]);
			this.pstmt.setString(4,log[3]);
			this.pstmt.executeUpdate();
		}
		this.pstmt.close();
	}
	synchronized public void upDateUserType(String userType,int sqlId) throws Exception{
		userType = userType.replaceAll(":", "='");
		String[] types = userType.split("`");
		StringBuilder Sql = new StringBuilder("UPDATE type SET ");
		boolean flag = false;
		for(String ele:types) {
			if(ele.isEmpty())
				break;
			if(flag)
				Sql.append("',");
			Sql.append(ele);
			flag = true;
		}
		Sql.append("' WHERE id='" + sqlId + "'");
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(Sql.toString());
		this.pstmt.executeUpdate();
		this.pstmt.close();
	}
	synchronized public String getBudget(int sqlId) throws Exception{
		String sql = "SELECT budget FROM user WHERE id='" + sqlId + "'";
		ResultSet rs = this.statement.executeQuery(sql);
		if(rs.next()) {
			return rs.getString(1);
		}else {
			return null;
		}
	}
	synchronized public String queryUser(int sqlId) throws Exception{
		String sql = "SELECT * FROM user WHERE id='" + sqlId + "'";
		ResultSet rs = this.statement.executeQuery(sql);
		if(rs.next()) {
			return rs.getString(2) + "`" + rs.getInt(6);
		}else {
			return null;
		}
	}
	synchronized public String queryType(int sqlId) throws Exception{
		String sql = "SELECT * FROM type WHERE id='" + sqlId + "'";
		ResultSet rs = this.statement.executeQuery(sql);
		java.sql.ResultSetMetaData data = rs.getMetaData();
		StringBuilder res = new StringBuilder();
		if(rs.next()) {
			for(int i = 2;i <= data.getColumnCount();i++) {
				res = res.append(data.getColumnName(i)).append(":" + rs.getInt(i) + "`");
			}
			return res.toString();
		}else {
			return null;
		}
		
	}
	synchronized public String queryLog(int sqlId) throws Exception{
		String sql = "SELECT * FROM log WHERE userId='" + sqlId + "'";
		ResultSet rs = this.statement.executeQuery(sql);
		StringBuilder res = new StringBuilder();
		boolean flag = false;
		while(rs.next()) {
			flag = true;
			res.append(rs.getInt(1) + "&" + rs.getString(2) + "&" + rs.getString(3) + "&" + rs.getString(4) + "&`");
		}
		if(!flag) {
			res.append("No record");
		}
		return res.toString();
	}
	synchronized public String queryAct(int sqlId) throws Exception{
		String sql = "DELETE FROM activity WHERE time<CURDATE()";
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		this.pstmt.executeUpdate();
		sql = "SELECT * FROM activity WHERE userId='" + sqlId + "' ORDER BY time ASC";
		ResultSet rs = this.statement.executeQuery(sql);
		StringBuilder res = new StringBuilder();
		boolean flag = false;
		while(rs.next()) {
			flag = true;
			res.append(rs.getString(1) + "&" + rs.getString(2) + "`");
		}
		if(!flag) {
			res.append("No record");
		}
		sql = "DELETE FROM activity WHERE userId='" + sqlId + "'";
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		this.pstmt.executeUpdate();
		this.pstmt.close();
		return res.toString();
	}
	synchronized public void insertAct(String acts,int sqlId) throws Exception{
		if(acts.equals("No record")) {
			return;
		}
		String sql = "INSERT INTO activity VALUES (?,?," + sqlId + ")";
		String[] actList = acts.split("`");
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		for(String ele:actList) {
			String[] act = ele.split("&");
			this.pstmt.setString(1,act[0]);
			this.pstmt.setString(2,act[1]);
			this.pstmt.executeUpdate();
		}
		this.pstmt.close();
	}
	synchronized public void deleteLog(int sqlId) throws Exception{
		String sql = "DELETE FROM log WHERE userId='" + sqlId + "'";
		this.pstmt = (PreparedStatement) this.connection.prepareStatement(sql);
		this.pstmt.executeUpdate();
		this.pstmt.close();
	}
}
