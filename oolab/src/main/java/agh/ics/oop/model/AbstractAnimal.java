package agh.ics.oop.model;

import static agh.ics.oop.model.MapDirection.fromNumericValue;

public class AbstractAnimal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;

    public AbstractAnimal(Vector2d vector, MapDirection orientation) {
        this.position = vector;
        this.orientation = orientation;
    }


    @Override
    public String toString() {
        return switch (this.getOrientation()) {
            case N -> "^";
            case E -> ">";
            case S -> "v";
            case W -> "<";
            case NE -> "↗";
            case SE -> "↘";
            case SW -> "↙";
            case NW -> "↖";
        };
    }


    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }


    public MapDirection getOrientation() {
        return this.orientation;
    }


    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public void move(MoveValidator validator, int direction) {
        MapDirection newOrientation = fromNumericValue((this.orientation.getNumericValue() + direction) % 8);
        Vector2d newPosition = this.position.add(this.orientation.toMapDirectionVector());

        if (validator.canMoveTo(newPosition)) {
            this.position = newPosition;
            this.orientation = newOrientation;
        } else {
            if (validator.isMovingBeyondBordersHorizontally(newPosition)
                    && validator.isMovingBeyondBordersVertically(newPosition)) {
                //position doesn't change
                this.orientation = newOrientation.reverseOrientation();

                //mapa o lowerLeft w (0,0)
            } else if (validator.isMovingBeyondBordersHorizontally(newPosition)) {
                this.position = this.position.getX() > validator.getUpperRight().getX() ?
                        new Vector2d(0, this.position.getY()) :
                        new Vector2d(validator.getUpperRight().getX(), this.position.getY());
                this.orientation = newOrientation.reverseOrientation();
            } else { // vertically but not in corners, position doesn't change
                this.orientation = newOrientation.reverseOrientation();
            }

        }
    }
}



