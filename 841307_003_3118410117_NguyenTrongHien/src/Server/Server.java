package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
	public static int port = 1234;
	public static ServerSocket server = null;
	
	public static HashMap<String, User> users = new HashMap<>();
	public static ArrayList<String> idleNicknames = new ArrayList<>();
	private static BufferedWriter clientOut;
	
	public static void main(String args[]) throws IOException {
		try {
			
			server = new ServerSocket(port);
			System.out.println("Server started!");
			System.out.println("Waiting for a Client...");
			
			while(true)
			{
				Socket socket = server.accept();

				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	
				User user = new User(socket, in, out, new ArrayList<String>());
				new ServerToClient("", user).start();

			}
		} 
		catch (IOException e) {
		}
		finally {
			if(server != null)
				server.close();
		}
	}
	
	public static void sendTo(String name, String message) throws IOException {
		clientOut = users.get(name).getOut();
		clientOut.write(message);
		clientOut.newLine();
		clientOut.flush();
	}
}
