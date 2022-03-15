package kr.chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer extends Thread{
	
	static final int PORT_NUMBER = 3500;
	
	ServerSocket serverSocket;
	ArrayList<CopyClient> list;
	
	public ChatServer() {

		try {
			list = new ArrayList<CopyClient>();
			serverSocket = new ServerSocket(PORT_NUMBER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ChatServer().start();
	}

	@Override
	public void run() {
		//접속자를 기다림(대기)
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				String ip = socket.getInetAddress().getHostAddress();
				System.out.println(ip + "님 접속!!");
				
				CopyClient copyClient = new CopyClient(socket, this);
				list.add(copyClient);
				copyClient.start();	//클라이언트 복사본의 스레드 시작
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void sendMessage(String message) {
		try {
			for(CopyClient copyClient:list) {
				copyClient.out.println(message);	//메세지 전달
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeClient(CopyClient copyClient) {
		sendMessage("☆★☆★" + copyClient.ip + "님 퇴장☆★☆★");
		
	}
	
}
