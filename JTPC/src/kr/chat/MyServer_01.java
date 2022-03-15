package kr.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer_01 extends Thread{
	
	ServerSocket ss;
	static final int PORT_NUMBER = 3000;
	
	public MyServer_01() {
		try {
			ss = new ServerSocket(PORT_NUMBER);
			
			System.out.println("서버 완료!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void run() {	//항상 서버가 켜진 상태로 유지
		while(true) {
			try {
				
				Socket s = ss.accept();	//클라이언트 수락
				//대기중
				//수락과 동시에 ip와 포트번호를 받아옴.
				String ip = s.getInetAddress().getHostAddress();
				System.out.println(ip + " is entered!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		//채팅 시작
		MyServer_01 myServer = new MyServer_01();
		
		//채팅 시작
		myServer.start();//run();
		
		
	}
	
}
