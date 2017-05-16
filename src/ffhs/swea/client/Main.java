package ffhs.swea.client;

import ffhs.swea.client.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int COLS = 50;
    private static final int ROWS = 30;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller(COLS, ROWS);

        controller.start(primaryStage);
    }
}
