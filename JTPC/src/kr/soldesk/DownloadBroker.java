package kr.soldesk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadBroker implements Runnable{

	private String address;
	private String fileName;
	
	
	
	public DownloadBroker(String address, String fileName) {
		super();
		this.address = address;
		this.fileName = fileName;
	}



	@Override
	public void run() {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			//쓰기 스트림
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			URL url = new URL(address);
			
			InputStream is = url.openStream();
			
			//속도가 빠름
			BufferedInputStream bis = new BufferedInputStream(is);
			
			int data;
			while((data=bis.read()) != -1) {
				bos.write(data);
			}
			bos.close();
			bis.close();
			System.err.println("download complete...!!");
			System.err.println("File Name : " + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
