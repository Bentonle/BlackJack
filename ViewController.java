package blackjack;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewController extends Application{

	public static void main(String[] args) {
		
		/** Multiple Application Usage Prevention
		 * 
		 * This block below prevents the user from opening multiple
		 * instances of the application with the user of FileChannel.
		 */
		String userHome = System.getProperty("user.home");
		File file = new File(userHome, "my.lock");
		try {
			/** FileChannel: 
			 * A channel for reading, writing, mapping, and manipulating a file.
			 */
		    FileChannel fc = FileChannel.open(file.toPath(),
		            StandardOpenOption.CREATE,
		            StandardOpenOption.WRITE);
		    FileLock lock = fc.tryLock();
		    if (lock == null) {
		        System.out.println("another instance is running");
		     
		        // This performs this action when you come back to main thread.
		        Platform.runLater(new Runnable() {

					@Override
					public void run() {
						
						// Creates an alert when the action above is done.
						Alert alert = new Alert(AlertType.INFORMATION);
				        alert.setTitle("Error");
				        alert.setHeaderText("Blackjack Game");
				        alert.setContentText("Sorry, you have an instance running aleady."
				        		+ "\n"
				        		+ "Please close that, before trying to open another.");
				        
				        alert.showAndWait();
					}
		        	
		        });
		        
		    }else {
		    	launch(args);
		    }
		} catch (IOException e) {
		    throw new Error(e);
		}
		// - - - - - - - - - - - End of Comment - - - - - - - - - - - -
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("BlackJack");
			primaryStage.setScene(scene);
			primaryStage.show();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    @FXML
    void playGame(ActionEvent event) {

    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
    	try {
			Parent root = (Parent) fxmlLoader.load();
			
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.setTitle("BlackJack");
			stage.setScene(new Scene(root));
			
			stage.show();
			
		} catch (IOException e) {
			System.out.println("Couldn't load game...");
		}
    }
    @FXML
    void exitGame(ActionEvent event) {
    	System.exit(0);
    }
}
