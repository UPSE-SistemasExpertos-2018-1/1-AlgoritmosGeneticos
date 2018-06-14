package ec.edu.upse.facsistel.ai.genetic_algorithm;

import ec.edu.upse.facsistel.ai.genetic_algorithm.knapsack.KnapsackChromosome;
import ec.edu.upse.facsistel.ai.genetic_algorithm.knapsack.KnapsackProblem;
import ec.edu.upse.facsistel.ai.genetic_algorithm.tsp.TSPChromosome;


public class ChromosomeFactory {
	private int size;
	private Alphabet alphabet;
	
	public static Chromosome getChromosome(String specificChromosomeClassName, Problem problem)
	{
		Chromosome fabricatedChromosome = null;
		switch (specificChromosomeClassName) {
		case "ec.edu.upse.facsistel.ai.genetic_algorithm.knapsack.KnapsackChromosome":
			problem.instantiateProblem();
			fabricatedChromosome = new KnapsackChromosome(problem);
			break;
		case "ec.edu.upse.facsistel.ai.genetic_algorithm.knapsack.TSPChromosome":
			//TODO Aqui va la fabricacion de objetos cromosmas para el problema TSP.
			break;
		default:
			break;
		}
		
		return fabricatedChromosome;
	}
	
	public static Chromosome getChromosome(Problem problem)
	{
		Chromosome fabricatedChromosome = null;
		String problemClassName = problem.getClass().getSimpleName();
		System.out.println("Problem type: " + problemClassName);
		switch (problemClassName) {
		case "KnapsackProblem":
			problem.instantiateProblem();
			fabricatedChromosome = new KnapsackChromosome(problem);
			break;
		case "TSPChromosome":
			problem.instantiateProblem();
			//fabricatedChromosome = new TSPChromosome(problem);
			//TODO Aqui va la fabricacion de objetos cromosmas para el problema TSP.
			break;
		default:
			break;
		}
		
		return fabricatedChromosome;
	}
}
