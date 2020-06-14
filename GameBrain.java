package blackjack;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;

/**
 * 
 * @author Benton
 * Task List:
 * - Add money and wagering.
 *   - Money Variable
 *   - Win wager
 *   - What happens when you lose all your money.
 */
public class GameBrain implements Initializable {

	
	@FXML protected Button btnPlay;
	@FXML private Button btnDeck;

	@FXML private ImageView dealer_card1;
	@FXML private ImageView dealer_card2;
	@FXML private ImageView dealer_card3;
	@FXML private ImageView dealer_card4;
	@FXML private ImageView dealer_card5;
	
	@FXML private ImageView player_card1;
	@FXML private ImageView player_card2;
	@FXML private ImageView player_card3;
	@FXML private ImageView player_card4;
	@FXML private ImageView player_card5;
	
	@FXML private Label lblUserTotal;
	@FXML private Label lblDealerTotal;
	 
	// Keeps a array of cards which have been used.
	private List<Integer> chosenNumbers;
	private User user;
	private User dealer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Fetching Data...");
		chosenNumbers = new ArrayList<Integer>();
		user = new User();
		dealer = new User();
		
		btnDeck.setDisable(true);
	}

	/**
	 * This function starts the game by dealing 2 cards to users and 1 to dealer.
	 */
	public void startGame() {

		btnDeck.setDisable(false); btnPlay.setDisable(true);
		
		// Game starts by having user and dealer flip 2 cards.
		for(int i = 0; i < 2; i++) {
			int usercards = getRandomNumber();
			
			try {
				if(i == 1)
					player_card2.setImage(new Image(getClass().getResourceAsStream("../Assets/card" + Integer.toString(usercards) + ".png")));
				else {
					int dealercards = getRandomNumber();
					
					player_card1.setImage(new Image(getClass().getResourceAsStream("../Assets/card" + Integer.toString(usercards) + ".png")));
					dealer_card1.setImage(new Image(getClass().getResourceAsStream("../Assets/card" + Integer.toString(dealercards) + ".png")));
				
					dealer.addRandomCard(dealercards);
				
				}
				user.addRandomCard(usercards);
				
				user.generateTotal();
				dealer.generateTotal();
				displayNewTotals();
				
			} catch (Exception e) {
				print("Error: " + e);
			}
			
		}
		if(user.getCardTotal() > 21) {
			userAlertDialog(0, true);
		}
	}
	
	public void stand() {
		btnDeck.setDisable(true);
		
		for(int i = dealer.getRevealCount(); i < 5; i++) {
			int cardTotal = dealer.getCardTotal();
			
			if(cardTotal < 21) {
				if(cardTotal < 18) {
					int dealercards = getRandomNumber();
					dealer.addRandomCard(dealercards);
					
					Image cards = new Image(getClass().getResourceAsStream("../Assets/card" + Integer.toString(dealercards) + ".png"));
					
					switch(dealer.getRevealCount()) {
						case 2: {
							dealer_card2.setImage(cards);
							break;
						}
						case 3: {
							dealer_card3.setImage(cards);
							break;
						}
						case 4: {
							dealer_card4.setImage(cards);
							break;
						}
						case 5: {
							dealer_card5.setImage(cards);
							break;
						}
					}
					
					dealer.generateTotal();
					displayNewTotals();
				}
			}
		}
		
		if(user.getCardTotal() == dealer.getCardTotal())
			userAlertDialog(2, false);
		else
			userAlertDialog(didUserWin() ? 1 : 0 , false);
		
	}
	
	public boolean didUserWin() {
		
		final int dealerCardValues = dealer.getCardTotal(), userCardValues = user.getCardTotal();
		
		if(userCardValues > 21) return false;
		else 
			if(dealerCardValues > 21) return true;
			else 
				if(userCardValues > dealerCardValues) return true;
				else
					return false;
	}
	
	public void onDeckClick() {
		
		if(user.getRevealCount() < 5) {
			
			if(user.getCardTotal() < 21) {
				
				int number = getRandomNumber();
				int usernum = getRandomNumber();

				user.addRandomCard(usernum);	
				
				/** Card Symbol Order
				 *  Spade: 1 - 13, Diamond: 14 - 26, Clubs: 27 - 39, Hearts: 40 - 52
				 */
				Image cards = new Image(getClass().getResourceAsStream("../Assets/card" + Integer.toString(usernum) + ".png"));
				
				switch(user.getRevealCount()) {
					case 3: {
						player_card3.setImage(cards);
						break;
					}
					case 4: {
						player_card4.setImage(cards);
						break;
					}
					case 5: {
						player_card5.setImage(cards);
						break;
					}
				}
				user.generateTotal();
				displayNewTotals();
				
				if(user.getCardTotal() > 21)
					userAlertDialog(0, true);
				else if(user.getCardTotal() == 21)
					stand();
			}
		}
		else {
			print("No more card to distribute...");
			stand();
		}
		
	}
	
	public final void userAlertDialog(final int dialog, final boolean bust) {
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Game Results!!!");
		alert.setHeaderText(null);
		
		ButtonType btnAgain = new ButtonType("Play Again", ButtonBar.ButtonData.YES);
		ButtonType btnQuit = new ButtonType("Quit", ButtonBar.ButtonData.NO);
		
		if(bust == true) {
			
			alert.setContentText("BUST");
			alert.getButtonTypes().setAll(btnAgain, btnQuit);
		}
		else {
			// dialog 1 is for win, dialog 2 is for loss
			if(dialog == 1) {
				alert.setContentText("You Win!!!");
				alert.getButtonTypes().setAll(btnAgain, btnQuit);
			}
			else if(dialog == 2) {
				alert.setContentText("TIE!!!!");
				alert.getButtonTypes().setAll(btnAgain, btnQuit);
			}
			else {
				alert.setContentText("You Lost :(");
				alert.getButtonTypes().setAll(btnAgain, btnQuit);
			}
		}
		
		alert.showAndWait().ifPresent(type -> {

			if(type.getButtonData() == ButtonBar.ButtonData.YES)
				restart();
			else 
				System.exit(0);
		});
	}

	private int getRandomNumber() {
		
		int randomNumber = 0;
		boolean usedNumber = false;
		// Logger logger = Logger.getLogger(GameBrain.class.getName());

		while (usedNumber == false) {
			
			randomNumber = (int) ((Math.random() * (( 52 - 1 ) + 1)) + 1);
			usedNumber = true;

			if(chosenNumbers.size() > 0) 
				for(int i = 0; i < chosenNumbers.size(); i++)
					if(chosenNumbers.get(i) == randomNumber)
						usedNumber = false;
		}
		
		chosenNumbers.add(randomNumber);
		return randomNumber;
	}	
	
	private void displayNewTotals() {
		lblUserTotal.setText(Integer.toString(user.getCardTotal()));
		lblDealerTotal.setText(Integer.toString(dealer.getCardTotal()));
	}

	public void restart() {
		user = new User();
		dealer = new User();
		chosenNumbers = new ArrayList<Integer>();
		
		btnPlay.setDisable(false);
		btnDeck.setDisable(true);
		
		Image img = new Image(getClass().getResourceAsStream("../Assets/face_down.png"));
		

		player_card1.setImage(img); player_card2.setImage(img);
		player_card3.setImage(img); player_card4.setImage(img);
		player_card5.setImage(img);
		
		dealer_card1.setImage(img); dealer_card2.setImage(img);
		dealer_card3.setImage(img); dealer_card4.setImage(img);
		dealer_card5.setImage(img);
		
		lblDealerTotal.setText("Dealer: ");
		lblUserTotal.setText("User: ");
		
	}
	public void exit() { System.exit(0); }
	private void print(String message) { System.out.println(message); }
}
