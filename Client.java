import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
   public static void main(String [] args) {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      try {
        Socket client = new Socket(serverName, port);
        
        OutputStream outToServer = client.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
         
        InputStream inFromServer = client.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
         
        Scanner input = new Scanner (System.in);
        String serverSentence;
        String name;
        
        System.out.println("Digite seu nome:");
        name = input.nextLine();
        out.writeUTF("Name-"+name);

        boolean flag = false;
        while (flag == false) {
            serverSentence = in.readUTF();
            if(serverSentence.equals("Iniciando jogo")){
                flag = true;
            }
            else if(serverSentence.equals("Aguardando segundo jogador")){
                System.out.println("Aguardando o seu adversario chegar!");
                while(true){
                    serverSentence = in.readUTF();
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
                out.writeUTF("Name-"+name);
            }
        }
        flag = false;
        while(flag == false){
            System.out.println("Digite um numero:");
            String test = input.nextLine();
            out.writeUTF(test);
            // TODO execucao das rodadas
        }
        client.close();
        input.close();
        System.exit(0);
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
   }
}
