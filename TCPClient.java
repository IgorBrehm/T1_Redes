import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
	public static void main(String args[]) throws Exception {
	  	Scanner input = new Scanner (System.in);
        String serverSentence;
		String name;
        int port;
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        
        System.out.println("Especifique a porta que deseja usar:");
        port = input.nextInt();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Digite seu nome:");
        name = input.nextLine();
        outToServer.writeBytes("Name-"+name);
        outToServer.flush();
        Socket connectionSocket = serverSocket.accept();
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        
		boolean flag = false;
		while (flag == false) {
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
                System.out.println("Digite seu nome:");
                name = input.nextLine();
                outToServer.writeBytes("Name-"+name);
                outToServer.flush();
            }
	    }
        
        flag = false;
        while(flag == false){
            System.out.println("Digite um numero:");
            String test = input.nextLine();
            outToServer.writeBytes(test);
            outToServer.flush();
            // TODO execucao das rodadas
        }
        clientSocket.close();
	    input.close();
	    System.exit(0);
	}
}
