package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.UUID;

public class ServerToClient extends Thread {
	private String clientName;
	private String otherClientName;
	private BufferedReader in;
	private BufferedWriter out;
	private User user;
	
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
	
	public ServerToClient(String name, User user) throws IOException {
		this.user = user;
		this.clientName = name;
		in = user.getIn();
		out = user.getOut();
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
					
					if(Server.users.get(usernickname) != null)
						//Gửi phản hồi từ chối
						send("nickname-denied");
					else {
						//Gửi phản hồi đồng ý
						send("nickname-accepted:" + usernickname);
						
						clientName = usernickname;
						//Thêm vào danh sách phòng chờ
						Server.idleNicknames.add(clientName);
						//Thêm vào danh sách các user hiện tại
						Server.users.put(usernickname, user);
					}
				}
				
				if(requestType.equals("searching")) {
					if(Server.users.size() == 1)
						//Gửi phản hồi không tìm thấy do chỉ có duy nhất 1 client
						send("user-not-found");
					else {
						boolean found = false;
						//Tìm trong danh sách có phòng chờ các nickname chưa bị chặn 
						for(String idleNickname : Server.idleNicknames) 
						{
							if(Server.users.get(clientName).getBlockedNickname().indexOf(idleNickname) < 0
									&& Server.users.get(idleNickname).getBlockedNickname().indexOf(clientName) < 0
									&& !clientName.equals(idleNickname)) 
							{
								//Gửi phản hồi tìm thấy nickname
								send("found-user:" + idleNickname);
								found = true;
								break;
							}
						}
						//Gửi phản hồi không tìm thấy do đã chặn hết các user trong phòng chờ
						if(!found)
							send("user-not-found");
					}
				}
				
				if(requestType.equals("start-messaging")) {
					otherClientName = st.nextToken();
					
					//Cả 2 client chuyển sang thread ClientToClient
					new ClientToClient(clientName, otherClientName).start();
					break;
				}
				
				if(requestType.equals("accept")) {
					otherClientName = st.nextToken();
					
					//Gửi thông báo kết nối đến client
					send("connected:" + otherClientName);
					
					//Gửi thông báo kết nối đến client thứ 2
					Server.sendTo(otherClientName,"connected:" + clientName);
					
					//Xóa 2 client khỏi danh sách chờ
					Server.idleNicknames.remove(clientName);
					Server.idleNicknames.remove(otherClientName);
					
				}
				
				if(requestType.equals("decline")) {
					otherClientName = st.nextToken();
					
					//thêm nickname client mà server đề nghị vào danh sách chặn của client hiện tại
					User userUpdate1 = Server.users.get(clientName);
					userUpdate1.block(otherClientName);
					Server.users.put(clientName, userUpdate1);
					
					//thêm nickname client hiện tại vào danh sách chặn của client mà server đề nghị 
					User userUpdate2 = Server.users.get(otherClientName);
					userUpdate2.block(clientName);
					Server.users.put(otherClientName, userUpdate2);
					
					otherClientName = null;
				}
				
			}
		} catch (IOException e) {
			if(clientName != null) {
				Server.idleNicknames.remove(clientName);
				Server.users.remove(clientName);
			}
			
			try {
				if(user.getIn() != null)
					user.getIn().close();
				if(user.getOut() != null)
					user.getOut().close();
				if(user.getSocket() != null)
					user.getSocket().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return;
	}	
	
	
	
}
