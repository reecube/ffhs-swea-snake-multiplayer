package ffhs.swea.server.model;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private boolean safe;
    private List<Point> points = null;
    private Point head;
    private int xVelocity;
    private int yVelocity;

    public Snake(Point initialPoint) {
        reset(initialPoint);
    }

    public void reset() {
        reset(null);
    }

    private void reset(Point initialPoint) {
        if (initialPoint == null) {
            initialPoint = head;
        }

        if (points == null) {
            points = new LinkedList<>();
        } else {
            points.clear();
        }
        points.add(initialPoint);

        head = initialPoint;

        safe = true;
        xVelocity = 0;
        yVelocity = 0;
    }

    public void kill() {
        safe = false;
        xVelocity = 0;
        yVelocity = 0;
    }

    public List<Point> getPoints() {
        return points;
    }

    public boolean isSafe() {
        return safe || points.size() == 1;
    }

    public Point getHead() {
        return head;
    }

    public Point translateHead() {
        return head.translate(xVelocity, yVelocity);
    }

    public void addPoint(Point point) {
        if (points.contains(point)) {
            kill();
        }
        points.add(point);
        head = point;
    }

    public void removeOldestPoint() {
        points.remove(0);
    }

    public boolean isStill() {
        return xVelocity == 0 & yVelocity == 0;
    }

    public void setUp() {
        if (yVelocity == 1 && points.size() > 1) {
            return;
        }

        xVelocity = 0;
        yVelocity = -1;
    }

    public void setDown() {
        if (yVelocity == -1 && points.size() > 1) {
            return;
        }

        xVelocity = 0;
        yVelocity = 1;
    }

    public void setLeft() {
        if (xVelocity == 1 && points.size() > 1) {
            return;
        }

        xVelocity = -1;
        yVelocity = 0;
    }

    public void setRight() {
        if (xVelocity == -1 && points.size() > 1) {
            return;
        }

        xVelocity = 1;
        yVelocity = 0;
    }
}
