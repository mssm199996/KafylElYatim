package mssmfactory.KafylElYatim.MVC.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class MainWindowController implements Initializable{

	public static boolean IS_SU = false;
	public static String SU_PASSWORD = "ky-superuser";
	@FXML private Tab DonationsPanelTab, TutorsPaneTab, OrphensPaneTab, BenevolsPaneTab, EventsBonTabPane, EventsDechargeTabPane, HistoriqueActionsPane;
	
	@FXML
	private void showConfigurationStage() {
		UtilitiesHolder.CONFIGURATION_STAGE.showAndWait();	
	}
	
	@FXML
	private void authentificteSuperUser() {
		String password = UtilitiesHolder.ALERTS_DISPLAYER.displayPasswordAlert(
				"Gestion des donations", 
				"Confirmation d'authentification", 
				"Veuillez saisir le mot de passe de suppression", 
				"");
		
		if(password != null && password.equals(MainWindowController.SU_PASSWORD)) {
			IS_SU = true;
			UtilitiesHolder.ALERTS_DISPLAYER.displayInformationAlert("Authentification", "Authentification établie avec succés", "Vous êtes maintenant connecté(e) en tant que super utilisateur");
		}
		else if(password != null)
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Authentification", "Erreur d'authentification", "Mot de passe invalide");
	}
	
    @FXML
    public void printSummary() {
    	if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des Orphelins", "Confirmation d'impression", "Veuillez confirmer pour proceder à l'impression"))
    		return;
    	
    	List<Orphelin> orphelins = OrphensPanelController.ORPHELINS;
    	List<Tuteur> tuteurs = TutorsPanelController.TUTEURS;
    	List<Orphelin> printableOrphens = new LinkedList<Orphelin>();
    	
    	for(Orphelin orphelin: orphelins) 
    		if(tuteurs.contains(orphelin.getTuteur())) 
    			printableOrphens.add(orphelin);
    	
    	printableOrphens.sort(new Comparator<Orphelin>() {
			@Override
			public int compare(Orphelin o1, Orphelin o2) {
				String o1Nom = o1.getTuteur().getNom() + " " + o1.getPrenom();
				String o2Nom = o2.getNom() + " " + o2.getPrenom();
				
				return o1Nom.compareTo(o2Nom);
			}
		});
    	    	
    	UtilitiesHolder.PRINTING_HANDLER.printJoinedListTutorsOrphens(printableOrphens);
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			BorderPane donationsPane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/DonationsPanel.fxml").toURI().toURL()
			);
			
			BorderPane tutorsPane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/TutorsPanel.fxml").toURI().toURL()
			);
			
			BorderPane orphensPane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/OrphensPanel.fxml").toURI().toURL()
			);
			
			BorderPane benevolsPane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/BenevolsPanel.fxml").toURI().toURL()
			);
			
			BorderPane eventsBonPane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/EventsBonsPanel.fxml").toURI().toURL()
			);
			
			BorderPane eventsDechargePane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/EventsDechargesPanel.fxml").toURI().toURL()
			);
			
			BorderPane historiqueActionsPane = (BorderPane) FXMLLoader.load(getClass().getResource(
				"/mssmfactory/KafylElYatim/MVC/FXMLS/MainPanes/ActionsPanel.fxml").toURI().toURL()
			);
			
			ComponentsHolder.PLUS_IMAGE = 
					new Image(getClass().getResource("/mssmfactory/KafylElYatim/MVC/Icons/plus.png").toString());
			
			ComponentsHolder.MINUS_IMAGE = 
					new Image(getClass().getResource("/mssmfactory/KafylElYatim/MVC/Icons/minus.png").toString());			
			
			this.DonationsPanelTab.setContent(donationsPane);
			this.TutorsPaneTab.setContent(tutorsPane);
			this.OrphensPaneTab.setContent(orphensPane);
			this.BenevolsPaneTab.setContent(benevolsPane);
			this.EventsBonTabPane.setContent(eventsBonPane);
			this.EventsDechargeTabPane.setContent(eventsDechargePane);
			this.HistoriqueActionsPane.setContent(historiqueActionsPane);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}		
	}
}
