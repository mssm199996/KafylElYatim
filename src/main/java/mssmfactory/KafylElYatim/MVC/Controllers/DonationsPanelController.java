package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationForm;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationType;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donator;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class DonationsPanelController implements Initializable {
	
	public static ObservableList<Donation> DONATIONS = FXCollections.observableList(new LinkedList<Donation>());
	public static ObservableList<Donator> DONATORS = FXCollections.observableList(new LinkedList<Donator>());
		
	@FXML private TableView<Donation> DonationsTableView;
	@FXML private JFXTextArea UpdateDonationObservationTextArea;
	@FXML private JFXComboBox<DonationType> NewDonationTypeComboBox, SearchDonationTypeComboBox;
	@FXML private JFXComboBox<DonationForm> NewDonationFormeComboBox;
	@FXML private JFXTextField NewDonationMontantTextField, SearchDonationIdentifiantTextField, searchDonateursName;
	@FXML private JFXDatePicker NewDonationDateDatePicker, SearchDonationDateDebutDatePicker, SearchDonationDateFinDatePicker;
	@FXML private JFXHamburger leftHamburger, rightHamburger;
	@FXML private JFXDrawer leftDrawer;
	@FXML private Label resultCount;
	@FXML private ListView<Donator> listeDonateurs;

	@FXML
	private void bilanDons() {
		if(MainWindowController.IS_SU) {
			UtilitiesHolder.BILANS_DONATIONS_STAGE.setTitle("Donations de: " + BilansDonationsController.SELECTED_DONATOR.getValue());
			UtilitiesHolder.BILANS_DONATIONS_STAGE.showAndWait();
		}
		else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des donations", "Erreur d'authentification", "Veuillez vous connecter en tant que super utilisateur");
	}
	
	@FXML
	private void addNewDonateur() {
		BorderPane content = UtilitiesHolder.DIALOGS_BUILDER.getNewDonatorDialog();
		
		Alert alert = new Alert(AlertType.INFORMATION);
			  alert.setTitle("Gestion des donations");
			  alert.setHeaderText("Ajout d'un donateur");
			  alert.getDialogPane().setContent(content);
			  alert.showAndWait();
			  
		JFXTextField nomNode = (JFXTextField) ((HBox) ((VBox) content.getCenter()).getChildren().get(0)).getChildren().get(1);
		JFXTextField prenomNode = (JFXTextField) ((HBox) ((VBox) content.getCenter()).getChildren().get(1)).getChildren().get(1);
		
		if(!nomNode.getText().equals("") && !prenomNode.getText().equals("")) {
			Donator donateur = new Donator();
					donateur.setNom(nomNode.getText());
					donateur.setPrenom(prenomNode.getText());
					
			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.DONATION_DAO.insertNewDonator(donateur);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() { searchDonateurs(); }
					});
				}
			})).start();
		}
		else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des donations", "Ajout d'un donateur", "Erreur de saisi, Veuillez verifier les données");
	}
	
	@FXML
	private void deleteDonateur() {
		Donator donator = this.listeDonateurs.getSelectionModel().getSelectedItem();
		
		if(donator != null) {
			if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des donations", "Confirmation de suppression", "Veuillez confirmer pour procéder à la suppression"))
				return;
			
			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.DONATION_DAO.deleteDonator(donator);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							DonationsPanelController.DONATORS.remove(donator);
						}
					});
				}
			})).start();
		}
		else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des donations", "Erreur de saisi", "Veuillez d'abords saisir un donateur");
	}
	
	@FXML
	private void searchDonateurs() {
		String searchedDonator = this.searchDonateursName.getText();
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Donator> donators = UtilitiesHolder.DONATION_DAO.getSpecifiedDonators(searchedDonator);
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						DonationsPanelController.DONATORS.clear();
						DonationsPanelController.DONATORS.addAll(donators);
					}
				});
			}
		})).start();
	}
	
	@FXML
	private void addDonation(ActionEvent event) {
		Donator donator = this.listeDonateurs.getSelectionModel().getSelectedItem();
		LocalDate date = this.NewDonationDateDatePicker.getValue();
		DonationType type = this.NewDonationTypeComboBox.getSelectionModel().getSelectedItem();
		DonationForm forme = this.NewDonationFormeComboBox.getSelectionModel().getSelectedItem();
		String valeur = this.NewDonationMontantTextField.getText();
		
		if(donator == null) {
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert(
					"Gestion des donations", "Ajout d'une donation", "Veuillez d'abords selectionner un donateur");
			return;
		}
		
		if(date == null || LocalDate.now().compareTo(date) < 0 || type == null || forme == null) {
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Ajout d'une donation", "Erreur de saisi", "Veuillez verifier la date ou le type !");
			return;
		}
		
		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Ajout d'une donation", "Message de confirmation", 
				"Donation (" + type + ")" + " du " + date + " d'une valeur de: " + valeur + "\nConfirmer pour continuer.")) return;
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				Donation donation = new Donation();
						 donation.setDate(date);
						 donation.setValeur(NewDonationMontantTextField.getText());
						 donation.setDonator(donator);
						 donation.setType(type);
						 donation.setForme(forme);
						 donation.setObservation("Rien à signaler");
				 
				 UtilitiesHolder.DONATION_DAO.insertDonation(donation);
				 
				 Platform.runLater(new Runnable() {
					@Override
					public void run() { 
						refreshDonations();
						BilansDonationsController.DONATIONS.add(donation);
					}
				});
			}
		})).start();
	} 
	
	@FXML
	private void deleteDonation() {
		Donation donation = this.DonationsTableView.getSelectionModel().getSelectedItem();
		
		if(donation == null) {
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des donations", "Erreur de saisi", "Veuillez d'abords selectionner une donation");
			return;
		}
				
		if(MainWindowController.IS_SU) {
			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.DONATION_DAO.deleteDonation(donation);
					refreshDonations();
					Platform.runLater(new Runnable() {
						@Override
						public void run() { BilansDonationsController.DONATIONS.remove(donation); }
					});
				}
			})).start();
		}
		else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des donations", "Erreur d'authentification", "Veuillez vous connecter en tant que super utilisateur");
	}
	
	@FXML
	private void refreshDonations() {
		DonationType type = this.SearchDonationTypeComboBox.getSelectionModel().getSelectedItem();
		
		int id = -1;
		
		try { id = Integer.parseInt(this.SearchDonationIdentifiantTextField.getText()); }
		catch(NumberFormatException exp) { }
		
		LocalDate startDate = this.SearchDonationDateDebutDatePicker.getValue();
		LocalDate endDate = this.SearchDonationDateFinDatePicker.getValue();
		
		final int donId = id;		
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Donation> result = UtilitiesHolder.DONATION_DAO.getSpecifiedDonations(donId, type, startDate, endDate, null);
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						DonationsPanelController.DONATIONS.clear();
						DonationsPanelController.DONATIONS.addAll(result);
						resultCount.setText("Nombre de donations: " + DonationsPanelController.DONATIONS.size());
					}
				});
			}
		})).start();
	}
	
	@FXML
	private void resetSearchingDonations(){
		this.SearchDonationIdentifiantTextField.setText("");
		this.SearchDonationTypeComboBox.getSelectionModel().clearSelection();
		this.SearchDonationDateDebutDatePicker.setValue(null);
		this.SearchDonationDateFinDatePicker.setValue(null);
		this.refreshDonations();
	}
	
	@FXML
	private void updateDonationObservation(ActionEvent event) {
		if(UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Mise à jour d'une donation", "Message de confirmation", "Confirmer pour proceder à la mise à jour."))
			(new Thread(new Runnable() {
				@Override
				public void run() {
					Donation donation = DonationsTableView.getSelectionModel().getSelectedItem();
					 		 donation.setObservation(UpdateDonationObservationTextArea.getText());
					 
					UtilitiesHolder.DONATION_DAO.updateDonation(donation);
				}
			})).start();
		
	}
	
	@FXML
	private void printDonations() {
		if(UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Impression des donations", "Message de confirmation", "Confirmer pour proceder à l'impression"))
			UtilitiesHolder.PRINTING_HANDLER.printDonations(DonationsPanelController.DONATIONS);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initInitialDatas();
		this.initComboBox();
		this.initDatePickers();
		this.initHamburgers();
		this.initLists();
		this.initTableView();
	}
	
	private void initLists() {
		this.listeDonateurs.setItems(DonationsPanelController.DONATORS);
		this.listeDonateurs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Donator>() {
			@Override
			public void changed(ObservableValue<? extends Donator> observable, Donator oldValue, Donator newValue) {
				BilansDonationsController.SELECTED_DONATOR.setValue(newValue);
			}
		});
	}
	
	private void initComboBox() {
		this.NewDonationTypeComboBox.getItems().addAll(Donation.DonationType.values());
		
		this.NewDonationFormeComboBox.getItems().addAll(Donation.DonationForm.values());
		this.NewDonationFormeComboBox.setConverter(DonationsPanelController.DONATION_TYPE_STRING_CONVERTER);
		
		this.SearchDonationTypeComboBox.getItems().addAll(DonationType.values());
		this.SearchDonationTypeComboBox.getSelectionModel().selectedItemProperty().addListener(event -> { refreshDonations(); });
	}
	
	private void initDatePickers() {
		this.SearchDonationDateDebutDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				if(newValue != null) refreshDonations();
			}
		});
		
		this.SearchDonationDateFinDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				if(newValue != null) refreshDonations();
			}
		});

	}
	
	private void initTableView() {
		this.DonationsTableView.setItems(DonationsPanelController.DONATIONS);
		this.DonationsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Donation>() {
			@Override
			public void changed(ObservableValue<? extends Donation> observable, Donation oldValue, Donation newValue) {
				if(newValue != null) UpdateDonationObservationTextArea.setText(newValue.getObservation());
				else UpdateDonationObservationTextArea.setText("");
			}
		});
		
		TableColumn<Donation, Integer> idColumn =  ((TableColumn<Donation,Integer>) this.DonationsTableView.getColumns().get(0));
									   idColumn.setCellValueFactory(donation -> {
										   return new SimpleIntegerProperty(donation.getValue().getId()).asObject();
									   });
									   
		TableColumn<Donation, DonationType> typeColumn =  ((TableColumn<Donation, DonationType>) this.DonationsTableView.getColumns().get(1));
											typeColumn.setCellValueFactory(donation -> {
										   		return new SimpleObjectProperty(donation.getValue().getType());
										   	});
										
		TableColumn<Donation, DonationForm> formColumn =  ((TableColumn<Donation, DonationForm>) this.DonationsTableView.getColumns().get(2));
											formColumn.setCellValueFactory(donation -> {
										   		return new SimpleObjectProperty(donation.getValue().getForme());
										   	});
											formColumn.setCellFactory(TextFieldTableCell.forTableColumn(DonationsPanelController.DONATION_TYPE_STRING_CONVERTER));
											
		TableColumn<Donation, String> valeurColumn =  ((TableColumn<Donation, String>) this.DonationsTableView.getColumns().get(3));
									  valeurColumn.setCellValueFactory(donation -> {
										  return new SimpleStringProperty(donation.getValue().getValeur());
									  });
									  
		TableColumn<Donation, LocalDate> dateColumn =  ((TableColumn<Donation, LocalDate>) this.DonationsTableView.getColumns().get(4));
										 dateColumn.setCellValueFactory(donation -> {
											 return new SimpleObjectProperty(donation.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
									   	 });
										 
		TableColumn<Donation, Donator> donateurColumn =  ((TableColumn<Donation, Donator>) this.DonationsTableView.getColumns().get(5));
									   donateurColumn.setCellValueFactory(donation -> {
										   return new SimpleObjectProperty(donation.getValue().getDonator());
									  });
	}
	
	private void initHamburgers() {        
        HamburgerSlideCloseTransition leftHamburgerAnimator = new HamburgerSlideCloseTransition(this.leftHamburger);
        							  leftHamburgerAnimator.setRate(-1);
		  
		this.leftHamburger.setAnimation(leftHamburgerAnimator);
		this.leftHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
			this.leftHamburger.getAnimation().setRate(this.leftHamburger.getAnimation().getRate() * -1);
			this.leftHamburger.getAnimation().play();
				            
			if(this.leftDrawer.isShown()) 
				this.leftDrawer.close();
			else this.leftDrawer.open();
		});
	}

	private void initInitialDatas() {
		DonationsPanelController.DONATIONS.clear();
		DonationsPanelController.DONATIONS.addAll(UtilitiesHolder.DONATION_DAO.getAllDonations());
		
		DonationsPanelController.DONATORS.clear();
		DonationsPanelController.DONATORS.addAll(UtilitiesHolder.DONATION_DAO.getAllDonators());
		
		resultCount.setText("Nombre de donations: " + DonationsPanelController.DONATIONS.size());
	}
	
	public static StringConverter<DonationForm> DONATION_TYPE_STRING_CONVERTER = new StringConverter<DonationForm>() {
		@Override
		public DonationForm fromString(String arg0) {
			return null;
		}

		@Override
		public String toString(DonationForm arg0) {
			return arg0.toString().replaceAll("_", " ");
		}
	};
}
