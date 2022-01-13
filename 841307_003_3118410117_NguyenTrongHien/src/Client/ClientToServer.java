package Client;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.text.BadLocationException;

import Client.GUI.Chat;
import Client.GUI.Nickname;
import Client.GUI.WaitingRoom;

public class ClientToServer {
	Nickname nicknameFrame;
	WaitingRoom waitingRoomFrame;
	Chat chatFrame;
	
	public ClientToServer() {
		nicknameFrame = new Nickname();
		nicknameFrame.setVisible(true);
		
		try {
			while (true) {
				String response = Client.receiveResponse();
				
				StringTokenizer st = new StringTokenizer(response, ":");
				String type = st.nextToken();
				
				if (type.equals("nickname-denied")) {
					Client.alert("Nickname đã tồn tại.");
				}

				if (type.equals("nickname-accepted")) {
					Client.clientNickname = st.nextToken();

					nicknameFrame.setVisible(false);
					waitingRoomFrame = new WaitingRoom(Client.clientNickname);
					waitingRoomFrame.setVisible(true);
				}
				
				if (type.equals("found-user")) {
					
					String otherNickname = st.nextToken();
					String otherUUID = st.nextToken();

					boolean accept = Client.confirm("Bạn có muốn chat với " + otherNickname + " không?",
							"Tham gia phòng chat");
					if (accept) {
						Client.sendRequest("accept:" + otherUUID);
					} else {
						Client.sendRequest("decline:" + otherUUID);
					}

				}

				if (type.equals("user-not-found")) {
					Client.alert("Không tìm thấy người dùng khác. Vui lòng chờ.");
				}

				if (type.equals("connected")) {
					String othernameString = st.nextToken();
					String otherUUID = st.nextToken();
					
					Client.otherClientNickname = othernameString;
					Client.otherClientUUID = otherUUID;

					waitingRoomFrame.setVisible(false);
					chatFrame = new Chat(Client.clientNickname, Client.otherClientNickname);
					chatFrame.setVisible(true);
					
					Client.sendRequest("start-messaging:" + Client.otherClientUUID);
				}

				if (type.equals("receive-message")) {
					
					String message = st.nextToken();
					
					try {
						Chat.showOtherMessage(message);
					} catch (BadLocationException e) {
						Client.alert("Đã có lỗi xảy ra.");
					}
				}

				if (type.equals("other-client-exit")) {
					Client.sendRequest("stop-messaging");
					chatFrame.setVisible(false);
					waitingRoomFrame.setVisible(true);
					Client.alert(Client.otherClientNickname + " đã thoát khỏi phòng chat.");
					Client.otherClientNickname = "";
					Client.otherClientUUID = "";
				}

			}
		} catch (IOException e) {
			Client.alert("Đã có lỗi xảy ra.");
		}
	}

}
