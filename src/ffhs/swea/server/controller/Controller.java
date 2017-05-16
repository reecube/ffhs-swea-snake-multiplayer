package ffhs.swea.server.controller;

import ffhs.swea.global.CommunicationInterface;
import ffhs.swea.global.ConnectionListener;
import ffhs.swea.server.logic.Game;
import ffhs.swea.global.Connection;
import org.json.simple.JSONObject;

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

                initializeClient(client);
                updateClient(client);

                clients.put(client.hashCode(), client);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void log(Connection connection, String message) {
        System.out.print(connection.hashCode() + ": ");
        System.out.println(message);
    }

    @SuppressWarnings("unchecked")
    private void initializeClient(Connection client) throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(CommunicationInterface.DATA_KEY_TYPE, CommunicationInterface.DATA_TYPE_START);
        jsonObject.put(CommunicationInterface.DATA_KEY_COLS, game.getGrid().getCols());
        jsonObject.put(CommunicationInterface.DATA_KEY_ROWS, game.getGrid().getRows());

        client.sendObject(jsonObject);

        log(client, "connected");
    }

    private void updateClient(Connection client) throws Exception {
        updateClient(client, null);
    }

    @SuppressWarnings("unchecked")
    private void updateClient(Connection client, JSONObject jsonObject) throws Exception {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
            jsonObject.put(CommunicationInterface.DATA_KEY_TYPE, CommunicationInterface.DATA_TYPE_UPDATE);
            jsonObject.put(CommunicationInterface.DATA_KEY_SNAKES, game.getGrid().getSnakes());
            jsonObject.put(CommunicationInterface.DATA_KEY_FOODS, game.getGrid().getFoods());
        }

        jsonObject.put(CommunicationInterface.DATA_KEY_SNAKE_KEY, client.hashCode());

        client.sendObject(jsonObject);
    }

    @Override
    public void onObject(Connection connection, JSONObject object) {
        System.out.println(connection.hashCode() + ": " + object.toJSONString());
    }

    @Override
    public void onMessage(Connection connection, String message) {
        log(connection, message);
    }

    @Override
    public void onError(Connection connection, String text) {
        System.err.println("Could not parse `" + text + "`!");
    }

    @Override
    public void onException(Connection connection, Exception ex) {
        System.out.println(connection.hashCode() + ": disconnected [" + ex.getMessage() + "]");

        clients.remove(connection.hashCode());
    }

    @SuppressWarnings("unchecked")
    public void afterUpdate() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommunicationInterface.DATA_KEY_TYPE, CommunicationInterface.DATA_TYPE_UPDATE);
        jsonObject.put(CommunicationInterface.DATA_KEY_SNAKES, game.getGrid().getSnakes());
        jsonObject.put(CommunicationInterface.DATA_KEY_FOODS, game.getGrid().getFoods());

        for (Connection connection : clients.values()) {
            try {
                updateClient(connection, jsonObject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
