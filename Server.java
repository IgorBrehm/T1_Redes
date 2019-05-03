import java.net.*;
import java.io.*;
import java.util.Random;

public class Server extends Thread {
   private ServerSocket serverSocket;
   
   public Server(int port) throws IOException {
      serverSocket = new ServerSocket(port);
   }

   public void run() {
      while(true) {
         try {
            Random r = new Random();
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
                if(i % 3 == 0){
                    board[i] = 0; //posicao onde nada acontece
                }
                else{
                    board[i] = r.nextInt(6); //posicao onde volta uma casa ou anda uma casa extra
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
            int roll = 0;
            while(end_game == false){
                out.writeUTF("Sua vez");
                out2.writeUTF("Esperando o adversario jogar");
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Roll")){
                    roll = Integer.parseInt(data[1]);
                    System.out.println("Jogador "+players[0]+" tirou "+roll+" nos dados!");
                    scores[0] = scores[0] + roll;
                    out2.writeUTF("Adversario avancou " + roll + " casas!");
                    if(scores[0] >= 10 || scores[1] >= 10){
                        end_game = true;
                        if(scores[0] >= 10){
                            System.out.println("Jogador "+players[0]+" venceu a corrida!");
                            out.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                            out2.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                            out.writeUTF("Fim de jogo");
                            out2.writeUTF("Fim de jogo");
                        }
                        else{
                            System.out.println("Jogador "+players[1]+" venceu a corrida!");
                            out.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                            out2.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                            out.writeUTF("Fim de jogo");
                            out2.writeUTF("Fim de jogo");
                        }
                        break;
                    }
                    switch(board[scores[0]]) {
                        case 0: 
                            break;
                        case 1:
                            out.writeUTF("Voce passou sobre oleo, e ira voltar duas casas");
                            if (scores[0] >= 2) {
                                scores[0] -= 2;
                            }
                            else scores[0] = 0;

                            out2.writeUTF("Voce deu sorte, seu adversario passou sobre oleo e retornou duas casas");
                            break;

                        case 2:
                            out.writeUTF("Voce derrapou na curva e ira voltar uma casa");
                            if (scores[0] >= 1) {
                                scores[0] -= 1;
                            }
                            else scores[0] = 0;
                            out2.writeUTF("Seu adversario errou a curva e retornou uma casa");
                            break;

                        case 3:
                            out.writeUTF("Voce acabou de passar por um turbo, e ira andar mais duas casas");
                            scores[0] += 2;

                            out2.writeUTF("Seu adversario pegou um turbo e acelerou mais duas casas a frente");
                            break;

                        case 4:
                            out.writeUTF("Voce acabou de passar por um buraco, e ira retornar mais duas casas");
                            if (scores[0] >= 2) {
                                scores[0] -= 2;
                            }
                            else scores[0] = 0;

                            out2.writeUTF("Seu adversario passou por um buraco e voltou duas casas");
                            break;

                        case 5:
                            out.writeUTF("Voce acabou de realizar uma curva perfeita, e ira andar mais uma casa");
                            scores[0] += 1;

                            out2.writeUTF("Seu adversario se deu bem na curva, e andou mais uma casa");
                            break;
                            
                        case 6:
                            int muitaSorte = r.nextInt(2);
                            if (muitaSorte == 1) {
                                out.writeUTF("Uau, que habilidade!!! Voce encontrou um atalho e ira pular 4 casas");
                                scores[0] += 4;

                                out2.writeUTF("De repente seu adversario pulou 4 casas, que loucura!!!");
                                break;
                            }
                            else break;

                        default:
                            break;
                    }
                }
                else{
                    System.out.println(clientSentence);
                }
                out.writeUTF("Esperando o adversario jogar");
                out2.writeUTF("Sua vez");
                clientSentence = in2.readUTF();
                data = clientSentence.split("-", 2);
                if(data[0].equals("Roll")){
                    roll = Integer.parseInt(data[1]);
                    System.out.println("Jogador "+players[1]+" tirou "+roll+" nos dados!");
                    scores[0] = scores[0] + roll;
                    out2.writeUTF("Adversario avancou " + roll + " casas!");
                    if(scores[0] >= 10 || scores[1] >= 10){
                        end_game = true;
                        if(scores[0] >= 10){
                            System.out.println("Jogador "+players[0]+" venceu a corrida!");
                            out.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                            out2.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                            out.writeUTF("Fim de jogo");
                            out2.writeUTF("Fim de jogo");
                        }
                        else{
                            System.out.println("Jogador "+players[1]+" venceu a corrida!");
                            out.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                            out2.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                            out.writeUTF("Fim de jogo");
                            out2.writeUTF("Fim de jogo");
                        }
                        break;
                    }
                    switch(board[scores[0]]) {
                        case 0: 
                            break;
                        case 1:
                            out2.writeUTF("Voce passou sobre oleo, e ira voltar duas casas");
                            if (scores[0] >= 2) {
                                scores[0] -= 2;
                            }
                            else scores[0] = 0;

                            out.writeUTF("Voce deu sorte, seu adversario passou sobre oleo e retornou duas casas");
                            break;

                        case 2:
                            out2.writeUTF("Voce derrapou na curva e ira voltar uma casa");
                            if (scores[0] >= 1) {
                                scores[0] -= 1;
                            }
                            else scores[0] = 0;
                            out.writeUTF("Seu adversario errou a curva e retornou uma casa");
                            break;

                        case 3:
                            out2.writeUTF("Voce acabou de passar por um turbo, e ira andar mais duas casas");
                            scores[0] += 2;

                            out.writeUTF("Seu adversario pegou um turbo e acelerou mais duas casas a frente");
                            break;

                        case 4:
                            out2.writeUTF("Voce acabou de passar por um buraco, e ira retornar mais duas casas");
                            if (scores[0] >= 2) {
                                scores[0] -= 2;
                            }
                            else scores[0] = 0;

                            out.writeUTF("Seu adversario passou por um buraco e voltou duas casas");
                            break;

                        case 5:
                            out2.writeUTF("Voce acabou de realizar uma curva perfeita, e ira andar mais uma casa");
                            scores[0] += 1;

                            out.writeUTF("Seu adversario se deu bem na curva, e andou mais uma casa");
                            break;
                            
                        case 6:
                            int muitaSorte = r.nextInt(2);
                            if (muitaSorte == 1) {
                                out2.writeUTF("Uau, que habilidade!!! Voce encontrou um atalho e ira pular 4 casas");
                                scores[0] += 4;

                                out.writeUTF("De repente seu adversario pulou 4 casas, que loucura!!!");
                                break;
                            }
                            else break;

                        default:
                            break;
                    }
                }
                else{
                    System.out.println(clientSentence);
                }
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
