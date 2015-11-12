import java.util.ArrayList;

public class Item {
	public int benefit, weight,comu;
	boolean isSelected=false;
	String chromosom;

	public boolean toChromosom(ArrayList<Item> items,int newbenefit,int chroLen) {
		boolean found=false;
		for (Item item : items) {
			if(item.benefit==newbenefit)
			{
				found=true;
				this.weight=item.weight;
				String temp=Integer.toBinaryString(newbenefit);
				while (temp.length()<chroLen) {
					temp="0"+temp;
				}
				this.chromosom=temp;
				this.benefit=newbenefit;
				break;
			}
		}
		return found;
	}

	public void binToInt(ArrayList<Item> items,String str,int chrLen) {
		int temp=Integer.parseInt(str, 2);
		for (Item item : items) {
			if(item.benefit==temp)
			{
				Item tempItem=new Item();
				tempItem.benefit=this.benefit;
				tempItem.weight=this.weight;
				tempItem.chromosom=this.chromosom;
				
				this.benefit=item.benefit;
				this.weight=item.weight;
				this.chromosom=item.chromosom;
				item=tempItem;
				break;
			}
		}
	}
}
