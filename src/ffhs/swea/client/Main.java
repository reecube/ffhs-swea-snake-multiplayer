package ffhs.swea.client;

import ffhs.swea.client.logic.GameLoop;
import ffhs.swea.client.logic.Grid;
import ffhs.swea.client.logic.Snake;
import ffhs.swea.client.view.Painter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This is the place where the threads are dispatched.
 *
 * @author Subhomoy Haldar
 * @version 2016.12.17
 */
public class Main extends Application {

    private static final int COLS = 50;
    private static final int ROWS = 30;

    private GameLoop loop;
    private Grid grid;
    private GraphicsContext context;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(COLS * Painter.POINT_SIZE, ROWS * Painter.POINT_SIZE);
        context = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            Snake snake = grid.getSnake();
            if (loop.isKeyPressed()) {
                return;
            }
            switch (e.getCode()) {
                case UP:
                    snake.setUp();
                    break;
                case DOWN:
                    snake.setDown();
                    break;
                case LEFT:
                    snake.setLeft();
                    break;
                case RIGHT:
                    snake.setRight();
                    break;
                case ENTER:
                    if (loop.isPaused()) {
                        reset();
                        (new Thread(loop)).start();
                    }
            }
            loop.setKeyPressed();
        });

        reset();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        scene.setFill(new Color(0.1, 0.1, 0.1, 1));

        primaryStage.setResizable(false);
        primaryStage.setTitle("Snake");
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.show();

        (new Thread(loop)).start();
    }

    private void reset() {
        grid = new Grid(COLS, ROWS);
        loop = new GameLoop(grid, context);
        Painter.paint(grid, context);
    }
}
