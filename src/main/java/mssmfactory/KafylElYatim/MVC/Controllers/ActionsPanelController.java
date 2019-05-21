package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.Action;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class ActionsPanelController implements Initializable{

	public static ObservableList<Action> ACTIONS = FXCollections.observableList(new LinkedList<Action>());
	
	@FXML private JFXTextField searchNomTuteur;
    @FXML private ListView<Tuteur> listeTuteurs;
    @FXML private TableView<Action> tableActions;

    @FXML 
    private void searchTuteurs(KeyEvent event) {
    	String names = this.searchNomTuteur.getText();
    	
    	(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Tuteur> result = UtilitiesHolder.TUTEUR_DAO.getSpecifiedTuteurs(names, null, null, null, null);
    	
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						TutorsPanelController.TUTEURS.clear();
						TutorsPanelController.TUTEURS.addAll(result);
					}
				});
			}
    	})).start();
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initLists();
		this.initTables();
	}
	
	private void initLists() {
		this.listeTuteurs.setItems(TutorsPanelController.TUTEURS);
		this.listeTuteurs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tuteur>() {
			@Override
			public void changed(ObservableValue<? extends Tuteur> observable, Tuteur oldValue, Tuteur newValue) {
				ActionsPanelController.ACTIONS.clear();
				if(newValue != null)
					ActionsPanelController.ACTIONS.addAll(UtilitiesHolder.EVENEMENT_DAO.getAllActions(newValue));
			}
		});
	}
	
	private void initTables() {
		ActionsPanelController.ACTIONS.clear();
		
		this.tableActions.setItems(ACTIONS);
		
		TableColumn<Action, Integer> idColumn =  ((TableColumn<Action,Integer>) this.tableActions.getColumns().get(0));
									 idColumn.setCellValueFactory(action -> {
										 return new SimpleIntegerProperty(action.getValue().getIdAction()).asObject();
									 });
							
		TableColumn<Action, String> nomTuteurColumn =  ((TableColumn<Action,String>) this.tableActions.getColumns().get(1));
									nomTuteurColumn.setCellValueFactory(action -> {
										return new SimpleStringProperty(action.getValue().getTuteur().getNom() + " " + action.getValue().getTuteur().getPrenom());
									});
			
		TableColumn<Action, String> designationEventColumn =  ((TableColumn<Action,String>) this.tableActions.getColumns().get(2));
									designationEventColumn.setCellValueFactory(action -> {
										return new SimpleStringProperty(action.getValue().getEvenement().getDesignationEvenement());
									});	
									
		TableColumn<Action, String> descriptionEventColumn =  ((TableColumn<Action,String>) this.tableActions.getColumns().get(3));
									descriptionEventColumn.setCellValueFactory(action -> {
										return new SimpleStringProperty(action.getValue().getEvenement().getDescriptionEvenement());
									});		
									
		
									
		TableColumn<Action, Double> quantiteColumn =  ((TableColumn<Action,Double>) this.tableActions.getColumns().get(4));
									quantiteColumn.setCellValueFactory(action -> {
										return new SimpleDoubleProperty(action.getValue().getQuantiteAction()).asObject();
									});	
	}
}
