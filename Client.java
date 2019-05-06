import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

/*
 * Jogo de corrida usando arquitetura cliente-servidor.
 * 
 * @author Guilherme Picolli, Fernando Maioli, Igor Brehm
 */

public class Client {
   public static void main(String [] args) {
      Random r = new Random();

      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      try {
        Socket client = new Socket(serverName, port);

        OutputStream outToServer = client.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
         
        InputStream inFromServer = client.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
         
        Scanner input = new Scanner(System.in);
        String serverSentence = "";
        String name = "";
        
        //Adquirindo os nomes dos jogadores
        while (true) {
            serverSentence = in.readUTF();
            if(serverSentence.equals("Iniciando jogo")){
                break;
            }
            else if(serverSentence.equals("Aguardando nome")){
                System.out.println("Digite seu nome:");
                name = input.nextLine();
                out.writeUTF("Name-"+name);   
            }
            else if(serverSentence.equals("Aguardando adversario")){
                System.out.println(serverSentence);
                while(true){
                    serverSentence = in.readUTF();
                    if(serverSentence.equals("Aguardando nome")){
                        System.out.println("Digite seu nome:");
                        name = input.nextLine();
                        out.writeUTF("Name-"+name); 
                    }
                    else if(serverSentence.equals("Nome invalido")){
                        System.out.println("Nome Invalido! Tente novamente.");
                        System.out.println("Digite seu nome:");
                        name = input.nextLine();
                        out.writeUTF("Name-"+name);
                    }
                    if(serverSentence.equals("Iniciando jogo")){
                        System.out.println("\n"+serverSentence);
                        break;
                    }
                }
                break;
            }
            else{
                System.out.println("Nome Invalido! Tente novamente.");
                System.out.println("Digite seu nome:");
                name = input.nextLine();
                out.writeUTF("Name-"+name);
            }
        }
        
        int res = 0;
        int roll = 0;
        boolean flag = false;
        //Executando as rodadas
        while(flag == false){
            serverSentence = in.readUTF();
            if(serverSentence.equals("Aguardando adversario")){
                System.out.println(serverSentence);
                while(true){
                    serverSentence = in.readUTF();
                    if(serverSentence.equals("Sua vez")){
                        System.out.println("\n"+serverSentence);
                        System.out.println("\nDigite 1 para jogar o dado, 2 para sair:");
                        res = input.nextInt();
                        if(res == 2){
                            out.writeUTF("Exit-"+name);
                            flag = true;
                            break;   
                        }
                        roll = r.nextInt(5) + 1;
                        System.out.println("\nAvancou " + roll + " casas!\n");
                        out.writeUTF("Roll-"+roll);
                        break;
                    }
                    else if(serverSentence.equals("Fim de jogo")){
                        serverSentence = in.readUTF();
                        System.out.println(serverSentence);
                        flag = true;
                        break;   
                    }
                    else if(serverSentence.equals("Desistencia")){
                        System.out.println("Infelizmente, seu adversário desistiu do jogo.");
                        System.out.println("Esperamos poder jogar com você novamente.");
                        flag=true;
                        break;
                    }
                    else{
                        System.out.println(serverSentence);
                    }
                }            
            }
            else if(serverSentence.equals("Sua vez")){
                System.out.println("\n"+serverSentence);
                System.out.println("\nDigite 1 para jogar o dado, 2 para sair:");
                res = input.nextInt();
                if(res == 2){
                    out.writeUTF("Exit-"+name);
                    break;   
                }
                roll = r.nextInt(5) + 1;
                System.out.println("\nAvancou " + roll + " casas!\n");
                out.writeUTF("Roll-"+roll);
            }
            else if(serverSentence.equals("Fim de jogo")){
                serverSentence = in.readUTF();
                System.out.println(serverSentence);
                break;   
            }
            else if(serverSentence.equals("Desistencia")){
                System.out.println("Infelizmente, seu adversário desistiu do jogo.");
                System.out.println("Esperamos poder jogar com você novamente.");
                break;
            }
            else {
                System.out.println(serverSentence);
            }
            if(serverSentence.equals("Desistencia")){
                break;
            }
        }
        client.close();
        input.close();
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
   }
}
