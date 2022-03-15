package kr.soldesk;

public class AddressVO {

	private String roadAddress;
	private String jibunAddress;
	private String x;
	private String y;
	
	
	//getters
	public String getRoadAddress() {
		return roadAddress;
	}
	public String getJibunAddress() {
		return jibunAddress;
	}
	public String getX() {
		return x;
	}
	public String getY() {
		return y;
	}
	//setters
	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}
	public void setJibunAddress(String jibunAddress) {
		this.jibunAddress = jibunAddress;
	}
	public void setX(String x) {
		this.x = x;
	}
	public void setY(String y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "AddressVO [roadAddress=" + roadAddress + ", jibunAddress="+ jibunAddress + ", x="+ x + ", y=" + y + "]";
	}
	
	
}
