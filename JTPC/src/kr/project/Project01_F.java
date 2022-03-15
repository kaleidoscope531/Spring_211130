package kr.project;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.*;

public class Project01_F {

	JTextField address;	//주소 입력할 부분 변수선언
	JLabel resAddress, resX, resY, jibunAddress;
	JLabel imageLabel;	//지도 이미지 변수선언
	
	static int rows = 4;
	static int columns = 1;
	public void initGUI() {
		//GUI 메서드
		JFrame frame = new JFrame("Map View"); //프레임과 제목
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//ui 구현부
		Container c = frame.getContentPane();
		
		imageLabel = new JLabel("지도보기");
		
		JPanel addressInputPanel = new JPanel();	//맨 위 주소 부분
		JLabel addressLabel = new JLabel("주소입력");
		address = new JTextField(50);
		
		JButton inputButton = new JButton("클릭");
		
		addressInputPanel.add(addressLabel);
		addressInputPanel.add(address);
		addressInputPanel.add(inputButton);
		
		//지도 이미지 생성
		inputButton.addActionListener(new NaverMap(this));
		
		//API 정보
		JPanel infoPanel = new JPanel();
		
		infoPanel.setLayout(new GridLayout(rows, columns));
		resAddress = new JLabel("도로명주소");
		jibunAddress = new JLabel("지번주소");
		resX = new JLabel("경도");
		resY = new JLabel("위도");
		infoPanel.add(resAddress);
		infoPanel.add(jibunAddress);
		infoPanel.add(resX);
		infoPanel.add(resY);
		
		
		c.add(BorderLayout.NORTH, addressInputPanel);
		c.add(BorderLayout.CENTER, imageLabel);
		c.add(BorderLayout.SOUTH, infoPanel);
		
		frame.setSize(730, 660);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Project01_F().initGUI();
	}
}
