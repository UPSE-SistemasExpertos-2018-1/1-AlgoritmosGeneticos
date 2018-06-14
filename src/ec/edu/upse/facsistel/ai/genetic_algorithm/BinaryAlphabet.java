package ec.edu.upse.facsistel.ai.genetic_algorithm;


public class BinaryAlphabet extends Alphabet {

	private final static char[] binaryLetters = new char[] {'0','1'};
	public static final Alphabet binaryAlphabet = new BinaryAlphabet(); 
	
	public BinaryAlphabet(){
		super(BinaryAlphabet.binaryLetters);
	}

}
