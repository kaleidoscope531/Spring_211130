package kr.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Project04A_Client {

	static ServerSocket ss;
	static final String IP = "172.16.10.15";
	static final int PORT_NUMBER = 9100;
	public static void main(String[] args) {
		
		try {
			
			Socket socket = new Socket(IP, PORT_NUMBER);
			System.out.println("Connection Success!!");
			Scanner scan = new Scanner(System.in);
			String message = "";
			while(!(message.toUpperCase().equals("#EXIT"))) {
				message = scan.nextLine();
				if(!message.isBlank()) {
					//서버에게 정보 보냄
					OutputStream out = socket.getOutputStream();
					
					//출력스트림
					DataOutputStream dos = new DataOutputStream(out);
					dos.writeUTF(message);	//메세지 보냄
					
					//입력소켓
					InputStream in = socket.getInputStream();
					DataInputStream dis = new DataInputStream(in);
					
					System.out.println("Receive : " + dis.readUTF());
					
					dos.close();
					dis.close();
				}
			}
			scan.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
