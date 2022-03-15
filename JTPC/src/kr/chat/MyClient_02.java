package kr.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyClient_02 {

//	static final String IP = "172.16.10.119";
	static final String IP = "172.16.10.32";
	
	static final int PORT_NUMBER = 9999;
	
	
	public static void main(String[] args){
		
//		Socket socket = new Socket(IP, PORT_NUMBER);
		Scanner scan = new Scanner(System.in);
		Socket socket= null;
		
		System.out.print("입력 >");
		String msg = scan.nextLine();
		
		if(!msg.isBlank()) {
			try {
				socket = new Socket(IP, PORT_NUMBER);
				
				
				//문자열을 보내기 위한 스트림을 준비
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				//서버에 문자열 보내기
				
				out.write(msg);
				//다음 메세지를 위하여 스트림 비우기
				out.flush();
				
				if(out != null) {
					out.close();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(!socket.isClosed()) {
						socket.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
}
