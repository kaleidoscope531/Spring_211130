package kr.project;

import org.json.JSONArray;
import org.json.JSONObject;

public class Project01_B {

	public static void main(String[] args) {
		//JSON => Java(org.json)
		JSONArray students = new JSONArray();	//[{}, {}, {}, ....]
		JSONObject student = new JSONObject();
		student.put("name", "홍길동");
		student.put("phone", "010-111-2222");
		student.put("address", "그랜드라인 임펠다운");
		System.out.println(student.toString(2));
		students.put(student);
		//---------------------------------------------
		student = new JSONObject();
		student.put("name", "김길동");
		student.put("phone", "010-222-3333");
		student.put("address", "웨스트블루 해상 레스토랑 발라티에");
		System.out.println(student.toString(2));
		students.put(student);
		//---------------------------------------------
		JSONObject	object = new JSONObject();
		object.put("students", students);
		System.out.println("=============================================");
		System.out.println(students);
		System.out.println(object.toString(2));
	}
}
