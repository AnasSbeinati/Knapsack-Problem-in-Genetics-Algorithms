import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {

	public static void main(String[] args) {
		FileReader fr;
		BufferedReader br = null;
		FileWriter fw;
		BufferedWriter bw=null;
		String newline = System.getProperty("line.separator");
		try {
			fr = new FileReader("input_exp.txt");
			br = new BufferedReader(fr);
			fw=new FileWriter("input_exp_answer.txt");
			bw=new BufferedWriter(fw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int testcasesNum = 0;
		try {
			testcasesNum = Integer.parseInt(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < testcasesNum; i++) {
			try {
				ArrayList<Item> items = new ArrayList<>();
				String numberOfItemsStr = "";
				while (numberOfItemsStr.equals("")) {
					numberOfItemsStr = br.readLine();
				}
				int numberOfItems = Integer.parseInt(numberOfItemsStr);
				String napsackSizeStr = br.readLine();
				int napsackSize = Integer.parseInt(napsackSizeStr);
				for (int j = 0; j < numberOfItems; j++) {
					Item temp = new Item();
					String str = br.readLine();
					if (!str.equals("")) {
						String strs[] = str.split(" ");
						temp.benefit = Integer.parseInt(strs[1]);
						temp.weight = Integer.parseInt(strs[0]);
						items.add(temp);
					}
				}
				Sol sol = getSol(items, napsackSize, 200);
				System.out.println("Case: " + (i + 1) + " " + sol.benefits);
				bw.write("Case: " + (i + 1) + " " + sol.benefits+newline);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Sol getSol(ArrayList<Item> items, int napsackSize, int iterationsNum) {
		ArrayList<Sol> population = new ArrayList<>();
		// Calculate chromosome length
		int chroLen = items.size();
		// generate first population
		initiateSols(items, napsackSize, population, chroLen);
		for (int i = 0; i < iterationsNum; i++) {
			int popSize = population.size() / 2;
			for (int j = 0; j < popSize; j++) {
				// select tow chromosoms
				ArrayList<Sol> selectedItem = select(population, chroLen);
				// crossover
				crossOver(items, selectedItem.get(0), selectedItem.get(1), chroLen, napsackSize);
				// Mutate
				for (Sol sol : selectedItem) {
					mutate(items, sol, .1f, chroLen, napsackSize);
				}
				// add new sol
				addNewSol(population, selectedItem);
			}
			Collections.sort(population, new Comparator<Sol>() {
				@Override
				public int compare(Sol sol1, Sol sol2) {
					if (sol1.benefits > sol2.benefits)
						return -1;
					else if (sol1.benefits < sol2.benefits)
						return +1;
					else
						return 0;
				}
			});
			int t = popSize * 2;
			int size = population.size();
			for (int j = t; j < size; j++) {
				population.remove(population.size() - 1);
			}
		}
		// the end result is in population
		return population.get(0);
	}

	private static void initiateSols(ArrayList<Item> items, int napsackSize, ArrayList<Sol> population, int chroLen) {
		int all = (int) Math.pow(2, chroLen);
		int solNu = 0;
		while (solNu < 200) {
			int ran = (int) (Math.random() * (all - 1) + 1);
			boolean found = false;
			for (Sol sol : population) {
				if (sol.dec == ran) {
					found = true;
					break;
				}
			}
			//if (found)
				//continue;
			Sol sol = new Sol();
			String chromosome = Integer.toBinaryString(ran);
			int weight = sol.calculateWeight(items, chromosome);
			if (weight <= napsackSize) {
				while (chroLen > chromosome.length())
					chromosome = "0" + chromosome;
				sol.chromosome = chromosome;
				sol.dec = ran;
				sol.weight = weight;
				sol.calculateBen(items);
				population.add(sol);
				solNu++;
			}
		}
	}

	private static ArrayList<Sol> select(ArrayList<Sol> population, int chromLen) {
		Collections.sort(population, new Comparator<Sol>() {
			@Override
			public int compare(Sol sol1, Sol sol2) {
				if (sol1.benefits > sol2.benefits)
					return 1;
				else if (sol1.benefits < sol2.benefits)
					return -1;
				else
					return 0;
			}
		});
		int maxCom = 0;
		for (int j = 0; j < population.size(); j++) {
			maxCom += population.get(j).benefits;
			population.get(j).comu = maxCom;
		}
		ArrayList<Sol> selctedItems = new ArrayList<>();
		int i = 0;
		while (i < 2) {
			float ran = (float) (Math.random() * maxCom);
			for (int j = 0; j < population.size() - 1; j++) {
				if (ran >= population.get(j).comu && ran < population.get(j + 1).comu
						&& !population.get(j).isSelected) {
					Sol temp0 = new Sol();
					temp0.chromosome = population.get(j).chromosome;
					temp0.benefits = population.get(j).benefits;
					temp0.weight = population.get(j).weight;
					population.get(j).isSelected = true;
					selctedItems.add(temp0);
					i++;
				}
			}
			if (i < 2) {
				for (int j = 0; j < 2 - i; j++) {
					Sol temp0 = new Sol();
					temp0.chromosome = population.get(j).chromosome;
					temp0.benefits = population.get(j).benefits;
					temp0.weight = population.get(j).weight;
					population.get(j).isSelected = true;
					selctedItems.add(temp0);
				}
				break;
			}
		}
		return selctedItems;
	}

	private static void crossOver(ArrayList<Item> items, Sol sol1, Sol sol2, int chroLen, int napsackSize) {
		double r = Math.random();
		String temp3 = "", temp4 = "";
		if (r <= .7) {
			int rl = (int) (Math.random() * (chroLen - 2));
			if (rl == 0)
				rl = 1;
			String temp1 = sol1.chromosome.substring(rl, sol1.chromosome.length());
			String temp2 = sol2.chromosome.substring(rl, sol2.chromosome.length());
			temp3 = sol1.chromosome.substring(0, rl);
			temp3 += temp2;
			temp4 = sol2.chromosome.substring(0, rl);
			temp4 += temp1;
			validate(items, sol1, temp3, napsackSize);
			validate(items, sol2, temp4, napsackSize);
		}
	}

	private static void mutate(ArrayList<Item> items, Sol sol, float mutationRate, int chrLen, int napsackSize) {
		StringBuilder sb = new StringBuilder(sol.chromosome);
		for (int i = 0; i < sol.chromosome.length(); i++) {
			double r = Math.random();
			if (r <= mutationRate) {
				if (sb.charAt(i) == '1')
					sb.replace(i, i + 1, "0");
				else
					sb.replace(i, i + 1, "1");
			}
		}
		validate(items, sol, sb.toString(), napsackSize);
	}

	private static void addNewSol(ArrayList<Sol> population, ArrayList<Sol> selectedItem) {

		/*boolean b1 = false, b2 = false;
		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).chromosome.equals(selectedItem.get(0).chromosome)) {
				b1 = true;
			}
			if (population.get(i).chromosome.equals(selectedItem.get(1).chromosome)) {
				b2 = true;
			}
		}

		if (!b1)*/
			population.add(selectedItem.get(0));
		//if (!b2)
			population.add(selectedItem.get(1));
	}

	private static void validate(ArrayList<Item> items, Sol sol, String chrom, int napsackSize) {
		int weight = sol.calculateWeight(items, chrom);
		if (weight <= napsackSize) {
			sol.chromosome = chrom;
			sol.weight = weight;
			sol.calculateBen(items);
			sol.binToInt();
		}
	}

}
