package Bookkeeping.ServerPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//为了方便对线程的统一管理，此处为继承Thread类而非实现Runnable接口
class ClientSocketThread extends Thread{
	//判断线程是否需要断开
	private boolean isConnect;
	//该线程所需要服务的客户端
	private Socket clientSocket;
	//数据库中的id
	private int sqlId;
	//io相关
	private DataInputStream Is;
	private DataOutputStream dos;
	
	//构造方法，新建该线程对象时，连接状态初始已连接，接收一个客户端Socket对象
	ClientSocketThread(Socket clientSocket){
		//true为已连接，false需要断开
		this.isConnect = true;
		//引用传入的客户端对象
		this.clientSocket = clientSocket;
		this.sqlId = -1;
	}
	//获取状态
	public boolean isConnect() {
		return this.isConnect;
	}
	//线程主逻辑
	@Override
	public void run() {
		while(this.isConnect) {
			String request = receive();
			parsingRequest(request);
		}
		try {
			if(dos != null)
				this.dos.close();
			if(Is != null)
				this.Is.close();
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private String receive() {
		try {
			Is = new DataInputStream(this.clientSocket.getInputStream());
			String msg = Is.readUTF();
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 解析请求
	 * @param request 请求
	 */
	private void parsingRequest (String request) {
		String[] dic = request.split("`");
		//登陆
		if(dic[0].equals("Login")) {
			requestLogin(dic);
		//注册
		}else if(dic[0].equals("Register")) {
			requestRegister(dic);
		}else if(dic[0].equals("SetBudget")) {
			Util.upDateBudget(dic[1], this.sqlId);
		}else if(dic[0].equals("GetBudget")) {
			send(Util.getBudget(this.sqlId));
		}
		else if(dic[0].equals("quit")) {
			this.isConnect = false;
			handleQuit();
		}
	}
	private void handleQuit() {
		String budget = receive();
		if(budget.equals("NoLogin")) {
			return;
		}
		String userType = receive();
		String logs = receive();
		String acts = receive();
		Util.upDateBudget(budget, this.sqlId);
		Util.upDateUserType(userType,this.sqlId);
		Util.insertLog(logs, this.sqlId);
		Util.insertAct(acts, this.sqlId);
	}
	/**
	 * 处理登录请求
	 * @param dic 已解析的数组
	 */
	private void requestLogin(String[] dic) {
		int res = verificationUser(dic[1], dic[2]);
		if(res == 0) {
			send("ErrorPassword");
		}else if(res == -1) {
			send("NoUser");
		}else {
			send("OK");
			this.sqlId = res;
			String user = Util.queryUser(this.sqlId);
			send(user);
			String type = Util.queryType(this.sqlId);
			send(type);
			String log = Util.queryLog(this.sqlId);
			send(log);
			Util.deleteLog(this.sqlId);
			String act = Util.queryAct(this.sqlId);
			send(act);
		}
	}
	
	private void requestRegister(String[] dic) {
		String[] temp = dic;
		dic = new String[5];
		for(int i = 0;i < 5;i++) {
			if(i < temp.length) {
				dic[i] = temp[i];
			}else {
				dic[i] = "";
			}
		}
		boolean res = Util.addUser(dic[1], dic[2], dic[3], dic[4]);
		if(!res) {
			send("RepeatUserName");
		}else {
			send("OK");
		}
	}
	
	private void send(String msg) {
		try {
			dos = new DataOutputStream(this.clientSocket.getOutputStream());
			dos.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用户验证
	 * @param userName 用户名
	 * @param passWord 密码
	 * @return -1 用户不存在
	 * @return 0 密码错误
	 * @return 用户id
	 */
	private int verificationUser (String userName,String passWord) {
		try {
			return Util.selectId(userName, passWord);
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}
	
}
