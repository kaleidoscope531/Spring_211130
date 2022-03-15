package kr.project;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

public class Project03_B {
	
	public static void main(String[] args) {
		
		try {
			Workbook workbook = new HSSFWorkbook();	// 메모리에 가상의 워크북 만들기
			Sheet sheet = workbook.createSheet("My Sample Excel");	//시트 만들기
			InputStream is = new FileInputStream("./resources/Kingdom_Of_Heaven-01.jpg");
			byte[] bytes = IOUtils.toByteArray(is);	//이미지를 바이트 단위로 읽어오기
			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			is.close();
			
			CreationHelper helper = workbook.getCreationHelper();	//실제로 드로잉을 도와주는 메소드
			Drawing drawing = sheet.createDrawingPatriarch();	//드로잉 객체생성
			
			ClientAnchor anchor = helper.createClientAnchor();	//시트 내 위치 지정
			anchor.setCol1(1);
			anchor.setRow1(2);
			anchor.setCol2(2);
			anchor.setRow2(3);
			
			//지정된 위치에 이미지 생성 완료
			Picture picture = drawing.createPicture(anchor, pictureIdx);
			
			Cell cell = sheet.createRow(2).createCell(1);
			int w = 20*256;	//폭 하나당 256분의 1
			sheet.setColumnWidth(1, w);
			
			short h = 120*20;	//높이 한 칸을 20분의 1로
			
			cell.getRow().setHeight(h);
			
			FileOutputStream fileOut = new FileOutputStream("myFile.xls");	//Excel 파일 생성
			workbook.write(fileOut); //파일에 이미지 저장됨
			System.out.println("이미지 생성 성공!!");
			workbook.close();
			fileOut.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
