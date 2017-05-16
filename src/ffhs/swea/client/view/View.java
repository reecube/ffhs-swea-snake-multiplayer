package ffhs.swea.client.view;

import ffhs.swea.client.logic.Food;
import ffhs.swea.client.logic.Grid;
import ffhs.swea.client.logic.Point;
import ffhs.swea.client.logic.Snake;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class View {
    private static final int POINT_SIZE = 10;
    private static final int LABEL_HEIGHT = 10;
    private static final int LABEL_OFFSET = LABEL_HEIGHT / 2;

    private Stage primaryStage;

    private Canvas canvas;

    public View(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initializeStage(int cols, int rows) {
        StackPane root = new StackPane();

        this.canvas = new Canvas(cols * POINT_SIZE, rows * POINT_SIZE);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        scene.setFill(new Color(0.1, 0.1, 0.1, 1));

        primaryStage.setResizable(false);
        primaryStage.setTitle("Snake");
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scene);
    }

    public void registerKeyboardEvents(EventHandler<? super KeyEvent> eventHandler) {
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(eventHandler);
    }

    public void showStage() {
        primaryStage.show();
    }

    public void resetStage(Grid grid) {
        paint(grid);
    }

    public void paint(Grid grid) {
        int gridWidth = grid.getCols() * POINT_SIZE;
        int gridHeight = grid.getRows() * POINT_SIZE;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Grid.COLOR);
        gc.fillRect(0, 0, gridWidth, gridHeight);

        // Now the Food
        gc.setFill(Food.COLOR);
        paintPoint(grid.getFood().getPoint());

        // Now the snake
        Snake snake = grid.getSnake();
        gc.setFill(Snake.COLOR);
        snake.getPoints().forEach(this::paintPoint);
        if (!snake.isSafe()) {
            gc.setFill(Snake.DEAD);
            paintPoint(snake.getHead());
        }

        // The score
        gc.setFill(Color.DARKGREEN);
        gc.fillText("Length : " + (snake.getPoints().size() - 1), POINT_SIZE, gridHeight - (POINT_SIZE + LABEL_OFFSET));
    }

    private void paintPoint(Point point) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.fillRect(point.getX() * POINT_SIZE, point.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    public void paintResetMessage() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.DARKRED);
        gc.fillText("Hit RETURN to reset.", POINT_SIZE, POINT_SIZE + LABEL_OFFSET);
    }
}
