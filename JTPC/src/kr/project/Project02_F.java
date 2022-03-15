package kr.project;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Project02_F extends JFrame implements ActionListener, ItemListener{

	static final String MAIN_URL = "https://sum.su.or.kr:8888";
	static final String URL = "https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BodyMatter?qt_ty=QT1";
	static final Color SELECTED_COLOR = new Color(12211667);
	static final int NUMBER_OF_CHAR_IN_LINE = 40;
	private Choice chyear, chmonth;
	private JLabel yLabel, mLabel;
	private JTextArea area;
	GregorianCalendar gc;
	private int year, month;
	private JLabel[] dayLabel = new JLabel[7];
	private String[] day={"일","월","화","수","목","금","토"};
	private JButton[] days = new JButton[42];//7일이 6주이므로 42개의 버튼필요
	private JPanel selectPanel = new JPanel();
	private GridLayout grid = new GridLayout(7,7,5,5);//행,열,수평갭,수직갭
	private Calendar ca = Calendar.getInstance();
	private Dimension dimen, dimen1;
	private int xpos, ypos;
	
	public Project02_F() {
//		Calendar.MONTH : 1월이 0
		setTitle("오늘의 QT : " + ca.get(Calendar.YEAR)+"/"+(ca.get(Calendar.MONTH)+1)+"/"+ca.get(Calendar.DATE));
		
		setSize(900,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dimen = Toolkit.getDefaultToolkit().getScreenSize();	//화면의 크기
		dimen1 = this.getSize();	//프레임 크기
		
		xpos =(int) (dimen.getWidth() - dimen1.getWidth())/2;
		ypos = (int) (dimen.getHeight() - dimen1.getHeight())/2;
		
		setLocation(xpos, ypos);
		setResizable(false);
		getContentPane().add(selectPanel, "North");

		setVisible(true);
		
		chyear = new Choice();
		chmonth = new Choice();
		
		yLabel = new JLabel("년");
		mLabel = new JLabel("월");
		
		
		init();
		
	}
	
	public void init() {
		select();
		calendar();
	}
	
	private void select() {
		JPanel panel = new JPanel(grid);
		for(int i=2022; i>=2000;i--){
			chyear.add(String.valueOf(i));
		}
		for(int i=1; i <=12; i++){
			chmonth.add(String.valueOf(i));
		}
		for(int i=0; i<day.length; i++) {	//요일 이름을 레이블에 출력
			dayLabel[i] = new JLabel(day[i], JLabel.CENTER);
			panel.add(dayLabel[i]);
			dayLabel[i].setBackground(Color.GRAY);
		}
		dayLabel[6].setForeground(Color.BLUE);	//토요일 색상
		dayLabel[0].setForeground(Color.RED);		//일요일 색상
		
		for(int i = 0; i < 42;i++){
			days[i] = new JButton("");
			if(i % 7 == 0)
				days[i].setForeground(Color.RED);
			else if(i % 7 == 6)
				days[i].setForeground(Color.BLUE);
			else
				days[i].setForeground(Color.BLACK);
			days[i].addActionListener(this);
			panel.add(days[i]);
			
			selectPanel.add(chyear);
			selectPanel.add(yLabel);
			selectPanel.add(chmonth);
			selectPanel.add(mLabel);
			
			this.add(panel,"Center");
			
			area = new JTextArea(60,40);
			area.setCaretPosition(area.getDocument().getLength());
			JScrollPane scrollPane = new JScrollPane(area);
			this.add(scrollPane, "East");
			
			String month = Integer.toString(ca.get(Calendar.MONTH) + 1);
			String year = Integer.toString(ca.get(Calendar.YEAR));
			chyear.select(year);
			chmonth.select(month);
			chyear.addItemListener(this);
			chmonth.addItemListener(this);
		}
		
	}

	private void calendar() {
		year = Integer.parseInt(chyear.getSelectedItem());
		month = Integer.parseInt(chmonth.getSelectedItem());
		
		gc = new GregorianCalendar(year, month-1, 1);	//해당 년 월을 토대로 그레고리력 생성
		int max = gc.getActualMaximum(Calendar.DAY_OF_MONTH);	//해당 달의 최대 일수 획득
		int week = gc.get(Calendar.DAY_OF_WEEK);	//해당 달의 시작 요일
		System.out.println("DAY_OF_WEEK" + week);
		
		String today = Integer.toString(ca.get(Calendar.DATE));	//오늘 날짜
		String today_month = Integer.toString(ca.get(Calendar.MONTH)+1);	//오늘의 달
		
		System.out.println("today" + today);
		for(int i = 0; i<days.length; i++) {
			days[i].setEnabled(true);
		}
		for(int i = 0; i<week-1; i++) {//시작일 이전의 버튼 비활성화
			days[i].setEnabled(false);
		}
		
		for(int i=week; i<max+week; i++) {
			days[i-1].setText(String.valueOf(i-week+1));
			days[i-1].setBackground(Color.WHITE);
			if(today_month.equals(String.valueOf(month))) {	//오늘이 속한 달과 일치
				if(today.equals(days[i-1].getText())) {	//선택한 날이 맞냐?
					days[i-1].setBackground(SELECTED_COLOR);	//버튼색 변경
				}
			}
		}
		this.repaint();
	}

	//무언가 선택 시 기존 정보 클리어
	@Override
	public void itemStateChanged(ItemEvent e) {
		Color color = this.getBackground();
		if(e.getStateChange()==ItemEvent.SELECTED) {
			for(int i = 0; i< 42; i++) {	//기존 달력 지우고 새로 시작
				if(!days[i].getText().isBlank()) {	//기존 내용이 있다면
					days[i].setText("");	//지우기
					days[i].setBackground(color);//그레이색으로 기본 설정
				}
			}
			calendar();			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		area.setText("");
		String year = chyear.getSelectedItem();	//선택된 객체의 내용
		String month = chmonth.getSelectedItem();	//선택된 객체의 내용
		JButton btn = (JButton) e.getSource();
		String day = btn.getText();	//메소드를 이용하여 현재 텍스트박스의 내용만 출력
		
		String bible = year + "-" + month + "-" + day;
		System.out.println(bible);
		
		//Jsoup API : HTML 파싱
		String url = URL + "&Base_de=" + bible + "&bibleType=1";
		
		try {
			Document doc = Jsoup.connect(url).post();
//			System.out.println(doc.toString());
			Element dailyBible_title=doc.select(".bible_text").first();
			Element dailyBible_subtitle = doc.selectFirst(".bibleinfo_box");
			Element dailyBible_info = doc.selectFirst("#dailybible_info");
			Elements liList = doc.select(".body_list>li");
			
			System.out.println(dailyBible_title.text());
			System.out.println(dailyBible_subtitle.text());
			System.out.println(dailyBible_info.text());
			liList.forEach(li -> System.out.println(li.text()));
			
			
			
			//GUI area 텍스트에 붙이기
			area.append(dailyBible_title.text() + "\n");
			area.append(dailyBible_subtitle.text() + "\n");
			area.append(dailyBible_info.text() + "\n\n");
			for(Element li : liList) {
				StringBuffer text = new StringBuffer();
				text.append(li.selectFirst(".num").text() + " : ");
				CharSequence article = li.selectFirst(".info").text();
				for(int i=0;i<article.length();i++) {
					text.append(article.charAt(i));
					if(text.length()%NUMBER_OF_CHAR_IN_LINE==0) {
						text.append("\n");
					}
				}
				text.append("\n");
				area.append(text.toString());
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		calendar();
	}

	public static void main(String[] args) {
		new Project02_F();
	}
	
}
