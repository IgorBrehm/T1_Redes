import java.net.*;
import java.io.*;

public class Server extends Thread {
   private ServerSocket serverSocket;
   
   public Server(int port) throws IOException {
      serverSocket = new ServerSocket(port);
   }

   public void run() {
      while(true) {
         try {
            Socket server1 = serverSocket.accept();
            Socket server2 = serverSocket.accept();
            String clientSentence; // String usada para as mensagens enviadas pelo cliente
            String[] players = new String[2]; // lista de nomes dos jogadores
            players[0] = ""; // nome do jogador 1
            players[1] = ""; // nome do jogador 2
            int[] scores = new int[]{0,0}; // indice no tabuleiro onde esta cada jogador
            DataInputStream in = new DataInputStream(server1.getInputStream());
            DataOutputStream out = new DataOutputStream(server1.getOutputStream());
            DataInputStream in2 = new DataInputStream(server2.getInputStream());
            DataOutputStream out2 = new DataOutputStream(server2.getOutputStream());
            boolean end_game = false;
            int[] board = new int[10];

            for(int i = 0; i < board.length; i++){
                if(i % 2 == 0){
                    board[i] = 1;
                }
                else{
                    board[i] = 2;
                }
            }
            
            while(players[0].equals("")){
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Name")){
                    if(data[1].equals("")){
                        out.writeUTF("Nome invalido");
                    }
                    else{
                        players[0] = data[1];
                        out.writeUTF("Aguardando segundo jogador");
                    }
                }
                else{
                    out.writeUTF("Envie seu nome primeiro");
                }
            }
            while(players[1].equals("")){
                clientSentence = in2.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Name")){
                    if(data[1].equals("")){
                        out2.writeUTF("Nome invalido");
                    }
                    else{
                        players[1] = data[1];
                    }
                }
                else{
                    out2.writeUTF("Envie seu nome primeiro");
                }
            }
            out.writeUTF("Iniciando jogo");
            out2.writeUTF("Iniciando jogo");
            while(end_game == false){
                out.writeUTF("Sua vez");
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                int roll = Integer.parseInt(data[1]);
                if(data[0].equals("Roll")){
                    if(roll == 1){
                        scores[0] = scores[0] + 2;
                    }
                    else{
                        scores[0] = scores[0] + 1;
                    }
                }
                out.writeUTF("Esperando o adversario jogar");
                out2.writeUTF("Sua vez");
                clientSentence = in2.readUTF();
                data = clientSentence.split("-", 2);
                roll = Integer.parseInt(data[1]);
                if(data[0].equals("Roll")){
                    if(roll == 1){
                        scores[1] = scores[1] + 2;
                    }
                    else{
                        scores[1] = scores[1] + 1;
                    }
                }
                if(scores[0] >= 10 || scores[1] >= 10){
                    end_game = true;
                    break;
                }
                out2.writeUTF("Esperando o adversario jogar");
            }
            System.out.println("WE DONE!");
            server1.close();
            server2.close();
         } catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public static void main(String [] args) {
      int port = Integer.parseInt(args[0]);
      try {
         Thread t = new Server(port);
         t.start();
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
   }
}
