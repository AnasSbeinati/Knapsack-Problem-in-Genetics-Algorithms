import java.util.ArrayList;

import com.sun.javafx.image.impl.ByteIndexed.ToIntArgbAnyConverter;

public class Sol {
	public String chromosome;
	public int benefits = 0;
	public int weight = 0;
	public int dec;
	public int comu=0;
    public boolean isSelected=false;
	public void calculateBen(ArrayList<Item> items) {
		this.benefits = 0;
		for (int i = 0; i < chromosome.length(); i++) {
			if (chromosome.charAt(i) == '1') {
				this.benefits += items.get(i).benefit;
			}
		}
	}
	private static int toInt(String str) {
		int com = 0;
		int strLen = str.length();
		for (int i = 0; i <strLen; i++) {
			if (str.charAt(i) == '1') {
				com+=Math.pow(2, strLen-i-1);
			}
		}
		return com;
	}
	public void binToInt() {
		this.dec = toInt(chromosome);
	}

	public int calculateWeight(ArrayList<Item> items, String ch) {
		int weight = 0;
		for (int i = 0; i < ch.length(); i++) {
			if (ch.charAt(i) == '1') {
				weight += items.get(i).weight;
			}
		}
		return weight;
	}
}
