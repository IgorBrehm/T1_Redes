import java.net.*;
import java.io.*;
import java.util.Random;

/*
* Jogo de corrida usando arquitetura cliente-servidor.
* 
* @author Guilherme Picolli, Fernando Maioli, Igor Brehm
*/

public class Server extends Thread {
    private ServerSocket serverSocket;
    private int[] scores;
    private int[] board;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void rodadas(String[] players,DataInputStream in, DataInputStream in2, DataOutputStream out, DataOutputStream out2){
        try{
            int roll = 0;
            boolean end_game = false;
            String clientSentence = "";
            while(end_game == false){
                out.writeUTF("Sua vez");
                out2.writeUTF("Aguardando adversario");
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Roll")){
                    roll = Integer.parseInt(data[1]);
                    System.out.println("Jogador "+players[0]+" tirou "+roll+" nos dados!");
                    scores[0] = scores[0] + roll;
                    out2.writeUTF("Adversario avancou " + roll + " casas!");
                    if(scores[0] >= 10){
                        end_game = true;
                        System.out.println("Jogador "+players[0]+" venceu a corrida!");
                        out.writeUTF("Fim de jogo");
                        out2.writeUTF("Fim de jogo");
                        out.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                        out2.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                        break;
                    }
                    switchCasos(board[scores[0]], out, out2);

                    out.writeUTF("-------------------------------");
                    out.writeUTF("Voce esta na casa: " + scores[0] );
                    out.writeUTF("-------------------------------");
                    out.writeUTF("Seu adversario esta na casa: " + scores[1] );
                    out.writeUTF("-------------------------------");

                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Voce esta na casa: " + scores[1] );
                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Seu adversario esta na casa: " + scores[0] );
                    out2.writeUTF("-------------------------------");

                }
                else if(data[0].equals("Exit")){
                    out2.writeUTF("Desistencia");
                    System.out.println("Esperando próximos jogadores");
                    run();
                    return;
                }
                else{
                    System.out.println(clientSentence);
                }
                out.writeUTF("Aguardando adversario");
                out2.writeUTF("Sua vez");
                clientSentence = in2.readUTF();
                data = clientSentence.split("-", 2);
                if(data[0].equals("Roll")){
                    roll = Integer.parseInt(data[1]);
                    System.out.println("Jogador "+players[1]+" tirou "+roll+" nos dados!");
                    scores[1] = scores[1] + roll;
                    out.writeUTF("Adversario avancou " + roll + " casas!");
                    if(scores[1] >= 10){
                        end_game = true;
                        System.out.println("Jogador "+players[1]+" venceu a corrida!");
                        out.writeUTF("Fim de jogo");
                        out2.writeUTF("Fim de jogo");
                        out.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                        out2.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                        break;
                    }
                    switchCasos(board[scores[1]], out2, out);

                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Voce esta na casa: " + scores[1] );
                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Seu adversario esta na casa: " + scores[1] );
                    out2.writeUTF("-------------------------------");

                    out.writeUTF("-------------------------------");
                    out.writeUTF("Voce esta na casa: " + scores[1] );
                    out.writeUTF("-------------------------------");
                    out.writeUTF("Seu adversario esta na casa: " + scores[0] );
                    out.writeUTF("-------------------------------");



                }
                else if(data[0].equals("Exit")){
                    out.writeUTF("Desistencia");
                    System.out.println("Esperando próximos jogadores.");
                    run();
                    return;
                }
                else{
                    System.out.println(clientSentence);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Random r = new Random();
            Socket server1 = serverSocket.accept();
            Socket server2 = serverSocket.accept();
            
            String clientSentence; // String usada para as mensagens enviadas pelos clientes
            String[] players = new String[2]; // lista de nomes dos jogadores
            players[0] = ""; // nome do jogador 1
            players[1] = ""; // nome do jogador 2
            scores = new int[]{0,0}; // indice no tabuleiro onde esta cada jogador
            board = new int[10];
            
            DataInputStream in = new DataInputStream(server1.getInputStream());
            DataOutputStream out = new DataOutputStream(server1.getOutputStream());
            DataInputStream in2 = new DataInputStream(server2.getInputStream());
            DataOutputStream out2 = new DataOutputStream(server2.getOutputStream());
            
            //inicializando os valores das posicoes do tabuleiro
            //dependendo do valor na posicao do tabuleiro, o jogador sofre efeitos extras
            for(int i = 0; i < board.length; i++){
                if(i % 3 == 0){
                    board[i] = 0; //posicao onde nada acontece
                }
                else{
                    board[i] = r.nextInt(6); //posicoes onde se voltam casas ou se andam casas extras
                }
            }
            
            //adquirindo os nomes dos jogadores
            out2.writeUTF("Aguardando adversario");
            out.writeUTF("Aguardando nome");
            while(players[0].equals("")){
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Name")){
                    if(data[1].equals("") || data[1].length() < 3){
                        out.writeUTF("Nome invalido");
                    }
                    else{
                        players[0] = data[1];
                    }
                }
                else{
                    out.writeUTF("Nome invalido");
                }
            }
            out.writeUTF("Aguardando adversario");
            out2.writeUTF("Aguardando nome");
            while(players[1].equals("")){
                clientSentence = in2.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Name")){
                    if(data[1].equals("") || data[1].length() < 3 || data[1].equals(players[0])){
                        out2.writeUTF("Nome invalido");
                    }
                    else{
                        players[1] = data[1];
                    }
                }
                else{
                    out2.writeUTF("Nome invalido");
                }
            }
            
            //executando as rodadas
            out.writeUTF("Iniciando jogo");
            out2.writeUTF("Iniciando jogo");
            rodadas(players, in, in2, out, out2);;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchCasos(int board_score, DataOutputStream jogador1, DataOutputStream jogador2){
        Random r = new Random();
        try {
            switch(board_score) {
                case 0: 
                    break;
                case 1:
                    jogador1.writeUTF("Voce passou sobre oleo, e ira voltar duas casas");
                    if (scores[0] >= 2) {
                        scores[0] -= 2;
                    }
                    else scores[0] = 0;

                    jogador2.writeUTF("Voce deu sorte, seu adversario passou sobre oleo e retornou duas casa");
                    break;

                case 2:
                    jogador1.writeUTF("Voce derrapou na curva e ira voltar uma casa");
                    if (scores[0] >= 1) {
                        scores[0] -= 1;
                    }
                    else scores[0] = 0;
                    jogador2.writeUTF("Seu adversario errou a curva e retornou uma casa");
                    break;

                case 3:
                    jogador1.writeUTF("Voce acabou de passar por um turbo, e ira andar mais duas casas");
                    scores[0] += 2;

                    jogador2.writeUTF("Seu adversario pegou um turbo e acelerou mais duas casas a frente");
                    break;

                case 4:
                    jogador1.writeUTF("Voce acabou de passar por um buraco, e ira retornar mais duas casas");
                    if (scores[0] >= 2) {
                        scores[0] -= 2;
                    }
                    else scores[0] = 0;

                    jogador2.writeUTF("Seu adversario passou por um buraco e voltou duas casas");
                    break;

                case 5:
                    jogador1.writeUTF("Voce acabou de realizar uma curva perfeita, e ira andar mais uma casa");
                    scores[0] += 1;

                    jogador2.writeUTF("Seu adversario se deu bem na curva, e andou mais uma casa");
                    break;
                    
                case 6:
                    int muitaSorte = r.nextInt(2);
                    if (muitaSorte == 1) {
                        jogador1.writeUTF("Uau, que habilidade!!! Voce encontrou um atalho e ira pular 4 casas");
                        scores[0] += 4;

                        jogador2.writeUTF("De repente seu adversario pulou 4 casas, que loucura!!!");
                        break;
                    }
                    else break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
         }
   }
    
    // Metodo main que inicia o programa servidor
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
