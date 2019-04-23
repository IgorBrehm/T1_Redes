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
            int count = 0;
            DataInputStream in = new DataInputStream(server1.getInputStream());
            DataOutputStream out = new DataOutputStream(server1.getOutputStream());
            DataInputStream in2 = new DataInputStream(server2.getInputStream());
            DataOutputStream out2 = new DataOutputStream(server2.getOutputStream());
            
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
            while(count < 3){
                clientSentence = in.readUTF();
                System.out.println(clientSentence);
                clientSentence = in2.readUTF();
                System.out.println(clientSentence);
                // TODO execucao das rodadas
                count = count +1;
            }
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
