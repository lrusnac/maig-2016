package pacman.entries.pacman.geneticalgorithm;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Gene implements Comparable<Gene>, Comparator<Gene> {

    private static final int DISTANCE_THRESHOLD_UPPER_BOUND = 100;
    private static final int DISTANCE_THRESHOLD_LOWER_BOUND = 0;

    private int fitness;

    private int escapeThreshold;
    private int chaseThreshold;
    private int maxDepth;

    private SecureRandom random;

    public Gene() {
        this.random = new SecureRandom();
    }

    public void randomiseChromosome() {
        escapeThreshold = getRandomInBounds();
        chaseThreshold = getRandomInBounds();
        maxDepth = getRandomInBounds();
    }

    public List<Gene> reproduce(Gene gene) {
        // uniform crossover with 0.66 rate
        Gene child1 = new Gene();
        Gene child2 = new Gene();

        if (random.nextInt(2) == 0) {
            child1.setChaseThreshold(this.getChaseThreshold());
            child2.setChaseThreshold(gene.getChaseThreshold());
        } else {
            child1.setChaseThreshold(gene.getChaseThreshold());
            child2.setChaseThreshold(this.getChaseThreshold());
        }

        if (random.nextInt(2) == 0) {
            child1.setEscapeThreshold(this.getEscapeThreshold());
            child2.setEscapeThreshold(gene.getEscapeThreshold());
        } else {
            child1.setEscapeThreshold(gene.getEscapeThreshold());
            child2.setEscapeThreshold(this.getEscapeThreshold());
        }

        if (random.nextInt(2) == 0) {
            child1.setMaxDepth(this.getMaxDepth());
            child2.setMaxDepth(gene.getMaxDepth());
        } else {
            child1.setMaxDepth(gene.getMaxDepth());
            child2.setMaxDepth(this.getMaxDepth());
        }

        return Arrays.asList(child1, child2);
    }

    public void mutate() {
        // this way we have that 1 chromosome out of 3 will be mutated (probabilistically)
        if (random.nextFloat() < 1 / 3) {
            this.setChaseThreshold(getRandomInBounds());
        }
        if (random.nextFloat() < 1 / 3) {
            this.setEscapeThreshold(getRandomInBounds());
        }
        if (random.nextFloat() < 1 / 3) {
            this.setMaxDepth(getRandomInBounds());
        }
    }

    public String getPhenotype() {
        return escapeThreshold + "\t" + chaseThreshold + "\t" + maxDepth;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getEscapeThreshold() {
        return escapeThreshold;
    }

    public void setEscapeThreshold(int escapeThreshold) {
        this.escapeThreshold = escapeThreshold;
    }

    public int getChaseThreshold() {
        return chaseThreshold;
    }

    public void setChaseThreshold(int chaseThreshold) {
        this.chaseThreshold = chaseThreshold;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private int getRandomInBounds() {
        return random.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND - DISTANCE_THRESHOLD_LOWER_BOUND) + DISTANCE_THRESHOLD_LOWER_BOUND;
    }

    @Override
    public int compare(Gene o1, Gene o2) {
        return o1.getFitness() - o2.getFitness();
    }

    @Override
    public int compareTo(Gene o) {
        return compare(this, o);
    }

    public static Gene getRandomizedGene() {
        Gene gene = new Gene();
        gene.randomiseChromosome();
        return gene;
    }
}
