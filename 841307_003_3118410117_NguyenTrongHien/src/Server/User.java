package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

public class User {
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private ArrayList<String> blockedNickname;

	public User(Socket socket, BufferedReader in, BufferedWriter out, ArrayList<String> blockedNickname) {
		super();
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.blockedNickname = blockedNickname;
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

	public ArrayList<String> getBlockedNickname() {
		return blockedNickname;
	}

	public void setBlockedNickname(ArrayList<String> blockedNickname) {
		this.blockedNickname = blockedNickname;
	}
	
	public void block(String uuid) {
		blockedNickname.add(uuid);
	}
}
