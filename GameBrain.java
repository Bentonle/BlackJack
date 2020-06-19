package blackjack;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import javafx.scene.control.TextField;

/**
 * 
 * @author Benton Le
 * Date Completed: 06/19/2020
 */
public class GameBrain implements Initializable {

	
	@FXML protected Button btnPlay;
	@FXML private Button btnDeck;
	@FXML private Button btnStand;

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
	@FXML private Label lblTotalMoney;
	@FXML private Label lblPot;
	
	@FXML private TextField txtWager;
	
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
		
		lblTotalMoney.setText("$" + user.getMoneyCount());
		
		resetMenu();
	}
	
	/**
	 * This function starts the game by dealing 2 cards to users and 1 to dealer.
	 */
	public void startGame() {

		if(checkWagerAmount()) {

			txtWager.setDisable(true);
			btnDeck.setDisable(false); btnPlay.setDisable(true);
			btnStand.setDisable(false);
			
			user.setWager(Integer.valueOf(txtWager.getText()));
			
			user.changeAmountMoney(user.getMoneyCount() - user.getWager());
			
			lblTotalMoney.setText("$" + user.getMoneyCount());
			lblPot.setText("$" + (user.getWager() * 2));
			
			
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
		else {
			userAlertDialog(3, false);
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
	
	public void numTextFieldListener() {
		txtWager.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!(txtWager.getText() == null)) {
					if(!newValue.matches("\\d*")) {
						Platform.runLater(() -> {
							txtWager.setText(newValue.replaceAll("[^\\d]", ""));
						});
					}
				}
			}
		});
	}
	
	/**
	 * This function will be constantly executed, to check the amount entered is valid.
	 */
	public boolean checkWagerAmount() {
			
		print(txtWager.getText());
		if(!(txtWager.getText().isEmpty())) {
			
			final int amountEntered = Integer.valueOf(txtWager.getText());
			
			// Removes '0' if user inputs that before numbers.
			txtWager.setText(Integer.toString(amountEntered));
			
			if(amountEntered > user.getMoneyCount() || amountEntered < 1)
				return false;
			else
				return true;
		}
		else
			return false;
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
			
			switch(dialog) {
				// Case 1: If user wins.
				case 1:{
					
					int winnings = user.getWager() * 2;
					user.changeAmountMoney(winnings + user.getMoneyCount());
					lblTotalMoney.setText("$" + user.getMoneyCount());
					
					alert.setContentText("You Win!!!");
					alert.getButtonTypes().setAll(btnAgain, btnQuit);
					break;
				}
				// Case 2: If dealer and user tie.
				case 2: {
					
					user.changeAmountMoney(user.getWager() + user.getMoneyCount());
					lblTotalMoney.setText("$" + user.getMoneyCount());
					
					alert.setContentText("TIE!!!!");
					alert.getButtonTypes().setAll(btnAgain, btnQuit);
					break;
				}
				// Case 3: If wager is not valid.
				case 3: {
					ButtonType btnOk = new ButtonType("OK", ButtonBar.ButtonData.RIGHT);
					
					alert.setContentText("Please enter valid amount");
					alert.getButtonTypes().setAll(btnOk);
					break;
				}
				case 4: {
		
					ButtonType btnDone = new ButtonType("Done", ButtonBar.ButtonData.NO);
					alert.setContentText("Looks like your out of money, better luck next time!");
					alert.getButtonTypes().setAll(btnDone);
					
					break;
				}
				// Default: If user loses.
				default: {
					alert.setContentText("You Lost :(");
					alert.getButtonTypes().setAll(btnAgain, btnQuit);
					break;
				}
			}
		}
		
		alert.showAndWait().ifPresent(type -> {

			if(type.getButtonData() == ButtonBar.ButtonData.YES)
				restart();
			else if(type.getButtonData() == ButtonBar.ButtonData.RIGHT)
				txtWager.setText("");
			else 
				System.exit(0);
		});
	}
	
	/** cardNumberAdjust(int number)
	 * This function returns numbers from 0 - 12.
	 * 0, being King
	 * 12, being Queen
	 * @param number = cards from 1 - 52
	 * @return number = cards from 0 - 12 according to playing card standards.
	 */
	
	private void displayNewTotals() {
		lblUserTotal.setText(Integer.toString(user.getCardTotal()));
		lblDealerTotal.setText(Integer.toString(dealer.getCardTotal()));
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
	

	public void restart() {
		int temp = user.getMoneyCount();
		if(temp != 0) {
			user = new User();
			user.changeAmountMoney(temp);
			
			dealer = new User();
			chosenNumbers = new ArrayList<Integer>();
			
			resetMenu();
			resetImages();
			
			lblDealerTotal.setText("Dealer: ");
			lblUserTotal.setText("User: ");
		}
		else
			userAlertDialog(4, false);
	}
	
	public void resetMenu() {
		txtWager.setDisable(false); txtWager.setText("");
		btnPlay.setDisable(false); btnDeck.setDisable(true);
		btnStand.setDisable(true); lblPot.setText("Pot");
	}
	
	public void resetImages() {
		Image img = new Image(getClass().getResourceAsStream("../Assets/face_down.png"));
		
		player_card1.setImage(img); player_card2.setImage(img);
		player_card3.setImage(img); player_card4.setImage(img);
		player_card5.setImage(img);
		
		dealer_card1.setImage(img); dealer_card2.setImage(img);
		dealer_card3.setImage(img); dealer_card4.setImage(img);
		dealer_card5.setImage(img);
		
	}
	
	public void exit() { System.exit(0); }
	private void print(String message) { System.out.println(message); }
}
