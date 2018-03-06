package farmerWorkers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class Farmer extends Thread {
	private TreeSet<WordRecord> wordRecords;
	private BufferedReader in;
	private int jokeID = 1;

	private final static int WORKERS = 3;
	private Worker workers[] = new Worker[WORKERS];

	Farmer (String file) {
		
		System.out.println("Farmar - zacinam.");
		
		// nacteni vstupniho souboru
		System.out.println("Farmar - oteviram soubor...");

		try {
			FileReader fr = new FileReader(file);
			in = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// vytvoreni globalniho seznamu vysledku
		wordRecords = new TreeSet<WordRecord>(new WRComparer());
	}
	
//	public void compute() {
    public void run() {

        System.out.println("Farmar: Vyvarim a spoustim delniky");
        for (int i = 0; i < WORKERS; i++) {
            workers[i] = new Worker("Delnik" + (i+1), this, (i + 1) * 100);
            workers[i].start();
            System.out.println("Farmar: " + workers[i].getJmeno() + " spusten");
        }

        System.out.println("Farmar: cekam na ukonceni prace delniku.");
        for (int i = 0; i < WORKERS; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Farmar: Zaviram soubor.");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Farmar: Tisknu vysledek");
        printResult();
//		int jobCount = 0;
//		String joke;
//
//		TreeSet<WordRecord> results;
//
//		System.out.println("Zacinam makat.");
//
//		while (!(joke = getJoke()).equals("$$skonci$$")) {
//			System.out.println("Dostal jsem praci.");
//
//			results = new TreeSet<WordRecord>(new WRComparer());
//
//			String[] words = joke.split("[^A-Za-z0-9]+");
//
//			for (i = 0; i < words.length; i++) {
//
//				// prevedeni slova na mala pismena
//				words[i] = words[i].toLowerCase();
//
//				// zpracovani slova
//				WordRecord wr = getWordRecord(words[i], results);
//
//				if (wr == null) { // nove slovo ve vtipu
//					wr = new WordRecord();
//					wr.word = words[i];
//					wr.frequency = 1;
//				}
//				else
//				{ // slovo se jiz ve vtipu vyskytlo
//					results.remove(wr);
//					wr.frequency++;
//				}
//				results.add(wr);
//
//				System.out.println("Zpracovano slovo: " + " / " + wr.word + " /");
//			}
//			// ulozeni vysledku do globalniho seznamu u farmera
//			reportResult(results);
//			jobCount++;
//		}
//		System.out.println("Koncim, zpracoval jsem " + jobCount + " vtipu.");
//
//		System.out.println("Tisknu vysledek.");
//		printResult();
	}
		
	// prideleni prace workerovi
	public String getJoke(String jmeno) {
		
		String jokeText = "";
		String line;
		
		System.out.println(jmeno + ": Zadam vtip.");
		
		try {
			while ((line = in.readLine()) != null) {
				if (line.trim().equals("%")) {
					System.out.println(jmeno + ": Vtip nacten (" + jokeID + ").");
					jokeID++;					
					return jokeText.trim();
				}
				jokeText += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jmeno + ": Neni co cist.");
		return "$$skonci$$";
	}
	
	// ukladani vysledku z jednoho vtipu do wordRecords
	public synchronized void reportResult(TreeSet<WordRecord> results, String jmeno) {
		System.out.println(jmeno + ": Pridavam vysledky.");
		
		Iterator<WordRecord> it = results.iterator();
		
		while (it.hasNext()) {
			WordRecord listItem = it.next();
			
			WordRecord wr = getWordRecord(listItem.word, wordRecords);
			
			if (wr == null)
				wordRecords.add(listItem);
			else {
				wordRecords.remove(wr);
				wr.frequency += listItem.frequency;
				wordRecords.add(wr);
			}
		}
	}
		
	// ziskani zaznamu slova z results
	public synchronized WordRecord getWordRecord (String item, TreeSet<WordRecord> results) {
		Iterator<WordRecord> it = results.iterator();
		
		while (it.hasNext()) {
			WordRecord listItem = it.next();
			
			if (listItem.word.equals(item))
				return listItem;			
		}		
		return null;
	}
	
	// tisk vysledneho seznamu
	public void printResult() {
		Iterator<WordRecord> it = wordRecords.iterator();
		
		System.out.println("Vysledek:");
		System.out.println("=========");
		
		while (it.hasNext()) {
			WordRecord listItem = it.next();
			System.out.println(listItem.word + " - " + listItem.frequency);
		}
	}
}
