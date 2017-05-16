package ffhs.swea.client.controller;

import ffhs.swea.client.view.View;
import ffhs.swea.global.CommunicationInterface;
import ffhs.swea.global.Connection;
import ffhs.swea.global.ConnectionListener;
import ffhs.swea.server.model.Grid;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.net.Socket;

public class Controller implements EventHandler<KeyEvent>, ConnectionListener {
    private Connection connection;
    private Grid grid;
    private boolean started;
    private View view;

    public Controller(Stage primaryStage, String host, int port) throws Exception {
        this.connection = new Connection(this, new Socket(host, port));
        this.grid = null;
        this.started = false;
        this.view = new View(primaryStage);
    }

    public void disconnect() throws Exception {
        connection.sendMessage(CommunicationInterface.CLIENT_DISCONNECT);
    }

    private void start() {
        if (grid == null) {
            throw new NullPointerException("The grid must not be null!");
        }

        if (started) {
            System.err.println("You can't start the view multiple times!");
            return;
        }

        this.started = true;

        Platform.runLater(() -> {
            view.initializeStage(grid.getCols(), grid.getRows());
            view.registerKeyboardEvents(this);

            view.showStage();
        });
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                // TODO
                break;
            case DOWN:
                // TODO
                break;
            case LEFT:
                // TODO
                break;
            case RIGHT:
                // TODO
                break;
            case ENTER:
                // TODO
                break;
        }
    }

    private void update() {
        Platform.runLater(() -> view.paint(grid));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onObject(Connection connection, JSONObject object) {
        Object type = object.getOrDefault(CommunicationInterface.DATA_KEY_TYPE, null);

        if (type == null) {
            System.err.println("Could not parse `" + object.toJSONString() + "`!");
            return;
        }

        switch (type.toString()) {
            case CommunicationInterface.DATA_TYPE_START:
                int cols = Integer.parseInt(object.getOrDefault(CommunicationInterface.DATA_KEY_COLS, 0).toString());
                int rows = Integer.parseInt(object.getOrDefault(CommunicationInterface.DATA_KEY_ROWS, 0).toString());

                // TODO: handle <= 0

                this.grid = new Grid(cols, rows);

                start();

                break;
            case CommunicationInterface.DATA_TYPE_UPDATE:
                // TODO: overwrite the variables

                Object objSnakeKey = object.getOrDefault(CommunicationInterface.DATA_KEY_SNAKE_KEY, null);
                Object objSnakes = object.getOrDefault(CommunicationInterface.DATA_KEY_SNAKES, null);
                Object objFoods = object.getOrDefault(CommunicationInterface.DATA_KEY_FOODS, null);

                System.out.println(objSnakeKey);
                System.out.println(objSnakes);
                System.out.println(objFoods);

//                update();

                break;
            default:
                System.err.println("Could not parse `" + object.toJSONString() + "`!");
        }
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
        ex.printStackTrace();
    }
}
