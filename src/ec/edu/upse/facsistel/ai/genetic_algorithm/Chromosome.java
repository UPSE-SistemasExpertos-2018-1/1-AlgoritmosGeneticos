package ec.edu.upse.facsistel.ai.genetic_algorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public abstract class Chromosome implements Comparable<Chromosome>{
	protected int size;
	protected Alphabet alphabet;

	protected char[] chromosome =  new char[size];
	public double fitness = 0;

	private Random r = new Random();

	protected Problem problem;

	public Chromosome(Problem p)
	{
		this.alphabet = p.getAlphabet();
		this.size = p.determineProblemSize();
		chromosome = new char[size];
		problem = p;
	}

	public Chromosome(int size, Alphabet alphabet)
	{
		this.alphabet = alphabet;
		this.size = size;
		chromosome = new char[size];
	}

	public Chromosome(Chromosome c)
	{
		this.alphabet = c.getAlphabet();
		this.chromosome = Arrays.copyOf(c.getChromosome(),c.getChromosome().length);
		this.size = c.size;
		this.fitness = c.fitness;

	}

	public abstract double evaluateFitness();

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Alphabet getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Alphabet alphabet) {
		this.alphabet = alphabet;
	}


	public char[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(char[] chromosome) {
		this.chromosome = chromosome;
	}

	public void initializeRandomly()
	{
		//Implementen Uds mas claro!
	}

	public void initializeDefaultLetter()
	{
		Arrays.fill(chromosome, alphabet.getDefaultLetter());
	}

	public void initializeWithLetter(char letter) throws LetterNotInAlphabetException
	{
		if(Arrays.binarySearch(alphabet.getLetters(), letter)!=-1)
		{
			Arrays.fill(chromosome, letter);
		}else {
			throw new LetterNotInAlphabetException("Letra no esta en el alfabeto");
		}
	}

	@Override
	public abstract Chromosome clone();
	

	public Chromosome clone(Chromosome otherChromosome, Chromosome chromosomeToReturn)
	{
		chromosomeToReturn.problem = otherChromosome.problem;
		chromosomeToReturn.fitness = otherChromosome.fitness;
		chromosomeToReturn.alphabet = otherChromosome.alphabet;
		chromosomeToReturn.size = otherChromosome.size;
		chromosomeToReturn.chromosome = Arrays.copyOf(otherChromosome.getChromosome(), otherChromosome.getChromosome().length);
		return chromosomeToReturn;
	}

	public double getFitness() {
		return fitness;
	}

	public void mutate(double mutationProbability) {
		//Implementen su propia mutacion. 
		//Mas informacion, preguntar en la entrada de la 8.
	}
	
	@Override
	public int compareTo(Chromosome o) {
		return Double.compare(this.getFitness(),o.getFitness());
	}

	public static double calculatePopulationMediumFitness(List<Chromosome> population)
	{
		double mediumFitness = population.stream().mapToDouble(c -> c.getFitness()).sum();
		return mediumFitness;
	}
}
