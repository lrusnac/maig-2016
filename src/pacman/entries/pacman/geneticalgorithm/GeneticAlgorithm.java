package pacman.entries.pacman.geneticalgorithm;

import static pacman.game.Constants.DELAY;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.pacman.BehaviorTreesPacMan;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 100;
    private static final int FITNESS_DELTA = 100; // stop criteria
    private static final int NUMBER_REPETITIONS_FOR_EVALUATION = 30;

    private List<Gene> mPopulation;

    public GeneticAlgorithm() {
        this.mPopulation = Stream.generate(() -> Gene.getRandomizedGene()).limit(POPULATION_SIZE).collect(Collectors.toList());
    }

    public void evaluateGeneration() {
        mPopulation.stream().parallel().forEach(gene -> gene.setFitness(evaluateGene(gene)));
        Collections.sort(mPopulation);
    }

    public void produceNextGeneration() {
        List<Gene> parentSelection = rouletteWheelSelection();

        List<Gene> children = parentSelection.get(0).reproduce(parentSelection.get(1));

        for (Gene gene : children) {
            gene.mutate();
        }

        mPopulation.remove(0);
        mPopulation.remove(0);

        mPopulation.addAll(children);
    }

    private List<Gene> rouletteWheelSelection() {
        Random rnd = new SecureRandom();
        int totalScore = mPopulation.stream().mapToInt(a -> a.getFitness()).sum();

        int parentProbability1 = rnd.nextInt(totalScore);
        int parentProbability2 = rnd.nextInt(totalScore);

        Gene parent1 = null;
        Gene parent2 = null;

        int sum = 0;
        for (int i = POPULATION_SIZE - 1; i > 0; i--) {
            sum += mPopulation.get(i).getFitness();
            if (sum > parentProbability1) {
                parent1 = mPopulation.get(i);
                parentProbability1 = Integer.MAX_VALUE;
                continue; // avoid having the same parent
            }
            if (sum > parentProbability2) {
                parent2 = mPopulation.get(i);
                parentProbability2 = Integer.MAX_VALUE;
            }
            if (parent1 != null && parent2 != null) {
                break;
            }
        }
        if (parent1 == null) {
            parent1 = mPopulation.get(mPopulation.size() - 1);
        }
        if (parent2 == null) {
            parent2 = mPopulation.get(mPopulation.size() - 2);
        }

        return Arrays.asList(parent1, parent2);
    }

    public int size() {
        return mPopulation.size();
    }

    public Gene getGene(int index) {
        return mPopulation.get(index);
    }

    public static void main(String args[]) {
        GeneticAlgorithm population = new GeneticAlgorithm();
        int generationCount = 0;

        while (true) {
            population.evaluateGeneration();

            int avgFitness = 0;
            int minFitness = Integer.MAX_VALUE;
            int maxFitness = Integer.MIN_VALUE;
            String bestIndividual = "";
            String worstIndividual = "";

            for (int i = 0; i < population.size(); i++) {
                int currFitness = population.getGene(i).getFitness();
                avgFitness += currFitness;
                if (currFitness < minFitness) {
                    minFitness = currFitness;
                    worstIndividual = population.getGene(i).getPhenotype();
                }
                if (currFitness > maxFitness) {
                    maxFitness = currFitness;
                    bestIndividual = population.getGene(i).getPhenotype();
                }
            }
            if (population.size() > 0) {
                avgFitness = avgFitness / population.size();
            }
            String output = "Generation: " + generationCount;
            output += "\t AvgFitness: " + avgFitness;
            output += "\t MinFitness: " + minFitness + " (" + worstIndividual + ")";
            output += "\t MaxFitness: " + maxFitness + " (" + bestIndividual + ")";
            System.out.println(output);

            if (maxFitness - minFitness < FITNESS_DELTA) {
                System.out.println("That was the best gene with delta < then " + FITNESS_DELTA);
                break;
            }

            population.produceNextGeneration();
            generationCount++;
        }
    }

    private int evaluateGene(Gene gene) {
        int sum = 0;

        Random rnd = new SecureRandom();
        Game game;
        Controller<MOVE> pacManController = new BehaviorTreesPacMan(gene.getEscapeThreshold(), gene.getChaseThreshold(),
                gene.getMaxDepth());
        Controller<EnumMap<GHOST, MOVE>> ghostController = new StarterGhosts();

        for (int i = 0; i < NUMBER_REPETITIONS_FOR_EVALUATION; i++) {
            game = new Game(rnd.nextLong());

            while (!game.gameOver()) {
                game.advanceGame(pacManController.getMove(game.copy(), System.currentTimeMillis() + DELAY),
                        ghostController.getMove(game.copy(), System.currentTimeMillis() + DELAY));
            }

            sum += game.getScore() * (game.getCurrentLevel() + 1);
        }

        return sum / NUMBER_REPETITIONS_FOR_EVALUATION;
    }
}
