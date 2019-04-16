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
            Socket server = serverSocket.accept();
            
            String clientSentence; // String usada para as mensagens enviadas pelo cliente
            String[] players = new String[2]; // lista de nomes dos jogadores
            players[0] = ""; // nome do jogador 1
            players[1] = ""; // nome do jogador 2
            int[] scores = new int[]{0,0}; // indice no tabuleiro onde esta cada jogador
            int count = 0; // numero de jogadores jogando no momento
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            
            while(players[0].equals("") && players[1].equals("")){
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Name")){
                    if(data[1].equals("")){
                        out.writeUTF("Nome invalido");
                    }
                    else{
                        players[count] = data[1];
                        if(count == 0){
                            out.writeUTF("Aguardando segundo jogador");
                            count = count + 1;
                        }
                        else{
                            out.writeUTF("Iniciando jogo");
                        }
                    }
                }
                else{
                    out.writeUTF("Envie seu nome primeiro");
                }
            }
            count = 0;
            while(count < 3){
                clientSentence = in.readUTF();
                System.out.println(clientSentence);
                // TODO execucao das rodadas
                count = count +1;
            }
            server.close();
            
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
