package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public class GrassField extends AbstractWorldMap {
    private final int numOfGrassFields;
    private final int maxDimensionOfGrassFields;
    private final MapVisualizer visualizer;
    private static final Vector2d MIN_VALUE = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final Vector2d MAX_VALUE = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private Vector2d maxiValTemp = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private Vector2d miniValTemp = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private final List<WorldElement> elements = new ArrayList<>();

    public GrassField(int numOfGrassFields){
        super(MIN_VALUE, MAX_VALUE);
        this.numOfGrassFields = numOfGrassFields;
        this.maxDimensionOfGrassFields = (int) Math.floor(Math.sqrt(numOfGrassFields*10));
        this.visualizer = new MapVisualizer(this);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxDimensionOfGrassFields, maxDimensionOfGrassFields, numOfGrassFields);
        Iterator<Vector2d> positionsIterator = randomPositionGenerator.iterator();

        while(positionsIterator.hasNext()) {
            Vector2d grassPosition = positionsIterator.next();
            grassTufts.put(grassPosition, new Grass(grassPosition));
        }
    }

    //przeciążenie metody
    public boolean place(Grass grass){
        if(canMoveTo(grass.getPosition()) && !isOccupied(grass.getPosition())) {
            grassTufts.put(grass.getPosition(), grass);
            return true;
        }
        return false;
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return grassTufts.containsKey(position);
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement animalExist = super.objectAt(position);
        if (animalExist == null){
            if (isOccupied(position)){
                return grassTufts.get(position);
            } else return null;
        } else return animalExist;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return !super.isOccupied(position);
    }


    @Override
    public String toString(){

        for (Vector2d position : animals.keySet()) {
            miniValTemp = miniValTemp.lowerLeft(position);
            maxiValTemp = maxiValTemp.upperRight(position);
        }

        for (Vector2d position : grassTufts.keySet()) {
            miniValTemp = miniValTemp.lowerLeft(position);
            maxiValTemp = maxiValTemp.upperRight(position);
        }

        return visualizer.draw(miniValTemp, maxiValTemp);
    }


    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> allElements = new ArrayList<>(super.getElements());
        allElements.addAll(grassTufts.values());
        return allElements;
    }


    public Map<Vector2d, Grass> getGrassTufts() {
        return Collections.unmodifiableMap(grassTufts);
    }
}

