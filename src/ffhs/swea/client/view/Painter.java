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

package ffhs.swea.client.view;

import ffhs.swea.client.logic.Food;
import ffhs.swea.client.logic.Grid;
import ffhs.swea.client.logic.Point;
import ffhs.swea.client.logic.Snake;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Subhomoy Haldar
 * @version 2016.12.17
 */
public class Painter {
    public static final int POINT_SIZE = 10;
    private static final int LABEL_HEIGHT = 10;
    private static final int LABEL_OFFSET = LABEL_HEIGHT / 2;

    public static void paint(Grid grid, GraphicsContext gc) {
        int gridWidth = grid.getCols() * POINT_SIZE;
        int gridHeight = grid.getRows() * POINT_SIZE;

        gc.setFill(Grid.COLOR);
        gc.fillRect(0, 0, gridWidth, gridHeight);

        // Now the Food
        gc.setFill(Food.COLOR);
        paintPoint(grid.getFood().getPoint(), gc);

        // Now the snake
        Snake snake = grid.getSnake();
        gc.setFill(Snake.COLOR);
        snake.getPoints().forEach(point -> paintPoint(point, gc));
        if (!snake.isSafe()) {
            gc.setFill(Snake.DEAD);
            paintPoint(snake.getHead(), gc);
        }

        // The score
        gc.setFill(Color.DARKGREEN);
        gc.fillText("Length : " + (snake.getPoints().size() - 1), POINT_SIZE, gridHeight - (POINT_SIZE + LABEL_OFFSET));
    }

    private static void paintPoint(Point point, GraphicsContext gc) {
        gc.fillRect(point.getX() * POINT_SIZE, point.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    public static void paintResetMessage(GraphicsContext gc) {
        gc.setFill(Color.DARKRED);
        gc.fillText("Hit RETURN to reset.", POINT_SIZE, POINT_SIZE + LABEL_OFFSET);
    }
}
