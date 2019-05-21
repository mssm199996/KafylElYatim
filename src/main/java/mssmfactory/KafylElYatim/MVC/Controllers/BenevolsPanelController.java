package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Benevole;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Benevole.Genre;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.JourneeDisponible;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.JourneeDisponible.JourDeSemaine;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Vehicule;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class BenevolsPanelController implements Initializable{
	 
	public static ObservableList<Benevole> BENEVOLES = FXCollections.observableList(new LinkedList<Benevole>());
	public static ObservableList<JourneeDisponible> JOURNEES_DISPONIBLES = FXCollections.observableList(new LinkedList<JourneeDisponible>());
	public static ObservableList<Vehicule> VEHICULES = FXCollections.observableList(new LinkedList<Vehicule>());
	
	@FXML private JFXTextField newBenevoleNom, newBenevolePrenom, newBenevoleTelephone, newBenevoleEmail, newBenevoleFacebook, searchBenevolesNom;
	@FXML private ToggleGroup newBenevoleSexe;
	@FXML private JFXComboBox<JourDeSemaine> searchBenevolesJourneeDisponible;
	@FXML private JFXComboBox<Vehicule> searchBenevolesVehicule;
	@FXML private JFXHamburger leftPaneHamburger;
	@FXML private TableView<Benevole> tableBenevoles;
	@FXML private ListView<JourneeDisponible> listeJourneesDisponibles;
	@FXML private ListView<Vehicule> listeVehicules;
	@FXML private JFXDrawer leftSidePane;
	@FXML private Label resultCount;

	@FXML 
	private void deleteVehicule() {
		Vehicule vehicule = this.listeVehicules.getSelectionModel().getSelectedItem();
	    
	    if(vehicule != null) {
	    	Benevole benevole = this.tableBenevoles.getSelectionModel().getSelectedItem();
	    	
	    	(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.BENEVOLE_DAO.deleteVehiculeBenevole(benevole, vehicule);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() { BenevolsPanelController.VEHICULES.remove(vehicule); }
					});
				}
	    	})).start();
	    }
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Benevoles", "Erreur de suppression", "Veuillez d'abords selectionner un vehicule");
	}
	    
	@FXML
	private void addNewVehicule() {
		Benevole benevole = this.tableBenevoles.getSelectionModel().getSelectedItem();
		
		if(benevole != null) {
			List<Vehicule> vehicules = UtilitiesHolder.VEHICULE_DAO.getAllVehicules();
			
			HBox dialogContainer = UtilitiesHolder.DIALOGS_BUILDER.getVehiculesDialog();
			
			JFXComboBox<Vehicule> vehiculesNode = (JFXComboBox<Vehicule>) dialogContainer.getChildren().get(1);
								  vehiculesNode.setItems(FXCollections.observableArrayList(vehicules));
								  vehiculesNode.getSelectionModel().select(0);
								  
			Alert alert = new Alert(AlertType.INFORMATION);
			   	  alert.setTitle("Nouvelle affection de vehicule");
			   	  alert.setHeaderText("Formulaire d'ajout d'une nouvelle affectation de vehicule");
			   	  alert.getDialogPane().setContent(dialogContainer);
			   	  alert.showAndWait();
			
			Vehicule vehicule = vehiculesNode.getSelectionModel().getSelectedItem();
			   	  
			if (vehicule != null){
				(new Thread(new Runnable() {
					@Override
					public void run() {
						UtilitiesHolder.BENEVOLE_DAO.addNewVehicule(benevole, vehicule);
						
						Platform.runLater(new Runnable() {
							@Override
							public void run() { BenevolsPanelController.VEHICULES.add(vehicule); }
						});
					}
				})).start();
			}
		}
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Benevoles", "Erreur de saisi", "Veuillez d'abords selectionner un benevole");
	}
	    
	@FXML
	private void addNewJourneeDisponible() {
	    Benevole benevole = this.tableBenevoles.getSelectionModel().getSelectedItem();
	    
	    if(benevole != null) {
	    	HBox dialogContainer = UtilitiesHolder.DIALOGS_BUILDER.getJourneeDisponibleDialog();
	    	
	    	JFXComboBox<JourDeSemaine> jourDeSemaineNode = (JFXComboBox<JourDeSemaine>) ((HBox)dialogContainer.getChildren().get(0)).getChildren().get(1);
	    							   jourDeSemaineNode.setItems(FXCollections.observableArrayList(JourDeSemaine.values()));
	    							   jourDeSemaineNode.getSelectionModel().select(0);
	    	
	    	JFXTimePicker heureDebutNode = (JFXTimePicker) ((HBox)dialogContainer.getChildren().get(2)).getChildren().get(1);
	    	JFXTimePicker heureFinNode =  (JFXTimePicker) ((HBox)dialogContainer.getChildren().get(2)).getChildren().get(2);
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
			   	  alert.setTitle("Nouvelle affectation d'une journee disponible");
			   	  alert.setHeaderText("Formulaire d'ajout d'une nouvelle affectation d'une journee disponible");
			   	  alert.getDialogPane().setContent(dialogContainer);
			   	  alert.showAndWait();
			   	  
			JourDeSemaine jourDeSemaine = jourDeSemaineNode.getSelectionModel().getSelectedItem();
			LocalTime heureDebut = heureDebutNode.getValue();
			LocalTime heureFin = heureFinNode.getValue();
			
			if(jourDeSemaine != null && heureDebut != null && heureFin != null) {
				(new Thread(new Runnable() {
					@Override
					public void run() {
						JourneeDisponible journeeDisponible = new JourneeDisponible();
										  journeeDisponible.setHeureDebut(heureDebut);
										  journeeDisponible.setHeureFin(heureFin);
										  journeeDisponible.setJourSemaine(jourDeSemaine);
										  journeeDisponible.setBenevole(benevole);
						
						UtilitiesHolder.BENEVOLE_DAO.insertNewJourneeDisponible(journeeDisponible);
										  
						Platform.runLater(new Runnable() {
							@Override
							public void run() { BenevolsPanelController.JOURNEES_DISPONIBLES.add(journeeDisponible); }
						});
					}
				})).start();
			}
		    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Benevoles", "Erreur de saisi", "Veuillez verifier le formulaire !");
	    }
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Benevoles", "Erreur de saisi", "Veuillez d'abords selectionner un benevole");
	}
	    
	@FXML
	private void supprimerJourneeDisponible() {
	    JourneeDisponible journeeDisponible = this.listeJourneesDisponibles.getSelectionModel().getSelectedItem();
	    
	    if(journeeDisponible != null) {
	    	if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des benevoles", "Confirmation de suppression", "Confirmer pour proceder à la suppression"))
	    		return;
	    		
	    	(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.BENEVOLE_DAO.deleteJourneeDisponible(journeeDisponible);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() { BenevolsPanelController.JOURNEES_DISPONIBLES.remove(journeeDisponible); }
					});
				}
			})).start();
	    }
	}
	    
	@FXML
	private void addNewBenevole() {
		String nomBenevole = this.newBenevoleNom.getText();
		String prenomBenevole = this.newBenevolePrenom.getText();
		String telephoneBenevole = this.newBenevoleTelephone.getText();
		String emailBenevole = this.newBenevoleEmail.getText();
		String facebookBenevole = this.newBenevoleFacebook.getText();
		Genre genreBenevole = this.newBenevoleSexe.getSelectedToggle().equals(this.newBenevoleSexe.getToggles().get(0)) ? 
				Genre.Masculin : Genre.Feminin;
		
		if(nomBenevole.equals("") || prenomBenevole.equals("")) {
		    UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Benevoles", "Erreur de saisi", "Veuillez verifier le nom et le prenom");
		    return;
		}
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				Benevole benevole = new Benevole();
						 benevole.setEmail(emailBenevole);
						 benevole.setFacebook(facebookBenevole);
						 benevole.setGenre(genreBenevole);
						 benevole.setNom(nomBenevole);
						 benevole.setPrenom(prenomBenevole);
						 benevole.setTelephone(telephoneBenevole);
				 
				UtilitiesHolder.BENEVOLE_DAO.insertNewBenevole(benevole);
		
				Platform.runLater(new Runnable() {
					@Override
					public void run() { searchBenevoles(); }
				});
			}
		})).start();
	}

	@FXML
	private void deleteBenevole() {
		Benevole benevole = this.tableBenevoles.getSelectionModel().getSelectedItem();
		
		if(benevole != null) {
			if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des benevoles", "Confirmation de suppression", "Confirmer pour proceder à la suppression"))
	    		return;
			
			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.BENEVOLE_DAO.deleteBenevole(benevole);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() { BenevolsPanelController.BENEVOLES.remove(benevole); }
					});
				}
			})).start();
		}
	}

	@FXML
	private void resetSearchBenevoles() {
		this.searchBenevolesNom.setText("");
		this.searchBenevolesJourneeDisponible.getSelectionModel().clearSelection();
		this.searchBenevolesVehicule.getSelectionModel().clearSelection();
		this.searchBenevoles();
	}

	@FXML
	private void searchBenevoles() {
		String names = this.searchBenevolesNom.getText();
		JourDeSemaine journee = this.searchBenevolesJourneeDisponible.getSelectionModel().getSelectedItem();
		Vehicule vehicule = this.searchBenevolesVehicule.getSelectionModel().getSelectedItem();
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Benevole> result = UtilitiesHolder.BENEVOLE_DAO.getSpecifiedBenevoles(names, journee, vehicule);
								
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						BenevolsPanelController.BENEVOLES.clear();
						BenevolsPanelController.BENEVOLES.addAll(result);
						resultCount.setText("Nombre de benevoles: " + BenevolsPanelController.BENEVOLES.size());
					}
				});
			}
		})).start();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initTables();
		this.initHamburgers();
		this.initComboBoxes();
		this.initLists();
		this.initInitialData();
	}
	
	private void initHamburgers() {
        HamburgerSlideCloseTransition leftHamburgerAnimator = new HamburgerSlideCloseTransition(this.leftPaneHamburger);
        							  leftHamburgerAnimator.setRate(-1);
		  
		this.leftPaneHamburger.setAnimation(leftHamburgerAnimator);
		this.leftPaneHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
			this.leftPaneHamburger.getAnimation().setRate(this.leftPaneHamburger.getAnimation().getRate() * -1);
			this.leftPaneHamburger.getAnimation().play();
				            
			if(this.leftSidePane.isShown()) 
				this.leftSidePane.close();
			else this.leftSidePane.open();			
		});
	}
	
	private void initComboBoxes() {
		this.searchBenevolesJourneeDisponible.getSelectionModel().selectedItemProperty().addListener(event -> { searchBenevoles(); });
		this.searchBenevolesVehicule.getSelectionModel().selectedItemProperty().addListener(event -> { searchBenevoles(); });
		this.searchBenevolesJourneeDisponible.setItems(FXCollections.observableArrayList(JourneeDisponible.JourDeSemaine.values()));
		this.searchBenevolesVehicule.setItems(ConfigurationStageController.VEHICULES);
	}
	
	private void initLists() {
		this.listeJourneesDisponibles.setItems(BenevolsPanelController.JOURNEES_DISPONIBLES);
		this.listeVehicules.setItems(BenevolsPanelController.VEHICULES);
	}
	
	private void initTables() {
		this.tableBenevoles.setItems(BenevolsPanelController.BENEVOLES);
		this.tableBenevoles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Benevole>() {
			@Override
			public void changed(ObservableValue<? extends Benevole> observable, Benevole oldValue, Benevole newValue) {
				BenevolsPanelController.JOURNEES_DISPONIBLES.clear();
				BenevolsPanelController.VEHICULES.clear();
				
				if(newValue != null) {
					BenevolsPanelController.JOURNEES_DISPONIBLES.addAll(UtilitiesHolder.BENEVOLE_DAO.getJourneesDisponibles(newValue));
					BenevolsPanelController.VEHICULES.addAll(UtilitiesHolder.BENEVOLE_DAO.getVehicules(newValue));
				}
			}
		});
		
		StringConverter<String> stringConverter = new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        };
		
		TableColumn<Benevole, Integer> idColumn =  ((TableColumn<Benevole,Integer>) this.tableBenevoles.getColumns().get(0));
									   idColumn.setCellValueFactory(benevole -> {
										   return new SimpleIntegerProperty(benevole.getValue().getId()).asObject();
									   });

		TableColumn<Benevole, String> nomColumn =  ((TableColumn<Benevole,String>) this.tableBenevoles.getColumns().get(1));
									  nomColumn.setCellValueFactory(benevole -> {
										  return new SimpleStringProperty(benevole.getValue().getNom());
									  });
		
		TableColumn<Benevole, String> prenomColumn =  ((TableColumn<Benevole,String>) this.tableBenevoles.getColumns().get(2));
									  prenomColumn.setCellValueFactory(benevole -> {
										  return new SimpleStringProperty(benevole.getValue().getPrenom());
									  });
									  
		TableColumn<Benevole, Genre> genreColumn =  ((TableColumn<Benevole,Genre>) this.tableBenevoles.getColumns().get(3));
									 genreColumn.setCellValueFactory(benevole -> {
										 return new SimpleObjectProperty(benevole.getValue().getGenre());
									 });
									 
		TableColumn<Benevole, String> telephoneColumn =  ((TableColumn<Benevole,String>) this.tableBenevoles.getColumns().get(4));
									  telephoneColumn.setCellValueFactory(benevole -> {
										  return new SimpleStringProperty(benevole.getValue().getTelephone());
									  });
									  telephoneColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									  telephoneColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Benevole, String>>() {
										  @Override
								          public void handle(TableColumn.CellEditEvent<Benevole, String> event) {
											  event.getRowValue().setTelephone(event.getNewValue());
								              UtilitiesHolder.BENEVOLE_DAO.updateBenevole(event.getRowValue());
										  }
								      });
									  
		TableColumn<Benevole, String> emailColumn =  ((TableColumn<Benevole,String>) this.tableBenevoles.getColumns().get(5));
									  emailColumn.setCellValueFactory(benevole -> {
										  return new SimpleStringProperty(benevole.getValue().getEmail());
									  });
									  emailColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									  emailColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Benevole, String>>() {
										  @Override
								          public void handle(TableColumn.CellEditEvent<Benevole, String> event) {
											  event.getRowValue().setEmail(event.getNewValue());
								              UtilitiesHolder.BENEVOLE_DAO.updateBenevole(event.getRowValue());
										  }
								      });
									  
		TableColumn<Benevole, String> facebookColumn =  ((TableColumn<Benevole,String>) this.tableBenevoles.getColumns().get(6));
									  facebookColumn.setCellValueFactory(benevole -> {
										  return new SimpleStringProperty(benevole.getValue().getFacebook());
									  });
									  facebookColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									  facebookColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Benevole, String>>() {
										  @Override
								          public void handle(TableColumn.CellEditEvent<Benevole, String> event) {
											  event.getRowValue().setFacebook(event.getNewValue());
								              UtilitiesHolder.BENEVOLE_DAO.updateBenevole(event.getRowValue());
										  }
								      });
	}
	
	private void initInitialData() {
		BenevolsPanelController.BENEVOLES.clear();
		BenevolsPanelController.BENEVOLES.addAll(UtilitiesHolder.BENEVOLE_DAO.getAllBenevoles());
		resultCount.setText("Nombre de benevoles: " + BenevolsPanelController.BENEVOLES.size());
	}
}
