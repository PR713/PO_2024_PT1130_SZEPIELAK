package agh.ics.oop.model;

import agh.ics.oop.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected final MapVisualizer visualizer;
    private final List<MapChangeListener> observers = new ArrayList<>();
    //observers w przyszłości jak WorldElement może mieć różne typy obiektów a implementację MapChangeListener
    //już w swoich klasach jak Animal i Grass = WorldElement
    private final UUID id;

    public AbstractWorldMap(Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.visualizer = new MapVisualizer(this);
        this.id = UUID.randomUUID();

    }


    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            mapChanged(String.format("New animal placed at position: %s", animal.getPosition()));
        } else {
            throw new IncorrectPositionException(animal.getPosition());
        }

    }


    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(this, direction);
        Vector2d newPosition = animal.getPosition();

        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged(String.format("Animal moved from %s to %s", oldPosition, newPosition));
        }
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }


    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        return Optional.ofNullable(animals.get(position));
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }


    @Override
    public List<WorldElement> getElements(){
        return List.copyOf(animals.values());
    }


    @Override
    public abstract Boundary getCurrentBounds();


    public void addObserver(MapChangeListener observer){
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }


    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }


    protected void mapChanged(String message){
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }


    @Override
    public String toString() {
        Boundary bounds = getCurrentBounds();
        return visualizer.draw(bounds.bottomLeft(), bounds.topRight());
    }


    @Override
    public UUID getId(){
        return id;
    }

    @Override
    public List<Animal> getOrderedAnimals(){
        return animals.values().stream().sorted(Comparator.comparing((Animal animal) -> animal.getPosition().getX())
                .thenComparing(animal -> animal.getPosition().getY())).toList();
    }
}
