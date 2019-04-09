import java.io.*;
import java.net.*;
import java.time.*;

class TCPServer {
	public static void main(String args[]) throws Exception {
		String clientSentence;
        String message;
        String[] players = new String[2];
		ServerSocket welcomeSocket = new ServerSocket(6789);
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
            
            Socket clientSocket = new Socket("localhost", 6789);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			outToServer.writeBytes(message);
			clientSocket.close();
		}
	}
}
