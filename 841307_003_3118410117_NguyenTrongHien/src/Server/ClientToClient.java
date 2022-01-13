package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class ClientToClient extends Thread {
	private String clientUUID;
	private String otherClientUUID;
	private BufferedReader clientIn;
	private BufferedWriter otherClientOut;
	
	//Chuyển tin nhắn đến client còn lại
	public void sendToOtherClient(String message) throws IOException {
		otherClientOut.write(message);
		otherClientOut.newLine();
		otherClientOut.flush();
	}
	
	//Nhận tin nhắn từ client
	public String receiveFromclient() throws IOException {
		String clientMessage = "";
		while (true) {
			clientMessage = clientIn.readLine();
			if (clientMessage != null)
				break;
		}
		return clientMessage;
	}
	
	public ClientToClient(String clientuuid, String otherclientuuid) throws IOException {
		this.clientUUID = clientuuid;
		this.otherClientUUID = otherclientuuid;
		clientIn = Server.users.get(clientuuid).getIn();
		otherClientOut = Server.users.get(otherclientuuid).getOut();		
	}

	@Override
	public void run() {
		try {
			while(true) {
				String request = receiveFromclient();
				System.out.println(request);
				StringTokenizer st = new StringTokenizer(request, ":");
				String type = st.nextToken();
				
				if(type.equals("send-message")) {
					String message = st.nextToken();
					sendToOtherClient("receive-message:" + message);
				}
				
				if(type.equals("stop-messaging")) {
					new ServerToClient(clientUUID).start();
					break;
				}
			}
		}
		catch (IOException e) {
			try {
				sendToOtherClient("other-client-exit");
				Server.nicknames.remove(Server.users.get(clientUUID).getNickname());
				Server.users.remove(clientUUID);
				Server.idleUUIDs.add(otherClientUUID);
				
			} catch (IOException e1) {
				
			}
		}
		return;
	}
	
	
}
