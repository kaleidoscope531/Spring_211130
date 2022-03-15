package kr.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Project01_E {
	
//	"https://naveropenapi.apigw.ntruss.com/map-static/v2/raster"
	static final String CLIENT_ID = "8ghlfk3aun";
	static final String CLIENT_SECRET = "qpDSo6hzlVnNdTPVeHrxxXNfNYXItWHcyxX75KV7";
	static final String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
	
	public static void map_service(double point_x, double point_y, String address) {
		map_service(Double.toString(point_x), Double.toString(point_y), address);
	}
	public static void map_service(String point_x, String point_y, String address) {
		try {
			String pos = URLEncoder.encode(point_x + " " + point_y, "UTF-8");
			String urlStr = URL_STATICMAP;
			
			
			urlStr += "center=" + point_x + ","+ point_y;	//찾는 위치가 중앙에 오도록 url 설정
			urlStr += "&level=16&format=png8&w=700&h=500";
			urlStr += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(address, "UTF-8");
			
			URL url = new URL(urlStr);
			System.out.println("url : " + url.toString());
			//HttpURLConnection : URL 연동해주는 API 클래스
			HttpURLConnection con =(HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
			int responseCode = con.getResponseCode();	//200
			
			BufferedReader br;
			if(responseCode == 200) {
				InputStream is = con.getInputStream();
				int read=0;
				byte[] bytes = new byte[1024];	//이미지를 바이트 단위로 처리하여 받아옴
				String tempName = Long.valueOf(new Date().getTime()).toString();
				
				File f = new File(tempName+".png");
				f.createNewFile();	//파일 생성
				
				//출력스트림
				OutputStream outputStream = new FileOutputStream(f);
				
				while((read=is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);	//0부터 읽어들인 만큼 바이트로 변환
				}
				is.close();
				outputStream.close();
				
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while((inputLine=br.readLine()) != null) {
					response.append(inputLine);	//스트림버퍼에 담기
				}
				br.close();
				System.out.println(response.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
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
			System.out.println("주소 : " + z);
			map_service(x, y, z);
			
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
