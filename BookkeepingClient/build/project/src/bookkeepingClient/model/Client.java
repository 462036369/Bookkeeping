package bookkeepingClient.model;

import java.io.*;
import java.net.Socket;

public class Client {
	//�ͻ����׽���
	private Socket socket;
	private static Client instance = new Client();
	private DataOutputStream dos;
	private DataInputStream dis; 
	//���췽��˽�л�
	private Client() {
		try {
			this.socket = new Socket("localhost",8080);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	//ȡ�ñ���Ψһ����
	public static Client getInstance() {
		return instance;
	}
	/**
	 * ���������������
	 * @param msg Ҫ���͵��ַ���
	 */
	public void send(String msg) {
		//ͨ��io��������
		try {
			dos = new DataOutputStream(this.socket.getOutputStream());
			dos.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �ӷ�������������
	 * @return �������ݵ��ַ�����ʾ
	 */
	public String receive() {
		//ͬ��io
		try {
			dis = new DataInputStream(this.socket.getInputStream());
			String msg = dis.readUTF();
			//dis.close();
			return msg;
		} catch (IOException e) {
			//�쳣����
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * �ر��׽���
	 */
	public void close() {
		try {
			this.socket.close();
			this.dos.close();
			this.dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
