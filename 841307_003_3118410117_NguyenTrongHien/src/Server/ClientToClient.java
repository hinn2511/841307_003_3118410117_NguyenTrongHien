package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class ClientToClient extends Thread {
	private String clientName;
	private String otherClientName;
	private BufferedReader clientIn;
	private BufferedWriter otherClientOut;

	// Chuyển tin nhắn đến client còn lại
	public void sendToOtherClient(String message) throws IOException {
		otherClientOut.write(message);
		otherClientOut.newLine();
		otherClientOut.flush();
	}

	// Nhận tin nhắn từ client
	public String receiveFromclient() throws IOException {
		String clientMessage = "";
		while (true) {
			clientMessage = clientIn.readLine();
			if (clientMessage != null)
				break;
		}
		return clientMessage;
	}

	public ClientToClient(String clientName, String otherClientName) throws IOException {
		this.clientName = clientName;
		this.otherClientName = otherClientName;
		clientIn = Server.users.get(clientName).getIn();
		otherClientOut = Server.users.get(otherClientName).getOut();
	}

	@Override
	public void run() {
		try {
			while (true) {
				String request = receiveFromclient();
				StringTokenizer st = new StringTokenizer(request, ":");
				String requestType = st.nextToken();

				if (requestType.equals("send-message")) {
					String message = st.nextToken();
					sendToOtherClient("receive-message:" + message);
				}

				if (requestType.equals("stop-messaging")) {
					new ServerToClient(clientName, Server.users.get(clientName)).start();
					break;
				}
			}
		} catch (IOException e) {
			try {
				sendToOtherClient("other-client-exit");
				Server.idleNicknames.add(otherClientName);
				Thread.sleep(1000);
				if (Server.users.get(clientName).getIn() != null)
					Server.users.get(clientName).getIn().close();
				if (Server.users.get(clientName).getOut() != null)
					Server.users.get(clientName).getOut().close();
				if (Server.users.get(clientName).getSocket() != null)
					Server.users.get(clientName).getSocket().close();
				Server.users.remove(clientName);
			} catch (IOException | InterruptedException e1) {

			}
		}
		return;
	}

}
