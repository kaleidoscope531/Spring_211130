package kr.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Project01_D {
//	"https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode"
//	query=	
	static final String CLIENT_ID = "8ghlfk3aun";
	static final String CLIENT_SECRET = "qpDSo6hzlVnNdTPVeHrxxXNfNYXItWHcyxX75KV7";
	public static void main(String[] args) {
		//new InputStreamReader(System.in) : 키보드로 입력한 데이터를 문자로 변환
		BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("주소를 입력하세요.");
			String address = io.readLine();
			String addr = URLEncoder.encode(address, "UTF-8");
			String reqUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+addr;
			
			URL url = new URL(reqUrl);
			//HttpURLConnection : URL 연동해주는 API 클래스
			HttpURLConnection con =(HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
			BufferedReader br;
			int responseCode = con.getResponseCode();	//200
			if(responseCode == 200) {
				//웹에서 넘어온 데이터
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			
			String line;
			StringBuffer response = new StringBuffer();
			while((line=br.readLine()) != null) {
				response.append(line);	//스트림버퍼에 담기
			}
			br.close();
			
			JSONTokener tokener = new JSONTokener(response.toString());	//JSON객체
			JSONObject object = new JSONObject(tokener);	//Object로 변환
			System.out.println(object.toString(2));
			double x = object.getJSONArray("addresses").getJSONObject(0).getDouble("x");
			double y = object.getJSONArray("addresses").getJSONObject(0).getDouble("y");
			String z = object.getJSONArray("addresses").getJSONObject(0).getString("roadAddress");
			System.out.println("좌표 x:" + x + ", y:" + y);
			
			JSONArray addresses = object.getJSONArray("addresses");	//주소가 여러 개일 때
			
			for(int i=0;i<addresses.length();i++) {
				JSONObject tempAddress = addresses.getJSONObject(i);
				System.out.println("address : " + tempAddress.get("roadAddress"));
				System.out.println("jibunaddress : " + tempAddress.get("jibunAddress"));
				System.out.println("위도 : " + tempAddress.get("x"));
				System.out.println("경도 : " + tempAddress.get("y"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
