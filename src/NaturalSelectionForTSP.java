import java.util.HashSet;
import java.util.Set;

public class NaturalSelectionForTSP {

    private final static int defaultPopulationSize = 20;
    private final static double defaultMutationProbability = 0.1;

    private Path[] matingPool = new Path[2];
    private Path[] population;
    private int populationSize;
    private int pointsCount;
    private double mutationProbability;

    private NaturalSelectionForTSP(int pointsCount, int populationSize, double mutationProbability) {
        this.pointsCount = pointsCount;
        if (populationSize >= 2) {
            this.populationSize = populationSize;
        } else {
            this.populationSize = defaultPopulationSize;
        }

        population = new Path[this.populationSize];

        if (mutationProbability < 0 || mutationProbability > 1) {
            this.mutationProbability = defaultMutationProbability;
        } else {
            this.mutationProbability = mutationProbability;
        }

        generateInitialPopulation();
    }

    private void generateInitialPopulation() {
        Path path = new Path(pointsCount);

        for (int i = 0; i < populationSize; ++i) {
            population[i] = new Path(path);

            // shuffle the points order in the path
            for (int j = 0; j < pointsCount; ++j) {
                population[i].mutate();
            }
        }
    }

    private void updateMatingPool() {

        Path firstBest;
        Path secondBest;

        if (population[0].getLength() < population[1].getLength()) {
            firstBest = population[0];
            secondBest = population[1];
        } else {
            firstBest = population[1];
            secondBest = population[0];
        }
        for (int i = 2; i < population.length; i++) {
            if (population[i].getLength() < firstBest.getLength()) {
                secondBest = firstBest;
                firstBest = population[i];
            } else if (population[i].getLength() < secondBest.getLength()) {
                secondBest = population[i];
            }
        }

        // we must use a copy of the elements, not the actual elements
        matingPool[0] = new Path(firstBest);
        matingPool[1] = new Path(secondBest);
    }

    // performs ordered crossover
    private void mate() {
        Path first = matingPool[0];
        Path second = matingPool[1];

        for (int i = 0; i < populationSize; i++) {
            Path newPath = population[i];

            // get a random subpath from the first path
            int subPathStart = Path.randomGenerator.nextInt(pointsCount);
            int subPathLength = Path.randomGenerator.nextInt(pointsCount + 1);

            Set<Point> alreadyCopiedPoints = new HashSet<>();

            // copy the subpath to the start of the new path
            int newPathIndex = 0;
            for (; newPathIndex < subPathLength; newPathIndex++) {
                int firstPathIndex = (subPathStart + newPathIndex) % pointsCount;

                Point newPoint = first.getPoint(firstPathIndex);

                newPath.setPoint(newPathIndex, newPoint);

                alreadyCopiedPoints.add(newPoint);
            }

            // copy all points from the second path that are not included in the new path
            for (int j = 0; j < pointsCount; ++j) {
                Point point = second.getPoint(j);
                if (!alreadyCopiedPoints.contains(point)) {
                    newPath.setPoint(newPathIndex++, point);
                }
            }
        }
    }

    private void mutate() {
        for (Path path : population) {
            double random = Path.randomGenerator.nextDouble();
            if (random < mutationProbability) {
                path.mutate();
            }
        }
    }

    public static void main(String[] args) {
        int citiesCount = Integer.parseInt(args[0]);
        int populationSize = (args.length >= 2) ? Integer.parseInt(args[1]) : 0;
        double mutationProbability = (args.length >= 3) ? Double.parseDouble(args[2]) : 0;
        int generationsCount = (args.length >= 4) ? Integer.parseInt(args[3]) : 500;

        NaturalSelectionForTSP tsp = new NaturalSelectionForTSP(citiesCount, populationSize, mutationProbability);

        for (int generation = 0; generation < generationsCount; generation++) {
            tsp.updateMatingPool();
            switch (generation) {
                case 10:
                case 20:
                case 50:
                case 100:
                    System.out.printf("%f, generation = %d \n", tsp.matingPool[0].getLength(), generation);
            }
            tsp.mate();
            tsp.mutate();
        }
        System.out.printf("%f, generation = %d \n", tsp.matingPool[0].getLength(), generationsCount);

    }
}
