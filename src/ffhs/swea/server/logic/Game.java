package ffhs.swea.server.logic;

import ffhs.swea.server.controller.Controller;
import ffhs.swea.server.model.Food;
import ffhs.swea.server.model.Grid;
import ffhs.swea.server.model.Point;
import ffhs.swea.server.model.Snake;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements Runnable {
    private Controller controller;
    private GameLoop loop;
    private Grid grid;

    public Game(Controller controller, int cols, int rows) {
        this.controller = controller;
        this.loop = new GameLoop(this);
        this.grid = new Grid(cols, rows);
    }

    public Grid getGrid() {
        return grid;
    }

    public void start() {
        (new Thread(loop)).start();
    }

    private Point wrap(Point point) {
        int x = point.getX();
        int y = point.getY();
        if (x >= grid.getCols()) x = 0;
        if (y >= grid.getRows()) y = 0;
        if (x < 0) x = grid.getCols() - 1;
        if (y < 0) y = grid.getRows() - 1;
        return new Point(x, y);
    }

    private Point getRandomPoint() {
        List<Point> points = new ArrayList<>();

        for (Snake snake : grid.getSnakes().values()) {
            points.addAll(snake.getPoints());
        }

        for (Food food : grid.getFoods()) {
            points.add(food.getPoint());
        }

        return getRandomPoint(points);
    }

    private Point getRandomPoint(List<Point> points) {
        Random random = new Random();
        Point point;

        do {
            point = new Point(random.nextInt(grid.getCols()), random.nextInt(grid.getRows()));
        } while (points.contains(point));

        return point;
    }

    private void moveSnake(Snake snake) {
        if (!snake.isStill()) {
            snake.addPoint(wrap(snake.translateHead()));
            snake.removeOldestPoint();
        }
    }

    private void extendSnake(Snake snake) {
        if (!snake.isStill()) {
            snake.addPoint(wrap(snake.translateHead()));
        }
    }

    @Override
    public void run() {
        for (Snake snake : grid.getSnakes().values()) {
            for (Food food : grid.getFoods()) {
                if (food.getPoint().equals(snake.getHead())) {
                    extendSnake(snake);
                    food.setPoint(getRandomPoint(snake.getPoints()));
                } else {
                    moveSnake(snake);
                    break;
                }
            }
        }

        controller.afterUpdate();
    }

    public void addPlayer(int playerKey) {
        grid.addSnake(playerKey, new Snake(getRandomPoint()));
        grid.addFood(new Food(getRandomPoint()));
    }

    public void removePlayer(int playerKey) {
        grid.removeSnake(playerKey);
        grid.removeFood();
    }

    public void onKeyPress(int hashCode, KeyCode keyCode) {
        Snake snake = grid.getSnakes().getOrDefault(hashCode, null);

        if (snake == null) {
            return;
        }

        switch (keyCode) {
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
                // TODO
                break;
        }
    }
}
