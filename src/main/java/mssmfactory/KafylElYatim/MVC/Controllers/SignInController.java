package mssmfactory.KafylElYatim.MVC.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.MVC.SubStages.AuthorizedOrphensStage;
import mssmfactory.KafylElYatim.MVC.SubStages.BilansDonationsStage;
import mssmfactory.KafylElYatim.MVC.SubStages.ConfigurationStage;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

/**
 * Created by MSSM on 13/09/2017.
 */
public class SignInController implements Initializable{
	public static String USERNAME = "admin";
	public static String PASSWORD = "ky-admin";
	
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton B1,B3;
    @FXML private ProgressIndicator progression;

    private BorderPane root = null;

    @FXML public void about() throws IOException {
        ComponentsHolder.SIGN_IN.setAlwaysOnTop(false);
        
        Stage stage = new Stage();
              stage.setScene(new Scene((BorderPane)FXMLLoader.load(getClass().getResource("/mssmfactory/KafylElYatim/MVC/FXMLS/About.fxml"))));
              stage.setTitle("A propos de l'auteur");
              stage.setResizable(false);
              stage.getIcons().addAll(ComponentsHolder.WINDOW_ICON);
              stage.setAlwaysOnTop(true);
              stage.showAndWait();
              
        ComponentsHolder.SIGN_IN.setAlwaysOnTop(true);
    }

    @FXML 
    public void checkConnection() throws IOException {
    		(new Thread(new Runnable() {
				@Override
				public void run() {	
					if(username.getText().equals(USERNAME) && password.getText().equals(PASSWORD)){
						Platform.runLater(new Runnable() {
							@Override
							public void run() { updateCraps(true, ProgressIndicator.INDETERMINATE_PROGRESS); }
						});
						try {
							root = (BorderPane) FXMLLoader.load(getClass().getResource("/mssmfactory/KafylElYatim/MVC/FXMLS/MainWindow.fxml"));
							
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Stage stage = new Stage();
							              stage.setScene(new Scene(root, 800, 600));
							              stage.setTitle("Kafil El Yatim");
							              stage.getScene().getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/GeneralPurposes.css");
							              stage.getIcons().addAll(ComponentsHolder.WINDOW_ICON);
							              stage.setOnCloseRequest(event -> {
							              	  ComponentsHolder.INTRODUCTION.show();
							              	  ComponentsHolder.SIGN_IN.show();
							              	  password.setText("");
							              });
							              stage.setOnShown(event -> {
							            	  UtilitiesHolder.CONFIGURATION_STAGE = new ConfigurationStage();
							            	  UtilitiesHolder.AUTHORIZED_ORPHENS_STAGE = new AuthorizedOrphensStage();
							            	  UtilitiesHolder.BILANS_DONATIONS_STAGE = new BilansDonationsStage();
							              	  updateCraps(false, 1.0);
							              });
								          stage.setMaximized(true);
								          stage.show();
							          
						          ComponentsHolder.INTRODUCTION.close();
						          ComponentsHolder.SIGN_IN.close();
						          MainWindowController.IS_SU = false;
								}
							});
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
					else {
			    		Platform.runLater(new Runnable() {
							@Override
							public void run() {
								ComponentsHolder.SIGN_IN.setAlwaysOnTop(false);
								
								Alert alert = new Alert(AlertType.ERROR);
									  alert.setTitle("Kafil El Yatim");
									  alert.setHeaderText("Erreur d'authentification");
									  alert.setContentText("Nom d'utilisateur ou mot de passe invalide");
									  alert.showAndWait();		
									 
								ComponentsHolder.SIGN_IN.setAlwaysOnTop(true);
							}
						});
			        }
				}
        	})).start();
    }
    
    @FXML 
    public void close(){ System.exit(0); }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	this.updateCraps(false, 1.0);    
    }
    
    private void updateCraps(boolean function, double progress) {
    	progression.setProgress(progress);
        username.setDisable(function);
        password.setDisable(function);
        B1.setDisable(function);
        B3.setDisable(function);
        progression.setPrefSize(45, 45);
    }
}
