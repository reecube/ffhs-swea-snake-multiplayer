package ffhs.swea.server.controller;

import ffhs.swea.global.ConnectionListener;
import ffhs.swea.global.model.UpdateObject;
import ffhs.swea.server.logic.Game;
import ffhs.swea.global.Connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Controller implements ConnectionListener {
    private int port;

    private Game game;
    private HashMap<Integer, Connection> clients;

    public Controller(int port, int cols, int rows) {
        this.port = port;
        this.game = new Game(this, cols, rows);
        this.clients = new HashMap<>();
    }

    public void start() throws Exception {
        game.start();

        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Server running...");

        while (true) {
            Socket clientSocket = serverSocket.accept();

            try {
                Connection client = new Connection(this, clientSocket);

                connectClient(client);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void log(Connection connection, String message) {
        System.out.print(connection.hashCode() + ": ");
        System.out.println(message);
    }

    private void connectClient(Connection client) throws Exception {
        initializeClient(client);
        updateClient(client);

        int clientHashCode = client.hashCode();

        clients.put(clientHashCode, client);

        game.addPlayer(clientHashCode);

        log(client, "connected");
    }

    private void disconnectClient(Connection client) throws Exception {
        int clientHashCode = client.hashCode();

        clients.remove(clientHashCode);

        game.removePlayer(clientHashCode);

        log(client, "disconnected");
    }

    private void initializeClient(Connection client) throws Exception {
        client.sendObject(new UpdateObject(client.hashCode(), game.getGrid()));
    }

    private void updateClient(Connection client) throws Exception {
        client.sendObject(new UpdateObject(client.hashCode(), game.getGrid()));
    }

    @Override
    public void onObject(Connection connection, Object object) {
        System.out.println(object);
        System.out.println(object.getClass());
    }

    @Override
    public void onMessage(Connection connection, String message) {
        System.out.println(message);
    }

    @Override
    public void onError(Connection connection, String text) {
        System.err.println("Could not parse `" + text + "`!");
    }

    @Override
    public void onException(Connection connection, Exception ex) {
        System.err.println(connection.hashCode() + ": " + ex.getMessage());

        try {
            disconnectClient(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afterUpdate() {
        for (Connection connection : clients.values()) {
            try {
                updateClient(connection);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
