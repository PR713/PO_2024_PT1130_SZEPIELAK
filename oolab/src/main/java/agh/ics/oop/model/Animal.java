package agh.ics.oop.model;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.Simulation;

public class Animal {
    private MapDirection orientation = MapDirection.NORTH;
    public Vector2d position;

    public Animal() {
        this.position = new Vector2d(2,2);
    }

    public Animal(Vector2d vector) {
        this.position = vector;
    }

    @Override
    public String toString(){
        return "(%s, %s)".formatted(this.position.getX(), this.position.getY());
    }

    public boolean isAt(Vector2d position) {
        return this.position == position;
    }

    public void move(MoveDirection direction) {
        Vector2d newPosition = this.position;
        switch(direction) {
            case LEFT: this.orientation = orientation.previous();
                break;
            case RIGHT: this.orientation = orientation.next();
                break;
            case FORWARD:
                switch (this.orientation) {
                    case NORTH: newPosition = new Vector2d(this.position.getX(), this.position.getY()+1);
                        break;
                    case SOUTH: newPosition = new Vector2d(this.position.getX(), this.position.getY()-1);
                        break;
                    case EAST: newPosition = new Vector2d(this.position.getX()+1, this.position.getY());
                        break;
                    case WEST: newPosition = new Vector2d(this.position.getX()-1, this.position.getY());
                        break;
                }; break;
            case BACKWARD:
                switch (this.orientation) {
                    case NORTH: newPosition = new Vector2d(this.position.getX(), this.position.getY()-1);
                        break;
                    case SOUTH: newPosition = new Vector2d(this.position.getX(), this.position.getY()+1);
                        break;
                    case EAST: newPosition = new Vector2d(this.position.getX()-1, this.position.getY());
                        break;
                    case WEST: newPosition = new Vector2d(this.position.getX()+1, this.position.getY());
                        break;
                }; break;
        };
        if (position.getX() >= 0 && position.getX() <= 4 &&
                position.getY() >= 0 && position.getY() <= 4) {
            this.position = newPosition;
        }
    }


}
