import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
	public static void main(String args[]) throws Exception {
	  	Scanner input = new Scanner (System.in);
        ServerSocket serverSocket = new ServerSocket(6788);
        String serverSentence;
		String message;
		String name;
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		boolean flag = false;
		while (flag == false) {
            System.out.println("Digite seu nome:");
            name = input.nextLine();
            outToServer.writeBytes("Name-"+name);
			serverSentence = inFromServer.readLine();
            if(serverSentence.equals("Iniciando jogo")){
                // comeca o jogo
            }
            else if(serverSentence.equals("Aguardando segundo jogador")){
                // congela e fica esperando
            }
            else{
                // nome invalido, pede pra mandar de novo
            }
	    }
        
        // joga o jogo    
        clientSocket.close();
	    input.close();
	    System.exit(0);
	}
}
