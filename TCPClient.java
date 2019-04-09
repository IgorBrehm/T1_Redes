import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
	public static void main(String args[]) throws Exception {
	  	Scanner input = new Scanner (System.in);
        ServerSocket welcomeSocket = new ServerSocket(6789);
        String clientSentence;
		String message;
		String name;
		System.out.println("Digite seu nome:");
        name = input.nextLine();
		boolean flag = false;
		while (flag == false) {
            Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
            
			Socket clientSocket = new Socket("localhost", 6789);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			outToServer.writeBytes(message);
			clientSocket.close();
	    }
	    input.close();
	    System.exit(0);
	}
}
