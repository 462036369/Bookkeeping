package Bookkeeping.ServerPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Ϊ�˷�����̵߳�ͳһ�����˴�Ϊ�̳�Thread�����ʵ��Runnable�ӿ�
class ClientSocketThread extends Thread{
	//�ж��߳��Ƿ���Ҫ�Ͽ�
	private boolean isConnect;
	//���߳�����Ҫ����Ŀͻ���
	private Socket clientSocket;
	//���ݿ��е�id
	private int sqlId;
	//io���
	private DataInputStream Is;
	private DataOutputStream dos;
	
	//���췽�����½����̶߳���ʱ������״̬��ʼ�����ӣ�����һ���ͻ���Socket����
	ClientSocketThread(Socket clientSocket){
		//trueΪ�����ӣ�false��Ҫ�Ͽ�
		this.isConnect = true;
		//���ô���Ŀͻ��˶���
		this.clientSocket = clientSocket;
		this.sqlId = -1;
	}
	//��ȡ״̬
	public boolean isConnect() {
		return this.isConnect;
	}
	//�߳����߼�
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
	 * ��������
	 * @param request ����
	 */
	private void parsingRequest (String request) {
		String[] dic = request.split("`");
		//��½
		if(dic[0].equals("Login")) {
			requestLogin(dic);
		//ע��
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
	 * �����¼����
	 * @param dic �ѽ���������
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
	 * �û���֤
	 * @param userName �û���
	 * @param passWord ����
	 * @return -1 �û�������
	 * @return 0 �������
	 * @return �û�id
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
