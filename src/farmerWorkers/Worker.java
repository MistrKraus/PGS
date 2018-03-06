package farmerWorkers;

import java.util.TreeSet;

public class Worker extends Thread {
    private String jmeno;
    private Farmer farmer;
    private TreeSet<WordRecord> results;
    private int speed;

    public Worker(String jmeno, Farmer farmer, int speed) {
        this.jmeno = jmeno;
        this.farmer = farmer;
        this.speed = speed;
    }

    public void run() {
        int jobCount = 0;
        String joke;

        System.out.println(jmeno + ": Zacinam makat.");

        while (!(joke = farmer.getJoke(jmeno)).equals("$$skonci$$")) {
            System.out.println(jmeno + ": Dostal jsem praci.");

            results = new TreeSet<WordRecord>(new WRComparer());

            String[] words = joke.split("[^A-Za-z0-9]+");

            long prevTime = System.currentTimeMillis();

            for (int i = 0; i < words.length; i++) {
                long currTime = System.currentTimeMillis();

                while (currTime < prevTime + speed) {
                    currTime = System.currentTimeMillis();
                }

                // prevedeni slova na mala pismena
                words[i] = words[i].toLowerCase();

                // zpracovani slova
                WordRecord wr = farmer.getWordRecord(words[i], results);

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

                System.out.println(jmeno + ": Zpracovano slovo: " + " / " + wr.word + " /");

                prevTime = currTime;
            }
            // ulozeni vysledku do globalniho seznamu u farmera
            farmer.reportResult(results, jmeno);
            jobCount++;
        }
        System.out.println(jmeno + ": Koncim, zpracoval jsem " + jobCount + " vtipu.");

        System.out.println(jmeno + ": Tisknu vysledek.");
    }

    public String getJmeno() {
        return jmeno;
    }
}
