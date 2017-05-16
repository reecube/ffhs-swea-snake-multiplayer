package ffhs.swea.client.view;

import ffhs.swea.server.model.Food;
import ffhs.swea.server.model.Grid;
import ffhs.swea.server.model.Point;
import ffhs.swea.server.model.Snake;
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

    private static final Color COLOR_GRID = Color.LIGHTGREEN;
    private static final Color COLOR_FOOD = Color.DARKRED;
    private static final Color COLOR_ENEMY = Color.GRAY;
    private static final Color COLOR_ENEMY_DEAD = Color.DARKGRAY;
    private static final Color COLOR_SNAKE = Color.GREEN;
    private static final Color COLOR_SNAKE_DEAD = Color.DARKGREEN;
    private static final Color COLOR_LABEL_SCORE = Color.DARKGREEN;
    private static final Color COLOR_LABEL_GAME_OVER = Color.DARKRED;

    private Stage primaryStage;

    private Canvas canvas;
    private GraphicsContext gc;

    public View(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initializeStage(int cols, int rows) {
        StackPane root = new StackPane();

        this.canvas = new Canvas(cols * POINT_SIZE, rows * POINT_SIZE);
        this.gc = canvas.getGraphicsContext2D();

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

    public void paint(Grid grid) {
        int gridWidth = grid.getCols() * POINT_SIZE;
        int gridHeight = grid.getRows() * POINT_SIZE;

        gc.setFill(COLOR_GRID);
        gc.fillRect(0, 0, gridWidth, gridHeight);

        gc.setFill(COLOR_FOOD);
        for (Food food : grid.getFoods()) {
            paintPoint(food.getPoint());
        }

        gc.setFill(COLOR_ENEMY);
        for (Snake snake : grid.getSnakes().values()) {
            snake.getPoints().forEach(this::paintPoint);
            if (!snake.isSafe()) {
                gc.setFill(COLOR_ENEMY_DEAD);
                paintPoint(snake.getHead());
            }
        }

        Snake playerSnake = grid.getPlayerSnake();
        if (playerSnake != null) {
            gc.setFill(COLOR_SNAKE);
            playerSnake.getPoints().forEach(this::paintPoint);
            if (!playerSnake.isSafe()) {
                gc.setFill(COLOR_SNAKE_DEAD);
                paintPoint(playerSnake.getHead());
            }

            gc.setFill(COLOR_LABEL_SCORE);
            gc.fillText("Length : " + (playerSnake.getPoints().size() - 1), POINT_SIZE, gridHeight - (POINT_SIZE + LABEL_OFFSET));
        }
    }

    private void paintPoint(Point point) {
        gc.fillRect(point.getX() * POINT_SIZE, point.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    public void paintResetMessage() {
        gc.setFill(COLOR_LABEL_GAME_OVER);
        gc.fillText("Hit RETURN to reset.", POINT_SIZE, POINT_SIZE + LABEL_OFFSET);
    }
}
