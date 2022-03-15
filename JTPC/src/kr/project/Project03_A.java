package kr.project;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import kr.soldesk.ExcelVO;

public class Project03_A {

	public static void main(String[] args) {
		String fileName = "bookList.xls";
		String[] constArgs = new String[5];
		List<ExcelVO> data = new ArrayList<ExcelVO>();
		
		try(FileInputStream fis = new FileInputStream(fileName)) {
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
//			HSSFSheet sheet = workbook.getSheet(0);	//메모리에 sheet적재
			HSSFSheet sheet = workbook.getSheet("bookList");	//메모리에 sheet적재
			Iterator<Row> rows = sheet.rowIterator();	//시트에 있는 행 순회하여 개수 반환
			rows.next();	//첫번째 줄 지나가기
			while(rows.hasNext()) {
				HSSFRow row = (HSSFRow)rows.next();	//줄 가져오기
				Iterator<Cell> cells = row.cellIterator();	//행에서 열 순회하여 개수 반환
				
				int i = 0;
				//셀의 데이터 가져오기
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();	//셀 읽기
					constArgs[i++] = cell.toString();
				}
				//VO클래스 객체 생성
				ExcelVO vo = new ExcelVO(constArgs[0],constArgs[1],constArgs[2],constArgs[3],constArgs[4]);
				//컬렉션에 담기
				data.add(vo);
				workbook.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		showExcelData(data);
	}
	
	public static void showExcelData(List<ExcelVO> data) {
		for(ExcelVO vo:data) {
			System.out.println(vo);
		}
	}
}
