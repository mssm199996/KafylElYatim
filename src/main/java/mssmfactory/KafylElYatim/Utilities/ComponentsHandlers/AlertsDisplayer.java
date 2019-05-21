package mssmfactory.KafylElYatim.Utilities.ComponentsHandlers;

import java.util.Optional;

import com.jfoenix.controls.JFXPasswordField;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class AlertsDisplayer {
	public void displayErrorAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
			  alert.setTitle(title);
			  alert.setHeaderText(header);
			  alert.setContentText(content);
			  alert.showAndWait();
	}
	
	public void displayInformationAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		  	  alert.setTitle(title);
		  	  alert.setHeaderText(header);
		  	  alert.setContentText(content);
		  	  alert.showAndWait();
	}
	
	public boolean displayConfirmationAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
			  alert.setTitle(title);
			  alert.setHeaderText(header);
			  alert.setContentText(content);
			  
		Optional<ButtonType> result = alert.showAndWait();
		
		return result.get() == ButtonType.OK;
	}

	public Integer displayIntegerAlert(String title, String header, String content, String defaultValue) {
		TextInputDialog dialog = new TextInputDialog(defaultValue);
						dialog.setTitle(title);
						dialog.setHeaderText(header);
						dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();
		
		if (result.isPresent()){
			try { return Integer.parseInt(result.get()); }
			catch(Exception exp) { return null; }
		}
		
		return 0;
	}
	
	public String displayPasswordAlert(String title, String header, String content, String defaultValue) {
		JFXPasswordField textContainer = new JFXPasswordField();
						 textContainer.setPromptText("Mot de passe");
						 textContainer.setLabelFloat(true);
						 
		BorderPane root = new BorderPane();
				   root.setCenter(textContainer);
		
		Alert dialog = new Alert(AlertType.CONFIRMATION);
						dialog.setTitle(title);
						dialog.setHeaderText(header);
						dialog.getDialogPane().setContent(root);
						
		Optional<ButtonType> button = dialog.showAndWait();
						
		if (button.get() == ButtonType.OK)
			return textContainer.getText();
		
		return null;
	}
	
}
