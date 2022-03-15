package kr.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import kr.soldesk.ExcelVO;

public class Project03_D {

	static final String SEARCH_URL = "https://openapi.naver.com/v1/search/book_adv.xml";
	
	public static void main(String[] args) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			System.out.print("책 제목 : ");
			String title = br.readLine();
			System.out.print("저자 : ");
			String author = br.readLine();
			System.out.print("출판사 : ");
			String company = br.readLine();
			
			ExcelVO vo = new ExcelVO(title, author, company);
			System.out.println(vo);
			getISBNImage(vo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getISBNImage(ExcelVO vo) {
		String openAPI = SEARCH_URL + "?";

		try {
			openAPI +=
					  "d_titl=" + URLEncoder.encode(vo.getTitle(), "UTF-8") + "&"
					+ "d_auth=" + URLEncoder.encode(vo.getAuthor(), "UTF-8") + "&"
					+ "d_publ=" + URLEncoder.encode(vo.getCompany(), "UTF-8");
			
			URL url = new URL(openAPI);
			HttpURLConnection con =(HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", "uNvgqvXjFLst_6z3L5oC");
			con.setRequestProperty("X-Naver-Client-Secret", "Iv6CL4rRR4");
			
			int responseCode = con.getResponseCode();
			
			BufferedReader br1 = null;
			if(responseCode == 200) {	//정상 실행
				br1=new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));			 
			}else {
				br1=new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br1.readLine()) != null) {
				response.append(inputLine);
			}
			br1.close();
			System.out.println("응답코드 : " + responseCode);
//			System.out.println(response);
			
			Document doc = Jsoup.parse(response.toString());
//			System.out.println(doc.toString());
			
			Element total = doc.selectFirst("total");
			if(total.text().equals("0")) {
				System.out.println("검색결과가 없습니다.");				
			} else {
				Element isbn = doc.selectFirst("isbn");
				String isbnStr = isbn.text().split(" ")[1];
				//8934944188
				System.out.println("ISBN : " + isbnStr);
				vo.setIsbn(isbnStr);
				//=============================================
				String imgDoc = doc.toString();
				String imgTag = (imgDoc.substring(imgDoc.indexOf("<img>")+5));
				String imgURL = imgTag.substring(0, imgTag.indexOf("?"));
				System.out.println("imgURL : " + imgURL);
				String fileName = imgURL.substring(imgURL.lastIndexOf("/")+1);
				System.out.println(fileName);
				vo.setImageUrl(fileName);
				System.out.println(vo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
