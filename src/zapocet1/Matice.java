package zapocet1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Matrix {
	private int size;
	private int matice[][];
	private BufferedReader in;

	Matrix (String file) {
		FileReader fr;
		try {
			fr = new FileReader(file);
			in = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		switch (file) {
		case "matrixS.dat":
			size = 10;
			break;
		case "matrixM.dat":
			size = 100;
			break;
		case "matrixL.dat":
			size = 1000;
			break;
		}

		matice = new int[size][size];
	}

	public void nacti() {

		String line = null;
		String cisla[] = null;

		for (int i = 0; i < size; i++) {
			try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			cisla = line.split("\t");

			for(int j = 0; j < size; j++) {
				matice[i][j] = Integer.parseInt(cisla[j]);
			}
		}
	}

	public int minHodnota() {

		int min = matice[0][0];

		for (int i = 0; i < size; i++) {
			for (int j = 0 ; j < size; j ++) {
				if (min > matice[i][j]) min = matice[i][j];
			}
		}

		return (min);
	}

	public int maxHodnota() {

		int max = matice[0][0];

		for (int i = 0; i < size; i++) {
			for (int j = 0 ; j < size; j ++) {
				if (max < matice[i][j]) max = matice[i][j];
			}
		}

		return (max);
	}

	public double prumHodnota() {

		double prumer;
		long suma = 0;

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				suma += matice[i][j];
			}
		}

		prumer = suma / (size * size);

		return (prumer);
	}

}

public class Matice {
	
	final static String FILE_NAME = "matrixM.dat";
	
	public static void main(String[] args) {
		
		Matrix m1 = new Matrix(FILE_NAME);
		
		m1.nacti();
		
		System.out.println("Minimalni hodnota: " + m1.minHodnota());
		System.out.println("Maximalni hodnota: " + m1.maxHodnota());
		System.out.println("Prumerna hodnota: " + m1.prumHodnota());
	}
}
