package course.oop.objects;

import java.io.Serializable;

public class Player implements Serializable {
	
	private String username;
	private String marker;
	
	public Player(String username, String marker) {
		this.username = username;
		this.marker = marker;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getMarker() {
		return marker;
	}
	public void setMarker(String marker) {
		this.marker = marker;
	}
}
