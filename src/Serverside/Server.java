package Serverside;
import ClientSide.Player;
import Serverside.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    String p1,p2;
    static Game game=new Game();
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    public void start(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port 5000...");
            int x=2;
            // Continuously listen for client connections
            while (x>0) {
                x--;
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                // Handle client in a separate thread
                new Thread(new ClientHandler(clientSocket)).start();
                if(x==0){
                    System.out.println("Two clients connected and assigned numbers");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Setup input and output streams for communication
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Add client writer to the shared set
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // Read messages from this client and broadcast to all others
                String message;
                int count=0;
                Overloop:while ((message = input.readLine()) != null) {
                    System.out.println("Received: " + message);
                    if(message.equals("X") || message.equals("O")){
                        game.addPlayer(message);
                        System.out.println(message+" added a player");
                        count++;
                    }
                    broadcast(message);
                    if(count==2){
                        break Overloop;
                    }

                }
                String messageToClients;
                while(game.winner.equals("")){
                    message=input.readLine();
                    messageToClients=game.makeMove(message);
                    broadcast(messageToClients);
                }

                // Remove client from the set when disconnected
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }

                input.close();
                out.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Method to broadcast a message to all connected clients
        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
    public static void main(String [] args){
        Server server=new Server();
        server.start(6000);

    }




}

