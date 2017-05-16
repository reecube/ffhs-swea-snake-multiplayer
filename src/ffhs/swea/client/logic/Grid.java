/*
 * The MIT License (MIT)
 * Copyright (c) 2016-2017 Subhomoy Haldar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package ffhs.swea.client.logic;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * The positional system for the game. This grid will be rendered in the Canvas.
 *
 * @author Subhomoy Haldar
 * @version 2016.12.17
 */
public class Grid {
    public static final Color COLOR = Color.LIGHTGREEN;

    private final int rows;
    private final int cols;

    private Snake snake;
    private Food food;

    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        // initialize the snake at the centre of the screen
        snake = new Snake(this, new Point(this.cols / 2, this.rows / 2));

        // put the food at a random location
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

    /**
     * This method is called in every cycle of execution.
     */
    void update() {
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
