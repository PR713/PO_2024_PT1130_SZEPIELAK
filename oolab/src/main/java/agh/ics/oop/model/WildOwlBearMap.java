package agh.ics.oop.model;

import agh.ics.oop.exceptions.IncorrectPositionException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WildOwlBearMap extends AbstractWorldMap {

    private final Vector2d owlBearAreaLowerLeft;
    private final Vector2d owlBearAreaUpperRight;
    protected WildOwlBear wildOwlBear;

    public WildOwlBearMap(int height, int width, int initialPlantCount, int dailyPlantGrowth, int consumeEnergy) {
        super(height, width, initialPlantCount, dailyPlantGrowth, consumeEnergy);

        List<Vector2d> owlBearAreaCords = getOwlBearAreaCords();
        this.owlBearAreaLowerLeft = owlBearAreaCords.get(0);
        this.owlBearAreaUpperRight = owlBearAreaCords.get(1);
        int genomeLength = AbstractAnimal.getGenomeLength();
        Genome genome = new Genome(genomeLength);
        this.wildOwlBear = new WildOwlBear(getRandomPositionOfArea(owlBearAreaLowerLeft, owlBearAreaUpperRight), MapDirection.randomOrientation(),
                genomeLength, (int) (Math.random() * genomeLength), genome);

    }


    public List<Vector2d> getOwlBearAreaCords() {
        int widthOfMap = upperRight.getX() - lowerLeft.getX() + 1;
        int heightOfMap = upperRight.getY() - lowerLeft.getY() + 1;
        int areaSurface = widthOfMap * heightOfMap;
        int areaSideLength = (int) Math.sqrt(0.2 * areaSurface); // >= shorterSide
        int lowerLeftXAreaCord = (int) (Math.random() * (widthOfMap - areaSideLength) + lowerLeft.getX());
        int lowerLeftYAreaCord = (int) (Math.random() * (heightOfMap - areaSideLength) + lowerLeft.getY());
        return List.of(new Vector2d(lowerLeftXAreaCord, lowerLeftYAreaCord),
                new Vector2d(lowerLeftXAreaCord + areaSideLength - 1, lowerLeftYAreaCord + areaSideLength - 1));
    }


    public Vector2d getRandomPositionOfArea(Vector2d lowerLeft, Vector2d upperRight) {
        int x = (int) (Math.random() * (upperRight.getX() - lowerLeft.getX()) + lowerLeft.getX());
        int y = (int) (Math.random() * (upperRight.getY() - lowerLeft.getY()) + lowerLeft.getY());
        return new Vector2d(x, y);
    }


    @Override
    public void move(AbstractAnimal animal, MapDirection direction) {
        super.move(animal, direction);
    }


    @Override
    public List<WorldElement> getAllWorldElements() {
        return Stream.concat(Stream.of(wildOwlBear), super.getAllWorldElements().stream())
                .collect(Collectors.toList());
    }


    public boolean isOwlBearMovingBeyondBordersHorizontally(Vector2d position) {
        return (position.getX() > owlBearAreaUpperRight.getX() || position.getX() < owlBearAreaLowerLeft.getX());
    }


    public boolean isOwlBearMovingBeyondBordersVertically(Vector2d position) {
        return (position.getY() > owlBearAreaUpperRight.getY() || position.getY() < owlBearAreaLowerLeft.getY());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(owlBearAreaLowerLeft) && position.precedes(owlBearAreaUpperRight);
    }

    //List<WorldElement> worldElement = new ArrayList<>(getAllGrassTufts());

    public Vector2d getOwlBearAreaUpperRight() {
        return this.owlBearAreaUpperRight;
    }

    public Vector2d getOwlBearAreaLowerLeft() {
        return this.owlBearAreaLowerLeft;
    }


    public void updateEaten() {
        Vector2d position = this.wildOwlBear.getPosition();
        if (this.animals.containsKey(position)) {
            for (Animal animal : this.animals.get(position)) {
                animal.setPassedAway(true);
                wildOwlBear.incrementAnimalsEaten();
            }
        }
        super.updateEaten();
    }
}
