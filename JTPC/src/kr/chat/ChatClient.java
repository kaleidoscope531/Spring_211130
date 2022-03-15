package kr.chat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;


public class ChatClient extends JFrame implements Runnable{

	private static final long serialVersionUID = -7104319238197209275L;
	
	static String ip;
	static int port_number;
	
	JTextArea area;	//메세지 출력 공간
	JTextField input;
	JButton send_bt, connect_bt, disconnect_bt;	//메세지 전송
	JPanel south_p, north_p;
	JLabel ipLabel, portLabel;
	JTextField ipInput, portInput;
	
	//서버 접속을 위한 객체
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	Thread t;
	
	public ChatClient() {
		area = new JTextArea();
		this.add(area);
		
		south_p = new JPanel(new BorderLayout());
		south_p.add(input = new JTextField());
		south_p.add(send_bt = new JButton("보내기"), BorderLayout.EAST);

		ipLabel = new JLabel("IP:");
		portLabel = new JLabel("PORT:");
		north_p = new JPanel(new FlowLayout());
		north_p.setSize(400, 20);
		ipLabel.add(ipInput = new JTextField());
		portLabel.add(portInput = new JTextField());
		ipInput.setSize(240, 20);
		portInput.setSize(80, 20);
		north_p.add(ipLabel);
		north_p.add(portLabel, BorderLayout.EAST);
		
		north_p.add(connect_bt = new JButton("접속"),BorderLayout.EAST);
		north_p.add(disconnect_bt = new JButton("해제"),BorderLayout.EAST);
		
		this.add(north_p, BorderLayout.NORTH);
		this.add(south_p, BorderLayout.SOUTH);

		//이벤트 감지
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				out.println("xx:~~X");
			}
		});
		
		send_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendData();	//서버로 메세지 전달
			}


		});
		
		
		setBounds(100, 100, 400, 500);
		setVisible(true);
		
		connect_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ip = ipInput.getText();
					port_number =Integer.parseInt(portInput.getText());					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				connect();
				if(socket.isConnected()) {
					connect_bt.setEnabled(false);
					ipInput.setEnabled(false);
					portInput.setEnabled(false);
					disconnect_bt.setEnabled(true);
				}
			}
		});
		
		disconnect_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				if(socket.isClosed()) {
					
					connect_bt.setEnabled(true);
					ipInput.setEnabled(true);
					portInput.setEnabled(true);
					disconnect_bt.setEnabled(true);
				}
				ip = "";
				port_number = 0;
			}
		});
		
		t = new Thread(this);	//서버가 주는 메세지를 감지하여 받음
		t.start();
	}
	
	//=================================================================
	//연결
	
	private void connect() {
		try {
			socket = new Socket(ip, port_number);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData() {
		String msg = input.getText().trim();
		out.println(msg);
		input.setText("");
	}
	
	private void close() {
		try {
			if(out != null) {
				out.close();
			}
			if(in != null) {
				in.close();
			}
			if(socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ChatClient();
	}
	
	@Override
	public void run() {

		while(true) {
			try {
				
				String message = in.readLine();	//대기상태
				if(message.equals("xx:~~X"))
					break;
				if(!message.isBlank()) {
					area.append(message+"\r\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		System.exit(0);
	}

}
