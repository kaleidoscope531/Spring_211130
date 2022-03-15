package kr.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import kr.soldesk.ExcelDAO;

public class Project03_F {


	public static void main(String[] args) {
		
		ExcelDAO dao = new ExcelDAO();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			while(true) {
				System.out.print("검색(I)/종료(E) : ");
				String sw = br.readLine();
				switch(sw.toUpperCase()) {
				case "I": 
					dao.excel_input();
					break;
				case "E":
					System.out.println("프로그램을 종료합니다.");
					System.exit(0);
					break;
				default:
					System.err.println("I 또는 E 중 하나를 입력하세요.");	
					System.out.println();
					System.out.println();
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
