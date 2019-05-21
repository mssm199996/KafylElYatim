package mssmfactory.KafylElYatim.MVC.SubStages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class AuthorizedOrphensStage extends Stage{
	public AuthorizedOrphensStage() {
		try {
			URL url = getClass().getResource("/mssmfactory/KafylElYatim/MVC/FXMLS/SubStages/AuthorizedOrphens.fxml").toURI().toURL();
			BorderPane rootPane = (BorderPane) FXMLLoader.load(url);
			
			this.setScene(new Scene(rootPane));
			this.setTitle("KafylElYatim");
			this.getScene().getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/GeneralPurposes.css");
			this.setResizable(false);
			this.setMaximized(true);
			this.getIcons().addAll(ComponentsHolder.WINDOW_ICON);
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
		}		
	}
}
