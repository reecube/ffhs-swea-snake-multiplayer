package ffhs.swea.client;

import ffhs.swea.client.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application implements EventHandler<WindowEvent> {
    private Controller controller;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(this);

        this.controller = new Controller(primaryStage, "localhost", 12345);
    }

    @Override
    public void handle(WindowEvent event) {
        try {
            controller.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
