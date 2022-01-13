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
				String responseType = st.nextToken();
				
				if (responseType.equals("nickname-denied")) {
					Client.alert("Nickname đã tồn tại.");
				}

				if (responseType.equals("nickname-accepted")) {
					Client.clientNickname = st.nextToken();

					nicknameFrame.setVisible(false);
					waitingRoomFrame = new WaitingRoom(Client.clientNickname);
					waitingRoomFrame.setVisible(true);
				}
				
				if (responseType.equals("found-user")) {
					
					String otherNickname = st.nextToken();

					boolean accept = Client.confirm("Bạn có muốn chat với " + otherNickname + " không?",
							"Tham gia phòng chat");
					if (accept) {
						Client.sendRequest("accept:" + otherNickname);
					} else {
						Client.sendRequest("decline:" + otherNickname);
					}

				}

				if (responseType.equals("user-not-found")) {
					Client.alert("Không tìm thấy người dùng khác. Vui lòng chờ.");
				}

				if (responseType.equals("connected")) {
					
					Client.otherClientNickname = st.nextToken();

					waitingRoomFrame.setVisible(false);
					chatFrame = new Chat(Client.clientNickname, Client.otherClientNickname);
					chatFrame.setVisible(true);
					
					Client.sendRequest("start-messaging:" + Client.otherClientNickname);
				}

				if (responseType.equals("receive-message")) {
					
					String message = st.nextToken();
					
					try {
						Chat.showOtherMessage(message);
					} catch (BadLocationException e) {
						Client.alert("Đã có lỗi xảy ra.");
					}
				}

				if (responseType.equals("other-client-exit")) {
					Client.sendRequest("stop-messaging");
					
					chatFrame.setVisible(false);
					waitingRoomFrame.setVisible(true);
					
					Client.alert(Client.otherClientNickname + " đã thoát khỏi phòng chat.");
					
					Client.otherClientNickname = "";
				}

			}
		} catch (IOException e) {
			Client.alert("Đã có lỗi xảy ra.");
		}
	}

}
