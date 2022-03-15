package kr.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.soldesk.DownloadBroker;


public class Project02_B {
	static final String MAIN_URL = "https://sum.su.or.kr:8888";
	static final String URL = "https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BodyMatter?qt_ty=QT1";
	public static void main(String[] args) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("[입력 -> 년(yyyy)-월(mm)-일(dd)] : ");
			String date = br.readLine();
			String urlStr = URL + "&Base_de=" + date + "&bibleType=1";
			System.out.println("=============================");
			br.close();
			Document doc = Jsoup.connect(urlStr).post();
			
//			System.out.println(doc);

			Element bible_text=doc.select(".bible_text").first();
			Element bibleinfo_box = doc.selectFirst(".bibleinfo_box");
			
			//내용
			
			
			System.out.println(bible_text.text());
			System.out.println("-----------------------------");
			System.out.println(bibleinfo_box.text());
//			System.out.println(liList);
			System.out.println();
			Elements liList = doc.select(".body_list > li");
			liList.forEach(li-> System.out.println(li.selectFirst(".num").text() + " : " + li.selectFirst(".info").text()));
			System.out.println();
			// 리소스 다운로드(mp3, image)
			//mp3
			Element tag = doc.select("source").first();
			String dPath = tag.attr("src").trim();	//속성을 붙음
			
			//image
//			Element tag = doc.select(".img>img").first();
//			String dPath = MAIN_URL + tag.attr("src").trim();	//속성을 붙음
			
			
			
			System.out.println(dPath);
			String fileName = dPath.substring(dPath.lastIndexOf("/")+1);
			System.out.println(fileName);
			
			Runnable downloader = new DownloadBroker(dPath, fileName);
			Thread dLoad = new Thread(downloader);	//스레드 구현
			dLoad.start();	//다운로드 시작
			for(int i=0;i<10;i++) {
				try {
					Thread.sleep(1000);	//1초
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!dLoad.isAlive()) {
					break;
				}
				System.out.println(""+(i+1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
