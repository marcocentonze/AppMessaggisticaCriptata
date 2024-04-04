package src.main;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import src.service.CifrarioDiCesare;

public class Client {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Client <server-ip> <port> <username>");
            System.exit(1);
        }

        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        try (Socket socket = new Socket(serverIp, port);
             Scanner userInput = new Scanner(System.in);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Invio dell'username al server subito dopo la connessione.
            out.println(username);
            System.out.println("Connected to server. Start typing messages (type 'exit' to quit).");

            // Chiede all'utente se desidera criptare i messaggi.
            System.out.println("Vuoi criptare i messaggi con il cifrario di Cesare? Si / No");
            String scelta = userInput.nextLine();
            System.out.println("di quante posizioni vuoi shiftare le lettere?");
            int sceltaShift = userInput.nextInt();

        

            // Thread per ascoltare e stampare i messaggi in arrivo dal server.
            Thread serverListener = new Thread(() -> {
                try (Scanner in = new Scanner(socket.getInputStream())) {
                    while (in.hasNextLine()) {
                        System.out.println(in.nextLine());
                    }
                } catch (IOException e) {
                    System.out.println("Errore durante la lettura dal server: " + e.getMessage());
                }
            });
            serverListener.start();

            // Ciclo principale per l'invio di messaggi.
            while (true) {
                String message = userInput.nextLine();

                // Esce dal ciclo se l'utente digita "exit".
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                // Cripta il messaggio se l'utente ha scelto di farlo.
               if(scelta.equalsIgnoreCase("yes")){
                  CifrarioDiCesare cifrarioDiCesare = new CifrarioDiCesare();
                  CifrarioDiCesare.cripta(message, sceltaShift);
                  out.println(username + ": " + message);
                  System.out.println(CifrarioDiCesare.cripta(message, sceltaShift));
              }
               }

                // Invia il messaggio al server.
              

        } catch (IOException e) {
            System.out.println("Si è verificato un errore di rete: " + e.getMessage());
        }
    }
}
