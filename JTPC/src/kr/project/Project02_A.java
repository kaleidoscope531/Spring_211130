package kr.project;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//crawling
public class Project02_A {
	static final String URL = "https://sports.news.naver.com/kfootball/index.nhn";
	public static void main(String[] args) {
		
		Document doc = null;	//Jsoup에서 얻어온 HTML 전체 문서(DOM -> Document Object Model)
		try {
			doc = Jsoup.connect(URL).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Elements elements = doc.select("div.home_news");
		
		String title = elements.select("div.head>h2").text().substring(0,4);
		System.out.println("==============================");
		System.out.println("        "+title);
		System.out.println("==============================");
		elements.select("li").forEach(element -> System.out.println(element.text()));
		
	}
}
