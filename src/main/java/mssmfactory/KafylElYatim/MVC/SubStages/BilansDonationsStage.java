package mssmfactory.KafylElYatim.MVC.SubStages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class BilansDonationsStage extends Stage{
	public BilansDonationsStage() {
		try {
			URL url = getClass().getResource("/mssmfactory/KafylElYatim/MVC/FXMLS/SubStages/BilansDonations.fxml").toURI().toURL();
			BorderPane rootPane = (BorderPane) FXMLLoader.load(url);

			this.setScene(new Scene(rootPane));
			this.getScene().getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/GeneralPurposes.css");
			this.setTitle("KafylElYatim");
			this.setResizable(false);
			this.getIcons().addAll(ComponentsHolder.WINDOW_ICON);
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
		}	
	}
}
