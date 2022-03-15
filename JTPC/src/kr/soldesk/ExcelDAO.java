package kr.soldesk;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ExcelDAO {

	static final String SEARCH_URL = "https://openapi.naver.com/v1/search/book_adv.xml";

	private List<ExcelVO> list;
	private HSSFWorkbook wb;

	public ExcelDAO() {
		list = new ArrayList<ExcelVO>(); // 책의 정보가 들어갈 Bean
		wb = new HSSFWorkbook(); // 워크북 메모리 생성
	}

	public void excel_input() {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			HSSFSheet firstSheet = wb.createSheet("BOOK SHEET"); // 시트 생성
			HSSFRow rowA = firstSheet.createRow(0); // 첫번째 행에 속성명 입력
			HSSFCell cellA = rowA.createCell(0);
			cellA.setCellValue(new HSSFRichTextString("책제목"));
			HSSFCell cellB = rowA.createCell(1);
			cellB.setCellValue(new HSSFRichTextString("저자"));
			HSSFCell cellC = rowA.createCell(2);
			cellC.setCellValue(new HSSFRichTextString("출판사"));
			HSSFCell cellD = rowA.createCell(3);
			cellD.setCellValue(new HSSFRichTextString("isbn"));
			HSSFCell cellE = rowA.createCell(4);
			cellE.setCellValue(new HSSFRichTextString("이미지이름"));
			HSSFCell cellF = rowA.createCell(5);
			cellF.setCellValue(new HSSFRichTextString("이미지"));

			int i = 1;
			while (true) {
				// 가져올 데이터의 정보 이력
				System.out.print("책제목:");
				String title = br.readLine();
				System.out.print("책저자:");
				String author = br.readLine();
				System.out.print("출판사:");
				String company = br.readLine();
				// 다음 행에 가져올 데이터 타입 이력
				HSSFRow rowRal = firstSheet.createRow(i);

				HSSFCell cellTitle = rowRal.createCell(0);
				cellTitle.setCellValue(new HSSFRichTextString(title));

				HSSFCell cellAuthor = rowRal.createCell(1);
				cellAuthor.setCellValue(new HSSFRichTextString(author));

				HSSFCell cellCompany = rowRal.createCell(2);
				cellCompany.setCellValue(new HSSFRichTextString(company));

				// title, author, company 정보 Bean에 저장
				ExcelVO vo = new ExcelVO(title, author, company);
				ExcelVO data = naver_search(vo); // API 통해서 검색(isbn, image)
				list.add(data);
				System.out.println("입력이 완료되었습니다.");
				System.out.println();
				System.out.println("계속 하시겠습니까?(Y/N) : ");
				String key = br.readLine();
				if (key.toUpperCase().equals("N")) {
					break;
				}
				i++;
			}
			System.out.println("데이터 추출중....");
			excel_save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ExcelVO naver_search(ExcelVO vo) {

		try {
			String openAPI = SEARCH_URL + "?" 
					+ "d_titl=" + URLEncoder.encode(vo.getTitle(), "UTF-8") + "&" 
					+ "d_auth=" + URLEncoder.encode(vo.getAuthor(), "UTF-8") + "&" 
					+ "d_publ=" + URLEncoder.encode(vo.getCompany(), "UTF-8");

			URL url = new URL(openAPI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", "uNvgqvXjFLst_6z3L5oC");
			con.setRequestProperty("X-Naver-Client-Secret", "Iv6CL4rRR4");

			int responseCode = con.getResponseCode();

			BufferedReader br1 = null;
			if (responseCode == 200) { // 정상 실행
				br1 = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			} else {
				br1 = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br1.readLine()) != null) {
				response.append(inputLine);
			}
			br1.close();
			System.out.println("응답코드 : " + responseCode);
//			System.out.println(response);

			Document doc = Jsoup.parse(response.toString());
//			System.out.println(doc.toString());

			Element total = doc.selectFirst("total");
			if (total.text().equals("0")) {
				System.out.println("검색결과가 없습니다.");
			} else {
				Element isbn = doc.selectFirst("isbn");
				String isbnStr = isbn.text().split(" ")[1];
				// 8934944188
				System.out.println("ISBN : " + isbnStr);
				vo.setIsbn(isbnStr);
				// =============================================
				String imgDoc = doc.toString();
				String imgTag = (imgDoc.substring(imgDoc.indexOf("<img>") + 5));
				String imgURL = imgTag.substring(0, imgTag.indexOf("?"));
				System.out.println("imgURL : " + imgURL);
				String fileName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
				System.out.println(fileName);
				vo.setImageUrl(fileName);
				System.out.println(vo);

				// DownloadBroker
				Runnable dl = new DownloadBroker(imgURL, "./resources/"+fileName);
				Thread t = new Thread(dl);
				t.start();
				
				while(t.isAlive()) {
					Thread.sleep(100);
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	public void excel_save() {
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			if (wb != null && sheet != null) {
				Iterator<Row> rows = sheet.rowIterator();
				rows.next();
				int i = 0; // list의 index

				while (rows.hasNext()) {
					HSSFRow row = (HSSFRow) rows.next();
					HSSFCell cell = row.createCell(3);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(i).getIsbn());

					cell = row.createCell(4);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(i).getImageUrl());
					
					InputStream is = new FileInputStream("./resources/"+list.get(i).getImageUrl());
					byte[] bytes = IOUtils.toByteArray(is);	//이미지를 바이트 단위로 읽어오기
					int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
					is.close();
					
					CreationHelper helper = wb.getCreationHelper();	//실제로 드로잉을 도와주는 메소드
					Drawing drawing = sheet.createDrawingPatriarch();	//드로잉 객체생성

					ClientAnchor anchor = helper.createClientAnchor();	//시트 내 위치 지정
					anchor.setCol1(5);
					anchor.setRow1(i+1);
					anchor.setCol2(6);
					anchor.setRow2(i+2);
					
					//지정된 위치에 이미지 생성 완료
					Picture picture = drawing.createPicture(anchor, pictureIdx);
					
					Cell cellImg = sheet.createRow(2).createCell(1);
					int widthUnits = 20*256;	//폭 하나당 256분의 1
					sheet.setColumnWidth(1, widthUnits);
					
					short heightUnits = 120*20;	//높이 한 칸을 20분의 1로
					
					cellImg.getRow().setHeight(heightUnits);

					i++;
					
					
				}
				
				FileOutputStream fos = new FileOutputStream("isbn.xls");
				wb.write(fos);
				fos.close();
				System.out.println("ISBN, imageURL 저장 성공!!");
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
