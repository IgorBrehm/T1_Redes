import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Random;
public class Client {
   public static void main(String [] args) {
      Random r = new Random();

      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      while(true){
          try {
            Socket client = new Socket(serverName, port);
            
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
             
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
             
            Scanner input = new Scanner(System.in);
            String serverSentence;
            String name;
            
            System.out.println("Digite seu nome:");
            name = input.nextLine();
            out.writeUTF("Name-"+name);

            boolean flag = false;
            while (flag == false) {
                serverSentence = in.readUTF();
                System.out.println(serverSentence);
                if(serverSentence.equals("Iniciando jogo")){
                    flag = true;
                }
                else if(serverSentence.equals("Aguardando segundo jogador")){
                    while(true){
                        serverSentence = in.readUTF();
                        if(serverSentence.equals("Iniciando jogo")){
                            System.out.println(serverSentence);
                            flag = true;
                            break;
                        }
                    }
                }
                else{
                    System.out.println("Nome Invalido! Tente novamente.");
                    System.out.println("Digite seu nome:");
                    name = input.nextLine();
                    out.writeUTF("Name-"+name);
                }
            }
            flag = false;
            while(flag == false){
                serverSentence = in.readUTF();
                if(serverSentence.equals("Esperando o adversario jogar")){
                    System.out.println(serverSentence);
                    while(true){
                        serverSentence = in.readUTF();
                        if(serverSentence.equals("Sua vez")){
                            System.out.println(serverSentence);
                            break;
                        }
                    }            
                }
                else if(serverSentence.equals("Fim de jogo")){
                    break;   
                }
                else {
                    System.out.println(serverSentence);
                }
                System.out.println("Digite 1 para jogar o dado, 2 para sair:");
                int res = input.nextInt();
                if(res == 2){
                    out.writeUTF("Exit-"+name);
                    break;   
                }
                int roll = r.nextInt(5) + 1;
                out.writeUTF("Roll-"+roll);
            }
            client.close();
            input.close();
          } 
          catch (IOException e) {
             e.printStackTrace();
          }
        }
   }
}
