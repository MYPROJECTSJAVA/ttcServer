import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    static Game game=new Game();//new game object
    private static Set<PrintWriter> clientWriters = new HashSet<>();// needed to store the output object to sent responses to
    static ServerSocket serverSocket;

    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port 6000...");
            int x=2;
            // Listens for two clients to connect
            while (x>0) {
                x--;
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                // Handle client in a separate thread
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.out.println("Exception at line 30 Server.java");
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
                String message=input.readLine();
                System.out.println("Received: " + message);
                if(message.equals("X") || message.equals("O")){
                     game.addPlayer(message);//adds player to the game
                     System.out.println(message+" added as player");

                    }
                broadcast(message);//sents back values to the clients


                String messageToClients;
                while(game.winner.equals("")){
                    message=input.readLine();
                    messageToClients=game.makeMove(message);
                    broadcast(messageToClients);
                    if(!game.winner.equals("")){
                        broadcast(game.winner);//send the winner to the clients
                    }
                }

                // Remove client from the set when disconnected
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }

                input.close();
                out.close();
                socket.close();
                serverSocket.close();

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

