package ec.edu.upse.facsistel.ai.genetic_algorithm;

public interface Problem {
	public void instantiateProblem();
	public int determineProblemSize();
	public void setAlphabet(Alphabet alphabet);
	public Alphabet getAlphabet();
}
