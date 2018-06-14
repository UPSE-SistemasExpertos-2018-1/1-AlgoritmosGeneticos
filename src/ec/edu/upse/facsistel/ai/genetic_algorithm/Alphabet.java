package ec.edu.upse.facsistel.ai.genetic_algorithm;

public abstract class Alphabet {
	private final int size;
	private char[] letters; 
	private char defaultLetter;
	
	public Alphabet(int size)
	{
		this.size = size;
		letters = new char[size];
	}
	
	public Alphabet(char[] letters) 
	{
		this.size = letters.length;
		this.letters = letters;
		if(size>0)
		{
			defaultLetter = letters[0];
		}
	}
	
	public Alphabet(char[] letters, char defaultLetter) 
	{
		this.size = letters.length;
		this.letters = letters;
		this.defaultLetter = defaultLetter;

	}

	private void chooseFirstLetterAsDefaultAlphabetLetter(char[] letters) throws EmptyAlphabetException {
		if(letters.length>0)
		{
			defaultLetter = letters[0];
		}else {
			throw new EmptyAlphabetException();
		}
	}
	
	public int getSize() {
		return size;
	}

	public char[] getLetters() {
		return letters;
	}

	public void setLetters(char[] letters) {
		this.letters = letters;
	}

	public char getDefaultLetter() {
		return defaultLetter;
	}

	public void setDefaultLetter(char defaultLetter) {
		this.defaultLetter = defaultLetter;
	}
	
	
}
