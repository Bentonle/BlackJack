package blackjack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

public class User {

	private int total;
	private List<Integer> userNumbers;
	private int cardRevealCount;
	
	public ImageView[] imgCards = new ImageView[5];
	
	User(){
		total = 0;
		userNumbers = new ArrayList<Integer>();
		cardRevealCount = 0;
	}
	
	
	public void addRandomCard(int number) { 
		
		userNumbers.add(number); 
		cardRevealCount++;
	}
	public int getRevealCount() { return this.cardRevealCount; }
	public List<Integer> getUserNumbers() { return this.userNumbers; }
	
	public void generateTotal() {
		
		this.total = 0;
		
		for(int i = 0; i < userNumbers.size(); i++) {
			
			int numberAdjust = cardNumberAdjust(userNumbers.get(i));
			if (numberAdjust == 0)
				numberAdjust = 13;
			
			this.total += numberAdjust;
		}
	}
	public int getCardTotal() { return this.total; }
	private int cardNumberAdjust(int number) { return (number % 13); }
	
	public String printList() { return Arrays.toString(userNumbers.toArray()); }
}
