package ffhs.swea.client.model;

import java.util.Random;

public class Grid {
    private final int rows;
    private final int cols;

    private Snake snake;
    private Food food;

    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        reset();
    }

    public void reset() {
        snake = new Snake(this, new Point(this.cols / 2, this.rows / 2));

        food = new Food(getRandomPoint());
    }

    Point wrap(Point point) {
        int x = point.getX();
        int y = point.getY();
        if (x >= cols) x = 0;
        if (y >= rows) y = 0;
        if (x < 0) x = cols - 1;
        if (y < 0) y = rows - 1;
        return new Point(x, y);
    }

    private Point getRandomPoint() {
        Random random = new Random();
        Point point;
        do {
            point = new Point(random.nextInt(cols), random.nextInt(rows));
        } while (snake.getPoints().contains(point));
        return point;
    }

    public void update() {
        if (food.getPoint().equals(snake.getHead())) {
            snake.extend();
            food.setPoint(getRandomPoint());
        } else {
            snake.move();
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
