package kr.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Project04F_MultiChatClient {
	
	static final String IP = "172.16.10.15";
	static final int PORT_NUMBER = 9100;
	
	public static void main(String[] args) {
		
		try {
			Socket socket = new Socket(IP, PORT_NUMBER);
			Scanner scan = new Scanner(System.in);
			
			System.out.println("이름을 입력하세요 >");
			String name = scan.nextLine();
			Thread sender = new Thread(new ClientSender(socket, name));
			Thread receiver = new Thread(new ClientReceiver(socket));
			
			sender.start();
			receiver.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class ClientSender extends Thread {

		Socket socket; 
		String name;
		DataOutputStream out;
		Scanner scan;
		public ClientSender(Socket socket, String name) {
			this.socket = socket;
			this.name = name;
			this.scan = new Scanner(System.in);
			try {
				//보내는 스트림 생성
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//메세지 보내기
		@Override
		public void run() {
			try {
				if(out != null) {
					out.writeUTF(name);
				}
				while(out != null) {
					System.out.print("할말 >");
					String message = scan.nextLine();
					if(message.toUpperCase().equals("#QUIT")) {
						break;
					}
					out.writeUTF("[" + name + "] " + message);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				scan.close();
				
				try {
					out.close();
					if(!socket.isClosed())
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	static class ClientReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		String name;
		
		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				//받는 스트림 생성
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				if(!socket.isClosed()) {
					while(in!=null) {
						System.out.println(in.readUTF());	//출력되는 메세지
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
					if(!socket.isClosed())
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		
	}
}
