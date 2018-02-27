package cv2;

class Vlakno {
	private int cisloVlakna;
	private int pocetClenu;
	private double soucet;
	private int aktualniClen = 0;	
	private int pocetVlaken;
	
	Vlakno(int cisloVlakna, int pocetClenu, int pocetVlaken) {
		this.cisloVlakna = cisloVlakna;
		this.pocetClenu = pocetClenu;
		this.pocetVlaken = pocetVlaken;
	}
	
	public double getSoucet() {
		return this.soucet;
	}
	
	public void secti() {		
		int index;
				
		soucet = Math.pow(-1, cisloVlakna) / (2 * cisloVlakna + 1);
		aktualniClen++;
		
		while (aktualniClen < pocetClenu) {
			
			index = cisloVlakna + pocetVlaken * aktualniClen;
			soucet += Math.pow(-1, index) / (2 * cisloVlakna + 1 + 2 * pocetVlaken * aktualniClen);
			
			aktualniClen++;
		}
		System.out.println("cv2.Vlakno " + cisloVlakna + " ma soucet: " + soucet);
	}	
}

public class Vlakna {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final int pocetClenu = 1000;
		final int pocetVlaken = 5;
		
		int i;
		double suma = 0;
		
		Vlakno v[] = new Vlakno[pocetVlaken];
		
		for (i = 0; i < pocetVlaken; i++) {
			v[i] = new Vlakno(i, pocetClenu, pocetVlaken);
			v[i].secti();
			suma += v[i].getSoucet();
		}
				
		System.out.println("Vysledne cislo pi: " + 4 * suma);
	}
}
