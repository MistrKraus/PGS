class Bariera {
	private int pocetVlaken;

	private double suma = 0;
	private int citac = 0;
	private boolean uspat = true;

	public Bariera(int pocetVlaken) {
		this.pocetVlaken = pocetVlaken;
	}

	public synchronized void synchronizuj(Vlakno v) {
//		suma += v.getSoucet();
		citac++;

		// okamzik synchronizace
		if (citac == pocetVlaken) {
//			System.out.println("Cislo pi po souctu " + v.getAktualniClen() * pocetVlaken + " prvku = " + suma);

			citac = 0;
//			suma = 0;

			notifyAll();
		} else {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void synchronizuj2(Vlakno v) {
//		suma += v.getSoucet();

		while (!uspat) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		citac++;

		if (citac == pocetVlaken) {
			System.out.println("Cislo pi po souctu " + v.getAktualniClen() * pocetVlaken + " prvku = " + suma);

			suma = 0;

			uspat = false;

			notifyAll();
		}

		while (uspat) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		citac--;

		if (citac == 0) {
			uspat = true;

			notifyAll();
		}
	}
}

class Vlakno extends Thread {
	private int cisloVlakna;
	private int pocetClenu;
	private double soucet;
	private int aktualniClen = 0;	
	private int pocetVlaken;

	private Bariera b;
	private int synch;

	Vlakno(int cisloVlakna, int pocetClenu, int pocetVlaken, int synch, Bariera bariera) {
		this.cisloVlakna = cisloVlakna;
		this.pocetClenu = pocetClenu;
		this.pocetVlaken = pocetVlaken;

		this.synch = synch;
		this.b = bariera;
	}
	
	public double getSoucet() {
		return this.soucet;
	}

	public int getAktualniClen() {
		return aktualniClen;
	}

	//	public void secti() {
	public void run() {
		int index;
				
		soucet = Math.pow(-1, cisloVlakna) / (2 * cisloVlakna + 1);
		aktualniClen++;

//		if (aktualniClen % synch == 0)
//			b.synchronizuj(this);
		
		while (aktualniClen < pocetClenu) {
			index = cisloVlakna + pocetVlaken * aktualniClen;
			soucet += Math.pow(-1, index) / (2 * cisloVlakna + 1 + 2 * pocetVlaken * aktualniClen);

			aktualniClen++;
//			if (aktualniClen % synch == 0) {
//				b.synchronizuj(this);
//			}
		}

		b.synchronizuj(this);

		System.out.println("Vlakno " + cisloVlakna + " ma soucet: " + soucet);
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

		double suma = 0;

		Bariera b = new Bariera(pocetVlaken);
		Vlakno v[] = new Vlakno[pocetVlaken];

		for (int i = 0; i < pocetVlaken; i++) {
			v[i] = new Vlakno(i, pocetClenu, pocetVlaken, 20, b);
			v[i].start();
		}

		for (int i = 0; i < pocetVlaken; i++) {
			try {
				v[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			suma += v[i].getSoucet();
		}
				
		System.out.println("Vysledne cislo pi: " + 4 * suma);
	}
}
