package net.specialattack.beacon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class Main {

    private static boolean stopping = false;
    private static Map<String, Client> clients = new TreeMap<>();

    public static void main(String[] args) {
        Motd.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (stopping) {
                return;
            }
            stopping = true;

            try {
                for (Client client : clients.values()) {
                    client.stop();
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted when shutting down");
                e.printStackTrace();
            }
        }));

        try (ServerSocket socket = new ServerSocket(25565)) {
            while (true) {
                try {
                    Socket clientSocket = socket.accept();
                    clientSocket.setSoTimeout(30000);

                    if (clients.size() > 64) {
                        clientSocket.close();
                    }

                    String _uuid;
                    do {
                        _uuid = UUID.randomUUID().toString().substring(0, 8);
                    } while (clients.containsKey(_uuid));

                    String uuid = _uuid;

                    System.out.printf("Adding client %s%n", uuid);

                    Client client = new Client(clientSocket, uuid, () -> {
                        System.out.printf("Removing client %s%n", uuid);
                        clients.remove(uuid);
                    });

                    clients.put(uuid, client);
                } catch (IOException e) {
                    System.err.println("Caught exception creating client connection");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
