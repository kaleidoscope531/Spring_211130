package kr.chat;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient_01 {

	static final String IP = "172.16.10.119";
	static final int PORT_NUMBER = 3000;
	
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		
		Socket socket = new Socket(IP, PORT_NUMBER);
	}
	
}
