package course.oop.objects;

import java.io.Serializable;

public class Player implements Serializable {
	
	private String username;
	private String marker;
	private int winCount;
	
	public Player(String username, String marker) {
		this.username = username;
		this.marker = marker;
		this.winCount = 0;
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
	public void playerWin(){
		winCount++;
	}
	public int getWinCount(){
		return this.winCount;
	}
}
