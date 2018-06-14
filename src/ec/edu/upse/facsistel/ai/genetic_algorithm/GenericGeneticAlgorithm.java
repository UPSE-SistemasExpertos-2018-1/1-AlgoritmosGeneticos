package ec.edu.upse.facsistel.ai.genetic_algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class GenericGeneticAlgorithm {

	protected final int chromosomeSize;
	protected final int populationSize;
	protected final int sampleSize;
	protected final int maxGenerations;
	protected final double targetIndividualFitness;
	//private Chromosome chromosome;
	protected Problem problem;
	protected double bestIndividualFitness = 0;

	//La probabilidad de mutacion debe ser baja
	//https://www.researchgate.net/post/Why_is_the_mutation_rate_in_genetic_algorithms_very_small
	//Es un caso de explore vs exploit. 
	//Grandes valores para la probabilidad de mutacion evitan que converga el GA.
	private double mutationProbability = 0.1; 
	
	protected int noImprovementGenerationLimit = Integer.MAX_VALUE;
	
	public double getBestIndividualFitness() {
		return bestIndividualFitness;
	}

	public void setBestIndividualFitness(double bestIndividualFitness) {
		this.bestIndividualFitness = bestIndividualFitness;
	}

	public int getNoImprovementGenerationLimit() {
		return noImprovementGenerationLimit;
	}

	public void setNoImprovementGenerationLimit(int noImprovementGenerationLimit) {
		this.noImprovementGenerationLimit = noImprovementGenerationLimit;
	}

	public int getMaxGenerations() {
		return maxGenerations;
	}

	public double getTargetIndividualFitness() {
		return targetIndividualFitness;
	}

	private Random r = new Random();
	
	
//	public GenericGeneticAlgorithm(int populationSize, int sampleSize, int chromosomeSize)
//	{
//		this.populationSize = populationSize;
//		this.sampleSize = sampleSize;
//		this.chromosomeSize = chromosomeSize;
//		
//	}
	
	public GenericGeneticAlgorithm(int populationSize, int sampleSize, Problem problem)
	{
		this.populationSize = populationSize;
		this.sampleSize = sampleSize;
		this.chromosomeSize = problem.determineProblemSize();
		this.problem = problem;
		this.maxGenerations = 100;
		this.targetIndividualFitness = Double.MAX_VALUE;
	}
	
	public GenericGeneticAlgorithm(int populationSize, int sampleSize, Problem problem, int maxGenerations, double targetFitness)
	{
		this.populationSize = populationSize;
		this.sampleSize = sampleSize;
		this.chromosomeSize = problem.determineProblemSize();
		this.problem = problem;
		this.maxGenerations = maxGenerations;
		this.targetIndividualFitness = targetFitness;
	}
	
	
	public abstract List<Chromosome> initialization();
	
	//1. Inicializacion al Random
	public List<Chromosome> initializationRamdomlyWithReplacement() 
	{
		List<Chromosome> initialPopulation = new ArrayList<Chromosome>();
		
		for(int i = 0; i<sampleSize; i++)
		{
			//Un factory para hacerlo mas generico y aceptar cualquier clase de Chromosoma.
			Chromosome randomChromosome = ChromosomeFactory.getChromosome(problem); 
			randomChromosome.initializeRandomly();
			initialPopulation.add(randomChromosome);
		}
		return initialPopulation;
	}

	//2. Evaluation: Depende de funcion interna de evaluacion (esta dentro del cromosoma).
	public List<Chromosome> evaluation(List<Chromosome> initialPopulation)
	{
		List<Chromosome> evaluatedPopulation = new ArrayList<Chromosome>();
		for(Chromosome c: initialPopulation)
		{
			double fitness = c.evaluateFitness();
			if(fitness>bestIndividualFitness)
			{
				bestIndividualFitness = fitness;
			}
			evaluatedPopulation.add(c);
		}
		return evaluatedPopulation;
	}

	//Implementar al menos 2 metodos de seleccion.
	public abstract List<Chromosome> selection(List<Chromosome> evaluatedPopulation);
	
	public List<Chromosome> selectionRoulette(List<Chromosome> evaluatedPopulation)
	{
		List<Chromosome> selectedPopulation = new ArrayList<Chromosome>();
		for(int i = 0; i<this.sampleSize; i++)
		{
			Chromosome selectedChromosome = rouletteSingleSelection(evaluatedPopulation).clone();
			selectedPopulation.add(selectedChromosome);
		}
		return selectedPopulation;
	}
	
	public List<Chromosome> selectionElitism_KBetter(List<Chromosome> evaluatedPopulation, int k)
	{
		List<Chromosome> selectedPopulation = new ArrayList<Chromosome>();
		
		Collections.sort(evaluatedPopulation);
		Collections.reverse(evaluatedPopulation);
		int elitistIndex = 0;
		for(int i = 0; i<evaluatedPopulation.size(); i++)
		{
			if(i==k)
			{
				elitistIndex=0;
			}
			Chromosome selectedChromosome = evaluatedPopulation.get(elitistIndex).clone();
			
			selectedPopulation.add(selectedChromosome);
			elitistIndex++;
		}
		return selectedPopulation;
	}

	private Chromosome rouletteSingleSelection(List<Chromosome> evaluatedPopulation) {
		double fsum = 0;
		for(Chromosome c: evaluatedPopulation)
		{
			fsum = c.getFitness() + fsum;
		}
		
		int tempSelectedIndividual = 0;
		if(evaluatedPopulation.size()>0)
		{
			tempSelectedIndividual = r.nextInt(evaluatedPopulation.size());
		}else {
			System.err.println("Evaluated Population must be > 0");
		}
		
		double tempSum = 0;
		int selectedIndividual = tempSelectedIndividual;
		do {
			if(selectedIndividual<evaluatedPopulation.size())
			{
				Chromosome cTemp = evaluatedPopulation.get(selectedIndividual);
				tempSum = cTemp.getFitness() + tempSum;
				selectedIndividual++;
				if(selectedIndividual==evaluatedPopulation.size())
				{
					selectedIndividual--;
				}
			}else {
				selectedIndividual = 0;
			}
		}while(tempSum>fsum);
		
		Chromosome selectedByRoulette = evaluatedPopulation.get(selectedIndividual);
		return selectedByRoulette;
	}

	public abstract List<Chromosome> crossover(List<Chromosome> selectedChromosomes);

	//Implementar minimo 3 metodos de crossOver (no incluye KPoint)
	public List<Chromosome> crossover(List<Chromosome> selectedChromosomes, CrossoverMechanism mechanism)
	{
		List<Chromosome> crossOverPopulation = new ArrayList<Chromosome>();
		switch (mechanism) {
		case K_POINT:
			int k = r.nextInt((selectedChromosomes.get(0).getAlphabet().getSize()/2)-1);
			crossOverPopulation = crossover_KPoint(selectedChromosomes, k);
			break;
		case UNIFORM:
			//TODO Implementar Uniform CrossOver
			break;
		default:
			break;
		}
		return crossOverPopulation;
	}
	
	public List<Chromosome> crossover_KPoint(List<Chromosome> selectedChromosomes, int k)
	{
		List<Chromosome> crossOverPopulation = new ArrayList<Chromosome>();
		do {
			Chromosome parent1 = selectParent(selectedChromosomes).clone();
			Chromosome parent2 = selectParent(selectedChromosomes).clone();

			Set<Integer> crossOverPoints = new HashSet<Integer>();

			do {
				int crossOverPoint = r.nextInt(parent1.getSize()) ;
				crossOverPoints.add(crossOverPoint);
			}while(crossOverPoints.size()<k);

			List<Chromosome> children = doCrossOver(parent1, parent2, crossOverPoints);
			crossOverPopulation.addAll(children);
		}while(crossOverPopulation.size()<selectedChromosomes.size());
		return crossOverPopulation;
	}

	private List<Chromosome> doCrossOver(Chromosome parent1, Chromosome parent2, Set<Integer> crossOverPoints) {
		List<Chromosome> children = new ArrayList<Chromosome>();

		Chromosome child1 = parent1.clone();
		Chromosome child2 = parent2.clone(); 

		ArrayList<Integer> crossOverPointsOrdered = new ArrayList<Integer>();
		crossOverPointsOrdered.addAll(crossOverPoints);
		Collections.sort(crossOverPointsOrdered);
		boolean cambio = false;

		for(int i = 0; i<parent1.getChromosome().length; i++){

			if(!crossOverPointsOrdered.isEmpty()) 
			{
				if(i==crossOverPointsOrdered.remove(0))
				{
					cambio = (cambio?false:true);
				}
			}
			
			if(cambio)
			{
				child1.getChromosome()[i] = parent2.getChromosome()[i]; 
				child2.getChromosome()[i] = parent2.getChromosome()[i]; 
			}
		}

		children.add(child1);
		children.add(child2);


		return children;
	}

	private Chromosome selectParent(List<Chromosome> parents) {
		Chromosome parent = parents.get(r.nextInt(parents.size())).clone();
		return parent;
	}

	public abstract List<Chromosome> mutation(List<Chromosome> crossOverPopulation);
	
	public List<Chromosome> mutationBitFlip(List<Chromosome> crossOverPopulation) throws WrongMutationOperatorException
	{
		if(problem.getAlphabet().getSize()>2)
		{
			throw new WrongMutationOperatorException("This operator only acts over a problem with a Binary Alphabet.");
		}

		List<Chromosome> mutatedPopulation = new ArrayList<Chromosome>();
		
		for(Chromosome c: crossOverPopulation)
		{
			c.mutate(mutationProbability);
			mutatedPopulation.add(c.clone());
		}
		
		return mutatedPopulation;
	}
	
	public List<Chromosome> executeGeneticAlgorithm()
	{
		int generations = 0;
		double improvement = Double.MAX_VALUE;
		double previousGenerationMediumFitness = 0;
		double actualGenerationMediumFitness = 0;
		int noImpromentCounter = 0;
		 List<Chromosome> initialPopulation = initialization();
		do {
			 List<Chromosome> evaluatedPopulation = evaluation(initialPopulation);
			 //Actualizar bestFitness
			 List<Chromosome> selectedPopulation = selection(evaluatedPopulation);
			 List<Chromosome> crossOverPopulation = crossover(selectedPopulation);
			 List<Chromosome> mutatedPopulation = mutation(crossOverPopulation);
			 initialPopulation = mutatedPopulation;
			 generations++;
			 
			 actualGenerationMediumFitness = Chromosome.calculatePopulationMediumFitness(mutatedPopulation);
			 improvement = actualGenerationMediumFitness - previousGenerationMediumFitness; 
			 previousGenerationMediumFitness = actualGenerationMediumFitness;
			 
			 //If no improvement in X Generations, terminate
			 if(improvement==0)
			 {
				 noImpromentCounter++;
			 }else{
				 noImpromentCounter = 0;
			 }
			 
		}while(generations<maxGenerations && bestIndividualFitness>targetIndividualFitness && noImpromentCounter<noImprovementGenerationLimit);
		if(generations>=maxGenerations)
		{
			System.out.println("Stopped cause max number of Generations reached.");
		}
		
		if(bestIndividualFitness>=targetIndividualFitness)
		{
			System.out.println("Stopped cause target individual fitness was reached.");
		}
		
		if(noImpromentCounter>=noImprovementGenerationLimit)
		{
			System.out.println("Stopped cause there was no improment in " + noImprovementGenerationLimit + "generations");
		}
		
		System.out.println("Solution found in " + generations + "generations");
		return initialPopulation;
	}

	public int getChromosomeSize() {
		return chromosomeSize;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public int getSampleSize() {
		return sampleSize;
	}
	
	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

}
