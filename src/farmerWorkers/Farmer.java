package farmerWorkers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class Farmer {
	private TreeSet<WordRecord> wordRecords;
	private BufferedReader in;
	private int jokeID = 1;
	
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
	
	public void compute() {
		
		int i;	
		
		int jobCount = 0;
		String joke;
		
		TreeSet<WordRecord> results;
		
		System.out.println("Zacinam makat.");		
		
		while ((joke = getJoke()).equals("$$skonci$$") == false) {
			System.out.println("Dostal jsem praci.");
			
			results = new TreeSet<WordRecord>(new WRComparer());
			
			String[] words = joke.split("[^A-Za-z0-9]+");
						
			for (i = 0; i < words.length; i++) {
							
				// prevedeni slova na mala pismena
				words[i] = words[i].toLowerCase();
				
				// zpracovani slova
				WordRecord wr = getWordRecord(words[i], results);
				
				if (wr == null) { // nove slovo ve vtipu
					wr = new WordRecord();
					wr.word = words[i];
					wr.frequency = 1;										
				}
				else 
				{ // slovo se jiz ve vtipu vyskytlo
					results.remove(wr);
					wr.frequency++;
				}
				results.add(wr);
				
				System.out.println("Zpracovano slovo: " + " / " + wr.word + " /");				
			}
			// ulozeni vysledku do globalniho seznamu u farmera
			reportResult(results);
			jobCount++;
		}
		System.out.println("Koncim, zpracoval jsem " + jobCount + " vtipu.");
		
		System.out.println("Tisknu vysledek.");
		printResult();
	}
		
	// prideleni prace workerovi
	public String getJoke() {
		
		String jokeText = "";
		String line;
		
		System.out.println("Zadam vtip.");
		
		try {
			while ((line = in.readLine()) != null) {
				if (line.trim().equals("%")) {
					System.out.println("Vtip nacten (" + jokeID + ").");
					jokeID++;					
					return jokeText.trim();
				}
				jokeText += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Neni co cist.");
		return "$$skonci$$";
	}
	
	// ukladani vysledku z jednoho vtipu do wordRecords
	public void reportResult(TreeSet<WordRecord> results) {
		System.out.println("Pridavam vysledky.");
		
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
	public WordRecord getWordRecord (String item, TreeSet<WordRecord> results) {
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
