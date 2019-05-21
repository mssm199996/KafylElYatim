package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Vehicule;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Appareillage;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class ConfigurationStageController implements Initializable{

	public static ObservableList<Vehicule> VEHICULES = FXCollections.observableList(new LinkedList<Vehicule>());
	public static ObservableList<Region> REGION = FXCollections.observableList(new LinkedList<Region>());
	public static ObservableList<Appareillage> APPAREILLAGE = FXCollections.observableList(new LinkedList<Appareillage>());
	
	@FXML private ListView<Vehicule> listeVehicules;
    @FXML private ListView<Region> listeRegions;
    @FXML private ListView<Appareillage> listeAppareillages;

    @FXML
    private void addNewRegion() {
    	TextInputDialog dialog = new TextInputDialog("");
				    	dialog.setTitle("Nouvelle region");
				    	dialog.setHeaderText("Formulaire d'une nouvelle region");
				    	dialog.setContentText("Veuillez saisir ci dessous le nom de la region");

    	Optional<String> result = dialog.showAndWait();
    	
    	if (result.isPresent()){
    		Region region = new Region();
			   	   region.setNom(result.get());
			   
			UtilitiesHolder.REGION_DAO.insertNewRegion(region);
			   
			ConfigurationStageController.REGION.add(region);
    	}
    }

    @FXML
    private void addNewVehicule() {
    	TextInputDialog dialog = new TextInputDialog("");
				    	dialog.setTitle("Nouveau vehicule");
				    	dialog.setHeaderText("Formulaire d'un nouveau vehicule");
				    	dialog.setContentText("Veuillez saisir ci dessous le nom du vehicule");

    	Optional<String> result = dialog.showAndWait();

    	if (result.isPresent()){
    		Vehicule vehicule = new Vehicule();
    				 vehicule.setType(result.get());
    		
    		UtilitiesHolder.VEHICULE_DAO.insertNewVehicule(vehicule);
    		
    		ConfigurationStageController.VEHICULES.add(vehicule);
    	}
    }

    @FXML
    private void deleteRegion() {
    	Region region = this.listeRegions.getSelectionModel().getSelectedItem();
    	
    	if(region != null) {
    		UtilitiesHolder.REGION_DAO.deleteRegion(region);
    		
    		ConfigurationStageController.REGION.remove(region);
    	}
    }

    @FXML
    private void deleteVehicule() {
    	Vehicule vehicule = this.listeVehicules.getSelectionModel().getSelectedItem();
    	
    	if(vehicule != null) {
    		UtilitiesHolder.VEHICULE_DAO.deleteVehicule(vehicule);
    		
    		ConfigurationStageController.VEHICULES.remove(vehicule);
    	}
    }
    
    @FXML
    private void addNewAppareillage() {
    	TextInputDialog dialog = new TextInputDialog("");
				    	dialog.setTitle("Nouveau appareillage");
				    	dialog.setHeaderText("Formulaire d'un nouveau appareillage");
				    	dialog.setContentText("Veuillez saisir ci dessous le nom de l'appareillage");

		Optional<String> result = dialog.showAndWait();
		
		if (result.isPresent()){
			Appareillage appareillage = new Appareillage();
					 appareillage.setType(result.get());
			
			UtilitiesHolder.APPAREILLAGE_DAO.insertNewAppareillage(appareillage);
			
			ConfigurationStageController.APPAREILLAGE.add(appareillage);
		}
    }

    @FXML
    void deleteAppareillage() {
    	Appareillage appareillage = this.listeAppareillages.getSelectionModel().getSelectedItem();
    	
    	if(appareillage != null) {
    		UtilitiesHolder.APPAREILLAGE_DAO.deleteAppareillage(appareillage);
    		
    		ConfigurationStageController.APPAREILLAGE.remove(appareillage);
    	}
    }

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initInitialData();
		this.initListes();
	}
	
	private void initListes() {
		this.listeVehicules.setItems(ConfigurationStageController.VEHICULES);
		this.listeRegions.setItems(ConfigurationStageController.REGION);
		this.listeAppareillages.setItems(ConfigurationStageController.APPAREILLAGE);
	}
	
	private void initInitialData() {
		ConfigurationStageController.REGION.clear();
		ConfigurationStageController.REGION.addAll(UtilitiesHolder.REGION_DAO.getAllRegions());
		ConfigurationStageController.VEHICULES.clear();
		ConfigurationStageController.VEHICULES.addAll(UtilitiesHolder.VEHICULE_DAO.getAllVehicules());		
		ConfigurationStageController.APPAREILLAGE.clear();
		ConfigurationStageController.APPAREILLAGE.addAll(UtilitiesHolder.APPAREILLAGE_DAO.getAllAppareillages());
	}
}
