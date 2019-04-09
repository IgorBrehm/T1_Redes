import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
	public static void main(String args[]) throws Exception {
	  	Scanner input = new Scanner (System.in);
        String serverSentence;
		String name;
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        
		boolean flag = false;
		while (flag == false) {
            System.out.println("Digite seu nome:");
            name = input.nextLine();
            outToServer.writeBytes("Name-"+name);
            ServerSocket serverSocket = new ServerSocket(6788);
            Socket connectionSocket = serverSocket.accept();
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			serverSentence = inFromServer.readLine();
            if(serverSentence.equals("Iniciando jogo")){
                flag = true;
            }
            else if(serverSentence.equals("Aguardando segundo jogador")){
                System.out.println("Aguardando o seu adversario chegar!");
                while(true){
                    serverSentence = inFromServer.readLine();
                    if(serverSentence.equals("Iniciando jogo")){
                        flag = true;
                        break;
                    }
                }
            }
            else{
                System.out.println("Nome Invalido! Tente novamente.");
            }
	    }
        
        flag = false;
        while(flag == false){
            flag = false;
            // TODO execucao das rodadas
        }
        clientSocket.close();
	    input.close();
	    System.exit(0);
	}
}
