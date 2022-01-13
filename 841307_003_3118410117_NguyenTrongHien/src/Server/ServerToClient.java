package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.UUID;

public class ServerToClient extends Thread {
	private String clientUUID;
	private String otherClientUUID;
	private BufferedReader in;
	private BufferedWriter out;
	
	//Nhận dữ liệu từ client
	public String receive() throws IOException {
		String clientMessage = "";
		while (true) {
			clientMessage = in.readLine();
			if (clientMessage != null)
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return clientMessage;
	}
	
	//Gửi dữ liệu đến client
	public void send(String message) throws IOException {
		out.write(message);
		out.newLine();
		out.flush();
	}
	
	public ServerToClient(String uuid) throws IOException {
		this.clientUUID = uuid;
		in = Server.users.get(uuid).getIn();
		out = Server.users.get(uuid).getOut();
	}

	@Override
	public void run() {
		
		try {
			while (true) {
				String request = receive();
				StringTokenizer st = new StringTokenizer(request, ":");
				String requestType = st.nextToken();
				
				if(requestType.equals("nickname-choosen")) {
					
					String usernickname = st.nextToken();
					
					if(Server.nicknames.indexOf(usernickname) > -1)
						send("nickname-denied");
					else {
						send("nickname-accepted:" + usernickname);
						Server.updateUserNickname(clientUUID, usernickname);
						Server.nicknames.add(usernickname);
						Server.idleUUIDs.add(clientUUID);
						System.out.println(Server.users.get(clientUUID).getNickname() + " " + clientUUID);
					}
				}
				
				if(requestType.equals("searching")) {
					if(Server.users.size() == 1)
						send("user-not-found");
					else {
						boolean found = false;
						for(String idleUuid : Server.idleUUIDs) 
						{
							if(Server.users.get(clientUUID).getBlockedUUID().indexOf(idleUuid) < 0
									&& Server.users.get(idleUuid).getBlockedUUID().indexOf(clientUUID) < 0
									&& !clientUUID.equals(idleUuid)) 
							{
								send("found-user:" + Server.users.get(idleUuid).getNickname() + ":" + idleUuid);
								found = true;
								break;
							}
						}
						if(!found)
							send("user-not-found");
					}
				}
				
				if(requestType.equals("start-messaging")) {
					otherClientUUID = st.nextToken();
					new ClientToClient(clientUUID, otherClientUUID).start();
					break;
				}
				
				if(requestType.equals("accept")) {
					otherClientUUID = st.nextToken();
					
					send("connected:" + Server.users.get(otherClientUUID).getNickname() + ":" + otherClientUUID);
					Server.sendTo(otherClientUUID,"connected:" + Server.users.get(clientUUID).getNickname() + ":" + clientUUID);
					Server.idleUUIDs.remove(clientUUID);
					Server.idleUUIDs.remove(otherClientUUID);
					
				}
				
				if(requestType.equals("decline")) {
					otherClientUUID = st.nextToken();
					
					User userUpdate1 = Server.users.get(clientUUID);
					userUpdate1.block(otherClientUUID);
					Server.users.put(clientUUID, userUpdate1);
					
					User userUpdate2 = Server.users.get(otherClientUUID);
					userUpdate2.block(clientUUID);
					Server.users.put(otherClientUUID, userUpdate2);
					
					otherClientUUID = null;
				}
				
			}
		} catch (IOException e) {
			Server.nicknames.remove(Server.users.get(clientUUID).getNickname());
			Server.users.remove(clientUUID);
			Server.idleUUIDs.remove(clientUUID);
		}
		return;
	}	
	
	
	
}
