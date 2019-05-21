package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.Action;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.EvenementDecharge;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.EtatHabitat;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.TypeBien;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.SituationSociale.NiveauVie;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class EventsDechargesController implements Initializable{
	public static ObservableList<Tuteur> SELECTED_TUTORS = FXCollections.observableList(new LinkedList<Tuteur>());
	public static ObservableList<EvenementDecharge> EVENEMENT_DECHARGE = FXCollections.observableList(new LinkedList<EvenementDecharge>());
		
	@FXML private JFXTextField searchNomTuteur;
    @FXML private JFXComboBox<Region> searchRegionTuteur;
    @FXML private JFXComboBox<NiveauVie> searchNiveauVieTuteur;
    @FXML private TableView<Tuteur> tableTuteurs;
    @FXML private ListView<EvenementDecharge> listeEvenements;
    @FXML private ListView<Tuteur> listeTuteurs;
    @FXML private JFXComboBox<EtatHabitat> searchEtatHabitat;
    @FXML private JFXComboBox<TypeBien> searchTypeHabitat;

    @FXML
    private void addEvenement() {
    	BorderPane dialogContainer = UtilitiesHolder.DIALOGS_BUILDER.getNewEvenementDechargeDialog();
		
		VBox formContainer = (VBox) dialogContainer.getCenter();
		
		JFXTextField designationNode = (JFXTextField) ((BorderPane) formContainer.getChildren().get(0)).getCenter();
		JFXTextArea descriptionNode = (JFXTextArea) ((BorderPane) ((TitledPane) formContainer.getChildren().get(1)).getContent()).getCenter();
		JFXDatePicker dateNode = (JFXDatePicker) ((BorderPane) formContainer.getChildren().get(2)).getCenter();
		
		Alert alert = new Alert(AlertType.INFORMATION);
			  alert.setTitle("Nouvel evenement");
			  alert.setHeaderText("Formulaire d'ajout d'un nouvel evenement");
			  alert.getDialogPane().setContent(dialogContainer);
		 	  alert.showAndWait();
						   	
		String designation = designationNode.getText();
		String description = descriptionNode.getText();
		LocalDate date = dateNode.getValue();
		
		if(alert.getResult() != ButtonType.OK)
			return;
		
		if (!designation.equals("") && date != null){
			(new Thread(new Runnable() {
				@Override
				public void run() {
					EvenementDecharge evenementDecharge = new EvenementDecharge();
									  evenementDecharge.setDateEvenement(date);
									  evenementDecharge.setDescriptionEvenement(description);
									  evenementDecharge.setDesignationEvenement(designation);
				
					UtilitiesHolder.EVENEMENT_DAO.addNewEvenement(evenementDecharge);
	
					Platform.runLater(new Runnable() {
						@Override
						public void run() { EventsDechargesController.EVENEMENT_DECHARGE.add(evenementDecharge); }
					});
				}
	    	})).start();						
		}
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenement", "Erreur de saisi", "Veuillez verifier la designation et la date");
    }

    @FXML
    private void deleteEvenement() {
    	EvenementDecharge evenementDecharge = this.listeEvenements.getSelectionModel().getSelectedItem();
    	
    	if(evenementDecharge != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des Evenements", "Confirmation de suppression", "Confirmer pour proceder à la suppression"))
	    		return;
    		
    		(new Thread(new Runnable() {
    			@Override
    			public void run() {
    				UtilitiesHolder.EVENEMENT_DAO.deleteEvenement(evenementDecharge);
    				
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() { EventsDechargesController.EVENEMENT_DECHARGE.remove(evenementDecharge); }
    				});
    			}
        	})).start();
    	}
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenements", "Erreur de suppression", "Veuillez d'abords selectionner un evenement");
    }

    @FXML
    private void printDecharges() {
    	EvenementDecharge evenement = this.listeEvenements.getSelectionModel().getSelectedItem();
    	
    	if(evenement == null) {
    		UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenements", "Erreur de saisi", "Veuillez d'abords selectionner un evenement");
    		return;
    	}
    	
		if(UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des evenements", "Confirmation d'impression", "Confirmer pour proceder à la l'impression")) {			
			Integer quantite = UtilitiesHolder.ALERTS_DISPLAYER.displayIntegerAlert(
					"Gestion des evenements", "Formulaire de saisi", 
					"Veuillez saisir la quantite de lot à imprimer pour le tuteur ci dessus", "1");
			
			if(quantite != null && quantite > 0) {
				Action[] printableActions = new Action[EventsDechargesController.SELECTED_TUTORS.size()];
				
				int i = 0;
				for(Tuteur tuteur: EventsDechargesController.SELECTED_TUTORS) {
					Action action = new Action();
						   action.setDateAction(LocalDate.now());
						   action.setEvenement(evenement);
						   action.setTuteur(tuteur);
						   action.setQuantiteAction(quantite);
						   
					
					printableActions[i++] = action;
				}
				
				(new Thread(new Runnable() {
					@Override
					public void run() { UtilitiesHolder.EVENEMENT_DAO.addNewActions(printableActions); }
				})).start();
				
				UtilitiesHolder.PRINTING_HANDLER.printDecharges(printableActions);
			}
			else if(quantite == null || quantite != 0){
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenements", "Erreur de saisi", "La quantite est un nombre positive !");
					}
				});
			}
		}
    }

    @FXML
    private void refreshTutors() {
    	String names = this.searchNomTuteur.getText();
    	Region region = this.searchRegionTuteur.getSelectionModel().getSelectedItem();
    	NiveauVie ndv = this.searchNiveauVieTuteur.getSelectionModel().getSelectedItem();
    	EtatHabitat etatHabitat = this.searchEtatHabitat.getSelectionModel().getSelectedItem();
    	TypeBien typeBien = this.searchTypeHabitat.getSelectionModel().getSelectedItem();
    	
    	(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Tuteur> result = UtilitiesHolder.TUTEUR_DAO.getSpecifiedTuteurs(names, region, ndv, etatHabitat, typeBien);
				
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

    @FXML
    private void resetSearchingTuteur() {
    	this.searchNomTuteur.setText("");
    	this.searchRegionTuteur.getSelectionModel().clearSelection();
    	this.searchNiveauVieTuteur.getSelectionModel().clearSelection();
    	this.searchEtatHabitat.getSelectionModel().clearSelection();
    	this.searchTypeHabitat.getSelectionModel().clearSelection();
    	this.refreshTutors();
    }
    
    @FXML
    private void addAllCurrentTutors() {
    	for(Tuteur tuteur: TutorsPanelController.TUTEURS)
    		if(!EventsDechargesController.SELECTED_TUTORS.contains(tuteur))
    			EventsDechargesController.SELECTED_TUTORS.add(tuteur);
    }
    
    @FXML
    private void removeAllCurrentTutors() {
		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des evenements", "Confirmation de suppression", "Confirmer pour proceder à la suppression"))
			return;
		
    	EventsDechargesController.SELECTED_TUTORS.clear();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initInitialData();
		this.initBindings();
		this.initComboBoxes();
		this.initTables();
		this.initLists();
	}
	
	private void initLists() {
		this.listeTuteurs.setItems(EventsDechargesController.SELECTED_TUTORS);
		this.listeEvenements.setItems(EventsDechargesController.EVENEMENT_DECHARGE);
	}
	
	private void initTables() {
		this.tableTuteurs.setItems(TutorsPanelController.TUTEURS);
		
		TableColumn<Tuteur, Integer> idColumn =  ((TableColumn<Tuteur,Integer>) this.tableTuteurs.getColumns().get(0));
									 idColumn.setCellValueFactory(tuteur -> {
										 return new SimpleIntegerProperty(tuteur.getValue().getId()).asObject();
									 });
		 
		TableColumn<Tuteur, String> nomColumn =  ((TableColumn<Tuteur,String>) this.tableTuteurs.getColumns().get(1));
									nomColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getNom());
									});
				
		TableColumn<Tuteur, String> prenomColumn =  ((TableColumn<Tuteur,String>) this.tableTuteurs.getColumns().get(2));
									prenomColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getPrenom());
									});
				   
		TableColumn<Tuteur, Region> regionColumn =  ((TableColumn<Tuteur,Region>) this.tableTuteurs.getColumns().get(3));
									regionColumn.setCellValueFactory(tuteur -> {
										return new SimpleObjectProperty(tuteur.getValue().getRegion());
									});
				
		TableColumn<Tuteur, Integer> nbOrphelinColumn =  ((TableColumn<Tuteur,Integer>) this.tableTuteurs.getColumns().get(4));
									 nbOrphelinColumn.setCellValueFactory(tuteur -> {
										 return new SimpleIntegerProperty(tuteur.getValue().getNbOrphelins()).asObject();
									 });
									
		TableColumn<Tuteur, Boolean> takeItOrNotColumn =  ((TableColumn<Tuteur, Boolean>) this.tableTuteurs.getColumns().get(5));
									 takeItOrNotColumn.setCellFactory(new Callback<TableColumn<Tuteur,Boolean>, TableCell<Tuteur,Boolean>>() {
										@Override
										public TableCell<Tuteur, Boolean> call(TableColumn<Tuteur, Boolean> arg0) {
											return new TableCell<Tuteur, Boolean>() {
												@Override
												public void updateItem(Boolean value, boolean empty) {
													super.updateItem(value, empty);
													if(empty) {
														setGraphic(null);
														setText(null);
													}
													else {
														Tuteur tuteur = (Tuteur) this.getTableRow().getItem();
														
														if(tuteur != null) {
															HBox container = new HBox();
															
															JFXButton takeItButton = new JFXButton("Ajouter");
																	  takeItButton.setOnMouseClicked(event -> {
																		  if(!EventsDechargesController.SELECTED_TUTORS.contains(tuteur))
																			  EventsDechargesController.SELECTED_TUTORS.add(tuteur);
																	  });
																	  takeItButton.setGraphic(new ImageView(ComponentsHolder.PLUS_IMAGE));
																	  
															JFXButton notTakeItButton = new JFXButton("Enlever");
																	  notTakeItButton.setOnMouseClicked(event -> {
																		  if(EventsDechargesController.SELECTED_TUTORS.contains(tuteur))
																			  EventsDechargesController.SELECTED_TUTORS.remove(tuteur);
																	  });
																	  notTakeItButton.setGraphic(new ImageView(ComponentsHolder.MINUS_IMAGE));
															
															container.getChildren().addAll(takeItButton, notTakeItButton);
															container.setAlignment(Pos.CENTER);
															setGraphic(container);
														}
													}
												}
											};
										}
									});
	}
	
	private void initComboBoxes() {
		this.searchRegionTuteur.setItems(ConfigurationStageController.REGION);
		this.searchNiveauVieTuteur.setItems(FXCollections.observableArrayList(NiveauVie.values()));
		this.searchTypeHabitat.setItems(FXCollections.observableArrayList(TypeBien.values())); 
		this.searchEtatHabitat.setItems(FXCollections.observableArrayList(EtatHabitat.values()));
	}
	
	private void initInitialData() {
		EventsDechargesController.EVENEMENT_DECHARGE.clear();
		EventsDechargesController.EVENEMENT_DECHARGE.addAll(UtilitiesHolder.EVENEMENT_DAO.getAllEvenementDecharge());
	}
	
	private void initBindings() {
		ComponentsHolder.SEARCH_REGION_TUTEUR.valueProperty().bindBidirectional(this.searchRegionTuteur.valueProperty());
		ComponentsHolder.SEARCH_TUTORS.textProperty().bindBidirectional(this.searchNomTuteur.textProperty());
		ComponentsHolder.SEARCH_NIVEAU_VIE_TUTEUR.valueProperty().bindBidirectional(this.searchNiveauVieTuteur.valueProperty());
		ComponentsHolder.SEARCH_ETAT_HABITAT.valueProperty().bindBidirectional(this.searchEtatHabitat.valueProperty());
		ComponentsHolder.SEARCH_TYPE_HABITAT.valueProperty().bindBidirectional(this.searchTypeHabitat.valueProperty());
	}
}
