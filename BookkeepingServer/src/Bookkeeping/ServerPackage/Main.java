package Bookkeeping.ServerPackage;

import java.net.Socket;

class Main {

	public static void main(String[] args) {
		//���Server���Ψһ����
		Server server = Server.getInstance();
		//��������������SQL
		server.connectSQL();
		while(true) {
			//�������ӣ�������������쳣�����¼���
			Socket clientSocket = server.getNewConnection();
			if(clientSocket == null) {
				continue;
			}
			//���µ������׽��ִ����ͻ����̶߳���
			ClientSocketThread clientSocketThread = new ClientSocketThread(clientSocket);
			//�����̼߳��뵽�߳��б�
			server.addThread(clientSocketThread);
			System.out.println("������һ̨�ͻ���" + clientSocket);
			System.out.println("��ǰ����" + server.getClientNum() + "̨�ͻ���");
			//�����߳�
			clientSocketThread.start();
		}
	}

}
