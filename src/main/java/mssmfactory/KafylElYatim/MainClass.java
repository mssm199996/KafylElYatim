package mssmfactory.KafylElYatim;

import java.util.logging.Level;
import java.util.logging.LogManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class MainClass extends Application{
    public static void main( String[] args ) {
    	LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        Application.launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception{
		ComponentsHolder.INTRODUCTION = primaryStage;
		ComponentsHolder.WINDOW_ICON = 
				new Image(getClass().getResource("/mssmfactory/KafylElYatim/MVC/Icons/logoc6.png").toString());

		StackPane introductionPane = (StackPane) FXMLLoader.load(getClass().getResource("/mssmfactory/KafylElYatim/MVC/FXMLS/Introduction.fxml").toURI().toURL());
		BorderPane signInPane = (BorderPane) FXMLLoader.load(getClass().getResource("/mssmfactory/KafylElYatim/MVC/FXMLS/SignIn.fxml").toURI().toURL());
		
		primaryStage.setScene(new Scene(introductionPane, 800, 600));
		primaryStage.setTitle("KafylElYatim");
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(event -> { System.exit(0); });
		primaryStage.getIcons().addAll(ComponentsHolder.WINDOW_ICON);
		primaryStage.show();
		
		Stage signInStage = new Stage();
			  signInStage.setScene(new Scene(signInPane, 400, 200));
			  signInStage.setOnCloseRequest(event -> { System.exit(0); });
			  signInStage.setResizable(false);
			  signInStage.setAlwaysOnTop(true);
			  signInStage.getIcons().addAll(ComponentsHolder.WINDOW_ICON);
			  signInStage.setTitle("Kafil El Yatim");
			  signInStage.show();
			  
		ComponentsHolder.SIGN_IN = signInStage;
	}
}
