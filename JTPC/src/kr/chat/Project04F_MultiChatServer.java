package kr.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Project04F_MultiChatServer {

	ConcurrentHashMap<String, DataOutputStream> clients;
	static final int PORT_NUMBER = 9100;

	public Project04F_MultiChatServer() {
		clients = new ConcurrentHashMap<String, DataOutputStream>();
	}
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			System.out.println("server started..");
			
			while(true) {
				socket = serverSocket.accept();
				//IP와 Port번호 출력
				System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " connected!");
				//Thread 생성
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//브로드캐스팅 기능 구현
	void sendToAll(String msg) {
		
		Iterator<Entry<String, DataOutputStream>> iterator = clients.entrySet().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().getValue().writeUTF(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {

		new Project04F_MultiChatServer().start();
	}
	//Inner Class
	class ServerReceiver extends Thread{

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
		
		public ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				//클라이언트 입출력 스트림 생성
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		//start chat
		@Override
		public void run() {
			String name = "";
			
			try {
				
				name = dis.readUTF();
				if(clients.get(name)!= null) {	//같은 이름이 존재
					dos.writeUTF("#이미 접속한 사용자의 이름입니다 : " + name);
					dos.writeUTF("#다른 이름으로 재접속하세요");
					System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "접속 종료!");
					dis.close();
					dos.close();
					socket.close();
					socket = null;
				}else {
					sendToAll("#" + name + "님이 접속하셨습니다!");
					clients.put(name, dos);
					while(dis != null) {
						sendToAll(name+ "님의 말 : " + dis.readUTF());
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(socket != null) {
					sendToAll("#" + name + "님이 퇴장하셨습니다!");
					try {
						dis.close();
						dos.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					socket = null;
				}
			}
		}

	}
}

