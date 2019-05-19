package Bookkeeping.ServerPackage;

import java.net.Socket;

class Main {

	public static void main(String[] args) {
		//获得Server类的唯一对象
		Server server = Server.getInstance();
		//加载驱动及连接SQL
		server.connectSQL();
		while(true) {
			//监听连接，如果监听出现异常则重新监听
			Socket clientSocket = server.getNewConnection();
			if(clientSocket == null) {
				continue;
			}
			//以新的连接套接字创建客户端线程对象
			ClientSocketThread clientSocketThread = new ClientSocketThread(clientSocket);
			//将新线程加入到线程列表
			server.addThread(clientSocketThread);
			System.out.println("连接了一台客户机" + clientSocket);
			System.out.println("当前连接" + server.getClientNum() + "台客户机");
			//启动线程
			clientSocketThread.start();
		}
	}

}
