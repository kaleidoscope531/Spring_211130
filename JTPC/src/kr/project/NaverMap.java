package kr.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import kr.soldesk.AddressVO;

public class NaverMap implements ActionListener{

	static final String CLIENT_ID = "8ghlfk3aun";
	static final String CLIENT_SECRET = "qpDSo6hzlVnNdTPVeHrxxXNfNYXItWHcyxX75KV7";
	static final String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
	static final String URL_GEOCODEWQUERY = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
	static Project01_F naverMap;
	
	
	
	public NaverMap(Project01_F naverMap) {
		super();
		NaverMap.naverMap = naverMap;
	}



	public static void main(String[] args) {
		
	}


//클릭하면 실행되는 부분
	@Override
	public void actionPerformed(ActionEvent e) {
		AddressVO vo = getAddressVO();
		map_service(vo);
	}
	
	public static AddressVO getAddressVO() {
		AddressVO vo = null;
		
		try {
			String address = naverMap.address.getText();
			String addr = URLEncoder.encode(address, "UTF-8");
			String reqUrl = URL_GEOCODEWQUERY+addr;
			
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
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine=br.readLine()) != null) {
				response.append(inputLine);	//스트림버퍼에 담기
			}
			br.close();

			JSONTokener tokener = new JSONTokener(response.toString());	//JSON객체
			JSONObject object = new JSONObject(tokener);	//Object로 변환			
			JSONArray addresses = object.getJSONArray("addresses");	//주소가 여러 개일 때
			
			vo = new AddressVO();
			
			if(!addresses.isEmpty()) {
				JSONObject tempAddress = addresses.getJSONObject(0);
				
				vo.setRoadAddress(tempAddress.getString("roadAddress"));
				vo.setJibunAddress(tempAddress.getString("jibunAddress"));
				vo.setX(tempAddress.getString("x"));
				vo.setY(tempAddress.getString("y"));
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return vo;
	}
	
	public static void map_service(AddressVO vo) {
		try {
			String pos = URLEncoder.encode(vo.getX() + " " + vo.getY(), "UTF-8");
			String urlStr = URL_STATICMAP;
			
			
			urlStr += "center=" + vo.getX() + ","+ vo.getY();	//찾는 위치가 중앙에 오도록 url 설정
			urlStr += "&level=16&format=jpg&w=700&h=500";
			urlStr += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(vo.getRoadAddress(), "UTF-8");
			
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
				String tempName = "currentMapImage";
				
				File f = new File(tempName+".jpg");
				f.createNewFile();	//파일 생성
				
				//출력스트림
				OutputStream outputStream = new FileOutputStream(f);
				
				while((read=is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);	//0부터 읽어들인 만큼 바이트로 변환
				}
				is.close();
				outputStream.close();
				
				ImageIcon img = new ImageIcon(f.getName());
				naverMap.imageLabel.setIcon(img);
				naverMap.resAddress.setText(vo.getRoadAddress());
				naverMap.jibunAddress.setText(vo.getJibunAddress());
				naverMap.resX.setText(vo.getX());
				naverMap.resY.setText(vo.getY());
				
			}else {	//에러 발생
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
