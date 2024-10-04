package ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Player pHome;
    public Client(Player p){
        pHome=p;
    }
    public void start(String serverIp){
        try{
            Socket clientSocket = new Socket(serverIp, 6000);
            System.out.println("Connected to server!!!");

            BufferedReader input=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output=new PrintWriter(clientSocket.getOutputStream(),true);
            output.println(pHome.letter);

            // Step 4: Read and display the response from the server
            new Thread(() -> {
                try {
                    String messageFromServer;
                    while ((messageFromServer = input.readLine()) != null) {
                        System.out.println("Server: " + messageFromServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connection lost. Exiting listener thread.");
                }
            }).start();
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String userMessage;
            while (true) {
                userMessage = userInput.readLine();  // Read user input
                output.println(userMessage);  // Send user message to the server
                if (userMessage.equalsIgnoreCase("exit")) {
                    break;  // Exit loop if user types "exit"
                }
            }





            // Step 5: Close connections
            userInput.close();
            input.close();
            output.close();
            clientSocket.close();


        }catch(Exception e){System.out.println(e);}
    }

    public static void main(String [] args){
        Player p=new Player("X");
        Client c=new Client(p);

        c.start("localhost");
    }
}
