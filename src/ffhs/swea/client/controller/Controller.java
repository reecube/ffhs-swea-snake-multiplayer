package ffhs.swea.client.controller;

import ffhs.swea.client.view.View;
import ffhs.swea.global.CommunicationInterface;
import ffhs.swea.global.Connection;
import ffhs.swea.global.ConnectionListener;
import ffhs.swea.global.model.UpdateObject;
import ffhs.swea.server.model.Grid;
import ffhs.swea.server.model.Snake;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.Socket;

public class Controller implements EventHandler<KeyEvent>, ConnectionListener {
    private Connection connection;
    private boolean started;
    private View view;

    public Controller(Stage primaryStage, String host, int port) throws Exception {
        this.connection = new Connection(this, new Socket(host, port));
        this.started = false;
        this.view = new View(primaryStage);
    }

    public void disconnect() throws Exception {
        connection.sendMessage(CommunicationInterface.CLIENT_DISCONNECT);
    }

    private void start(Grid grid) {
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

    private void update(Grid grid) {
        Platform.runLater(() -> view.paint(grid));
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

    @Override
    public void onObject(Connection connection, Object object) {
        if (!(object instanceof UpdateObject)) {
            System.err.println("`" + object.getClass() + "` is not a valid `" + UpdateObject.class + "` object!");
            return;
        }

        UpdateObject updateObject = (UpdateObject) object;

        Grid grid = updateObject.getGrid();

        if (started) {
            Snake playerSnake = grid.getSnakes().getOrDefault(updateObject.getHashCode(), null);

            if (playerSnake != null) {
                grid.setPlayerSnake(playerSnake);
            }

            update(grid);
        } else {
            start(grid);
            update(grid);
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
