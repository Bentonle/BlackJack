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

	private List<Integer> userNumbers; // List of card numbers the user has in hand.
	private int cardRevealCount; // Counts the # of cards the player has drawn.
	
	private int money;
	private int total; // Holds the cards in hand total.
	private int wager;
	
	// private ImageView[] imgCards; // Store Reference of FXML image element.
	
	User(){
		total = 0;
		userNumbers = new ArrayList<Integer>();
		cardRevealCount = 0;
		money = 100;
		wager = 0;
		// imgCards = new ImageView[5];
	}
	
	
	public void addRandomCard(int number) { 
		
		userNumbers.add(number); 
		cardRevealCount++;
	}
	
	/*public void setImages(ImageView tempImage) {
		
	}*/
	private int cardNumberAdjust(int number) { return (number % 13); }
	
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
	public int getWager() { return this.wager; }
	public void setWager(int wager) { this.wager = wager; } 
	
	public int getCardTotal() { return this.total; }
	
	public int getMoneyCount() { return this.money; }
	public void changeAmountMoney(final int money) { this.money = money; }
	
	// public String printList() { return Arrays.toString(userNumbers.toArray()); }
}
