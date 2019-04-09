import java.io.*;
import java.net.*;
import java.time.*;

class TCPServer {
	public static void main(String args[]) throws Exception {
		String clientSentence;
        String message;
        String[] players = new String[2];
        players[0] = "";
        players[1] = "";
        Socket outSocket = new Socket("localhost", 6788);
        DataOutputStream outToClient = new DataOutputStream(outSocket.getOutputStream());
        int count = 0;
		ServerSocket serverSocket = new ServerSocket(6789);
		while (true) {
            while(players[0].equals("") && players[1].equals("")){
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                clientSentence = inFromClient.readLine();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Name")){
                    if(data[1].equals("")){
                        outToClient.writeBytes("Nome invalido");
                    }
                    else{
                        players[count] = data[1];
                        if(count == 0){
                            outToClient.writeBytes("Aguardando segundo jogador");
                            count = count + 1;
                        }
                        else{
                            outToClient.writeBytes("Iniciando jogo");
                        }
                    }
                }
                else{
                    outToClient.writeBytes("Envie seu nome primeiro");
                }
            }
			count = 0;
            // ja tenho os nomes, agora inicio o jogo...
            // TODO resto do programa
			outToClient.writeBytes(message);
			outSocket.close();
		}
	}
}
