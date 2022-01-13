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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	public static int port = 1234;
	public static ServerSocket server = null;
	
	public static HashMap<String, User> users = new HashMap<>();
	
	public static ArrayList<String> idleUUIDs = new ArrayList<>();
	
	public static ArrayList<String> nicknames = new ArrayList<>();
	
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
				UUID clientUUID = UUID.randomUUID();
				
				User user = new User(socket, in, out, "", new ArrayList<String>());
				users.put(clientUUID.toString(), user);
				new ServerToClient(clientUUID.toString()).start();
				
				System.out.println("Connect to " + socket + " with UUID: " + clientUUID);
				
			}
		} 
		catch (IOException e) {
		}
		finally {
			if(server != null)
				server.close();
		}
	}
	
	//Gửi thông tin đến client theo mã định danh UUID 
	public static void sendTo(String uuid, String message) throws IOException {
		clientOut = users.get(uuid).getOut();
		clientOut.write(message);
		clientOut.newLine();
		clientOut.flush();
	}
	
	//Cập nhật nickname của client theo UUID
	public static void updateUserNickname(String uuid, String newNickname) {
		User userUpdate = users.get(uuid);
		userUpdate.setNickname(newNickname);
		Server.users.put(uuid, userUpdate);
	}
	
}
