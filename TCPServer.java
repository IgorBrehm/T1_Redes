import java.io.*;
import java.net.*;
import java.time.*;

class TCPServer {
	public static void main(String args[]) throws Exception {
		String clientSentence;
        String[] players = new String[2];
        players[0] = "";
        players[1] = "";
        int[] scores = new int[]{0,0};
        int count = 0;
		ServerSocket serverSocket = new ServerSocket(6789);
        Socket connectionSocket = serverSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        
        while(players[0].equals("") && players[1].equals("")){
            clientSentence = inFromClient.readLine();
            String[] data = clientSentence.split("-", 2);
            Socket outSocket = new Socket("localhost", 6788);
            DataOutputStream outToClient = new DataOutputStream(outSocket.getOutputStream());
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
            outSocket.close();
        }
        count = 0;
        while(count < 3){
            clientSentence = inFromClient.readLine();
            // TODO execucao das rodadas
            count = count +1;
        }
	}
}
