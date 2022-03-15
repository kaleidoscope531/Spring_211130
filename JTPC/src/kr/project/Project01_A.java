package kr.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.soldesk.BookDTO;

public class Project01_A {

	public static void main(String[] args) {
		//Object(BookDTO) => JSON(String)
		BookDTO dto = new BookDTO("Java", 25000, "한빛미디어", 520);
		Gson g = new Gson();
		String json = g.toJson(dto);
		
		System.out.println("Object to JSON : " + json);
		//JSON(String) => Object(BookDTO)
		BookDTO dto2 = g.fromJson(json, BookDTO.class);
		System.out.println("JSON to Object : " + dto2);
		
		//Object(List<BookDTO>) => JSON(String)
		String[] frontBookName = new String[] {"JAVA", "Oracle", "Spring", "Python", "JavaScript", "HTML/CSS", "React"};
		String[] backBookName = new String[] {"의 정석", " 파헤치기", ", 하루만에 완성", " 입문 가이드", "로 10억 벌기"};
		String[] companyName = new String[] {"한빛미디어", "Pearson", "두빛미디어", "Elevier", "열린책들"};
		
		List<BookDTO> list = new ArrayList<BookDTO>();
		int numberOfBooks = 100;
		for(int i=0;i<numberOfBooks;i++) {
			String title = frontBookName[new Random().nextInt(frontBookName.length)] + backBookName[new Random().nextInt(backBookName.length)];
			int price = new Random().nextInt(100)*100 + 20000;
			String company = companyName[new Random().nextInt(companyName.length)];
			int page = new Random().nextInt(200) + 300;
			list.add(new BookDTO(title, price, company, page));
		}
		String listJson = g.toJson(list);
		System.out.println(listJson);
//		JSON => Object(List<BookDTO>)
		List<BookDTO> list2 = g.fromJson(listJson, new TypeToken<List<BookDTO>>() {}.getType());
		
		for(BookDTO vo:list2) {
			System.out.println(vo);
		}
	}
}
