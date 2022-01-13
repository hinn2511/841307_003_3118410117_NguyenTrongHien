package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {
	public static Socket socket = null;
	public static BufferedWriter out = null;
	public static BufferedReader in = null;
	public static String clientNickname = "";
	public static String otherClientNickname = "";
	public static String otherClientUUID = "";
	
	//Gửi yêu cầu đến Server
	public static void sendRequest(String request) throws IOException {
		System.out.println(request);
		out.write(request);
		out.newLine();
		out.flush();
	}

	//Nhận phản hồi từ Server 
	public static String receiveResponse() throws IOException {
		String messageReceive = "";
		while (true) {
			messageReceive = in.readLine();
			if (messageReceive != null)
				break;
		}
		return messageReceive;
	}
	
	//Hiển thị thông báo
	public static void alert(String str) {
		JOptionPane.showMessageDialog(null, str);
	}
	
	//Hiển thị xác nhận
	public static boolean confirm(String str, String tilte) {
		int dialogResult = JOptionPane.showConfirmDialog(null, str, tilte, JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
	

	public static void main(String[] args) throws IOException {
		try {
			socket = new Socket("127.0.0.1", 1234);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			ClientToServer client = new ClientToServer();
		} 
		catch (IOException e) {
		}

	}



}
