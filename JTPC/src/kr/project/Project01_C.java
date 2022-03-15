package kr.project;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Project01_C {

	public static void main(String[] args) {
		String src = "info.json";
		
		//IO->Stream
		InputStream is = Project01_C.class.getResourceAsStream(src);
		if(is==null) {
			throw new NullPointerException("Cannot find resource file name : " + src );
		}
		
		JSONTokener tokener = new JSONTokener(is);	//문자열(json)이 JSON객체로 변환
		JSONObject object = new JSONObject(tokener);	//JSON 객체를 Object로 변환
		JSONArray students = object.getJSONArray("students");	//Array JSON
		
		students.forEach(student -> System.out.println(((JSONObject)student).toString(2)));
		
		String src2 = "info2.json";		
		InputStream is2 = Project01_C.class.getResourceAsStream(src2);
		if(is2==null) {
			throw new NullPointerException("Cannot find resource file name : " + src2 );
		}
		
		tokener = new JSONTokener(is2);
		JSONArray books = new JSONArray(tokener);
		books.forEach(book -> System.out.println(((JSONObject)book).toString(2)));
		
	}
}
