package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

public class User {
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private String nickname;
	private ArrayList<String> blockedUUID;
	

	public User(Socket socket, BufferedReader in, BufferedWriter out, String nickname, ArrayList<String> blockedUUID) {
		super();
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.nickname = nickname;
		this.blockedUUID = blockedUUID;
	}

	public BufferedWriter getOut() {
		return out;
	}

	public void setOut(BufferedWriter out) {
		this.out = out;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
		
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public ArrayList<String> getBlockedUUID() {
		return blockedUUID;
	}

	public void setBlockedUUID(ArrayList<String> blockedUUID) {
		this.blockedUUID = blockedUUID;
	}
	
	public void block(String uuid) {
		blockedUUID.add(uuid);
	}
}
