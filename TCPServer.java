import java.io.*;
import java.net.*;
import java.time.*;

class TCPServer {
	public static void main(String args[]) throws Exception {
		String clientSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		int bestBet = 0;
        String bestBetOwner = "";
		int num_bets = 10;
		while (true) {
			if(num_bets == 0){
				System.out.println("\nLeilao terminou!");
				System.out.println("Lance Vencedor: "+ bestBet);
                System.out.println("Feito por: "+ bestBetOwner);
				break;
			}
			else{
				System.out.println("\nLances Restantes: "+ num_bets);
				num_bets = num_bets - 1;	
			}
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient =
			new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
            String[] data = clientSentence.split("-", 2);
			System.out.println("Lance Recebido: " + data[1]);
            System.out.println("Feito por: "+ data[0]);
			if(Integer.parseInt(data[1]) > bestBet){
				bestBet = Integer.parseInt(data[1]);
                bestBetOwner = data[0];
				System.out.println("\nMaior lance ate agora! "+ bestBet+"!");
                System.out.println("Lance feito por: "+ bestBetOwner);
			}
		}
	}
}
