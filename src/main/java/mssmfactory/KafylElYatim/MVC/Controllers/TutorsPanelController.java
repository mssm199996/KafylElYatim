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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Appareillage;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.DossierMedical;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Enquete;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.EtatHabitat;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.TypeBien;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.SituationSociale;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.SituationSociale.NiveauVie;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class TutorsPanelController implements Initializable{
	public static ObservableList<Tuteur> TUTEURS = FXCollections.observableList(new LinkedList<Tuteur>());
	public static ObservableList<Appareillage> APPAREILLAGES = FXCollections.observableList(new LinkedList<Appareillage>());

	@FXML private JFXHamburger leftHamburger, rightHamburger;
    @FXML private JFXDrawer leftPane, rightPane;
    @FXML private JFXTextField newNomTuteur, newPrenomTuteur, newTelFixTuteur, newTelPortTuteur, newCNITuteur, newCCPTuteur,
    					 majBanqTuteur, majCCPTuteur, majFonctionSS, majNiveauEtudeSS, majRetraiteSS, majCouvertureSS, majSalaireSS, majNomEnqueteur,
    					 searchNomTuteur, majCniTuteur, majPrenomEnqueteur, updateClassementHabitat, majNDossier;
    @FXML private JFXDatePicker newDdnTuteur, majDdnEnquete;
    @FXML private JFXComboBox<Region> newRegionTuteur, searchRegionTuteur;  
    @FXML private JFXTextArea newAdresseTuteur, majCompetancesSS, updatePathologie, updateMedicament, updateObservationTuteur;
    @FXML private TableView<Tuteur> TuteursTableView;
    @FXML private JFXComboBox<EtatHabitat> majEtatHabitat, searchEtatHabitat;
    @FXML private JFXComboBox<TypeBien> majTypeBienHabitat, searchTypeHabitat;
    @FXML private JFXComboBox<NiveauVie> majNiveauDeVieSS, searchNiveauVieTuteur, newNiveauVie; 
	@FXML private Label resultCount;
	@FXML private ListView<Appareillage> listeAppareillages;
    
	@FXML 
    private void addNewTuteur() {
    	String tutorName = this.newNomTuteur.getText();
    	String tutorPrenom = this.newPrenomTuteur.getText();
    	String tutorTelFix = this.newTelFixTuteur.getText();
    	String tutorTelPort = this.newTelPortTuteur.getText();
    	String tutorCNI = this.newCNITuteur.getText();
    	String tutorCCP = this.newCCPTuteur.getText();
    	LocalDate tutorDdn = this.newDdnTuteur.getValue();
    	Region tutorRegion = this.newRegionTuteur.getValue();
    	String tutorAdress = this.newAdresseTuteur.getText();
    	NiveauVie niveauVie = this.newNiveauVie.getValue();
    	    	
    	if(tutorDdn == null || LocalDate.now().compareTo(tutorDdn) < 0 || tutorName == null || tutorName.equals("") || tutorPrenom == null || tutorPrenom.equals("") || tutorRegion == null) {
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur de saisi", "Veuillez verifier les informations");
			return;
		}
    	
    	(new Thread(new Runnable() {
			@Override
			public void run() {
				Habitat habitat = new Habitat();
		    			habitat.setEtat(null);
		    			habitat.setTypeBien(null);
		    			habitat.setClassementHabitat("");
    	
		    	Enquete enquete = new Enquete();
		    			enquete.setDateEnquete(null);
		    			enquete.setNomEnqueteur("");
		    			enquete.setPrenomEnqueteur("");
		    			
		    	SituationSociale situationSociale = new SituationSociale();
		    					 situationSociale.setCompetances("");
		    					 situationSociale.setConvertureSociale(0.0);
		    					 situationSociale.setFonction("");
		    					 situationSociale.setNiveauEtude("");
		    					 situationSociale.setNiveauVie(niveauVie);
		    					 situationSociale.setRetraite(0.0);
		    					 situationSociale.setSalaire(0.0);
		    					 
		    	DossierMedical dossierMedical = new DossierMedical();
		    				   dossierMedical.setMedicaments("");
		    				   dossierMedical.setPathologie("");
		    					 
		    	Tuteur tuteur = new Tuteur();
		    		   tuteur.setAdresse(tutorAdress);
		    		   tuteur.setDdn(tutorDdn);
		    		   tuteur.setNccp(tutorCCP);
		    		   tuteur.setNcni(tutorCNI);
		    		   tuteur.setNom(tutorName);
		    		   tuteur.setnTelFix(tutorTelFix);
		    		   tuteur.setnTelMob(tutorTelPort);
		    		   tuteur.setPrenom(tutorPrenom);
		    		   tuteur.setRegion(tutorRegion);
		    		   tuteur.setNbOrphelins(0);
		    		   tuteur.setnDossierBureautique("");
		    		   tuteur.setnTel1("");
		    		   tuteur.setnTel2("");
		    		   tuteur.setNomPereOrphelins("");
		    		   tuteur.setPrenomPereOrphelins("");
		    		   tuteur.setnCarteBanquaine("");
		    		   
		    	situationSociale.setTuteur(tuteur);	   
		    	situationSociale.setEnquete(enquete);    	
		    	
		    	tuteur.setSituationSociale(situationSociale);
		    	tuteur.setHabitat(habitat);
		    	tuteur.setDossierMedical(dossierMedical);
		    	
		    	habitat.setTuteur(tuteur);
		    	
		    	enquete.setSituationSociale(situationSociale);
		    	
		    	dossierMedical.setTuteur(tuteur);
		    	
		    	UtilitiesHolder.TUTEUR_DAO.insertTuteur(tuteur);
				
				Platform.runLater(new Runnable() {					
					@Override
					public void run() { refreshTutors(); }
				});
			}
    	})).start();
    }

    @FXML
    private void updateDossierMedical() {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
    	
    	if(tuteur != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de mise à jour", "Confirmer pour proceder à la mise à jour"))
    			return;
    		
    		tuteur.getDossierMedical().setMedicaments(this.updateMedicament.getText());
    		tuteur.getDossierMedical().setPathologie(this.updatePathologie.getText());
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() { UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur); }
    		})).start();
    	}
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Tuteurs", "Message d'erreur", "Veuillez d'abords selectionner un tuteur !");
    }
    
    @FXML
    private void insertNewAppareillage() {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
		
		if(tuteur != null) {		
			HBox dialogContainer = UtilitiesHolder.DIALOGS_BUILDER.getAppareillagesDialog();
			
			JFXComboBox<Appareillage> appareillagesNode = (JFXComboBox<Appareillage>) dialogContainer.getChildren().get(1);
								  	  appareillagesNode.setItems(FXCollections.observableArrayList(ConfigurationStageController.APPAREILLAGE));
								  	  appareillagesNode.getSelectionModel().select(0);
				
			Alert alert = new Alert(AlertType.CONFIRMATION);
			   	  alert.setTitle("Nouvelle affection d'appareillage");
			   	  alert.setHeaderText("Formulaire d'ajout d'une nouvelle affectation d'appareillage");
			   	  alert.getDialogPane().setContent(dialogContainer);
			   	  alert.showAndWait();
			
			Appareillage appareillage = appareillagesNode.getSelectionModel().getSelectedItem();
			   	  
			if (appareillage != null && alert.getResult() == ButtonType.OK){
				(new Thread(new Runnable() {
					@Override
					public void run() {
						UtilitiesHolder.TUTEUR_DAO.addNewAppareillage(tuteur, appareillage);
						
						Platform.runLater(new Runnable() {
							@Override
							public void run() { TutorsPanelController.APPAREILLAGES.add(appareillage); }
						});
					}
				})).start();
			}
		    else if(appareillage == null)
		    	UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Tuteurs", "Message d'erreur", "Aucun appareil selectionné !");
		}
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Tuteurs", "Message d'erreur", "Veuillez d'abords selectionner un tuteur !");
    }
    
    @FXML
    private void deleteAppareillage() {
    	Appareillage appareillage = this.listeAppareillages.getSelectionModel().getSelectedItem();
	    
	    if(appareillage != null) {
	    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
	    	
	    	if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de suppression", "Veuillez confirmer pour proceder à la suppression."))
	    		return;
	    		
	    	(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.TUTEUR_DAO.deleteAppareillage(tuteur, appareillage);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() { TutorsPanelController.APPAREILLAGES.remove(appareillage); }
					});
				}
	    		
	    	})).start();
	    }
	    else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Message d'erreur", "Veuillez d'abords selectionner un appareil !");
    }
    
    @FXML
    private void updateAdditionalInformations() {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
    	
    	if(tuteur != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de mise à jour", "Veuillez confirmer pour proceder à la mise à jour"))
    			return;
    		
    		tuteur.setNccp(this.majCCPTuteur.getText());
    		tuteur.setnCarteBanquaine(this.majBanqTuteur.getText());
    		tuteur.setNcni(this.majCniTuteur.getText());
    		tuteur.setnDossierBureautique(this.majNDossier.getText());
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() { UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur); }
    		})).start();
    	}
    	else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur de mise à jour", "Veuillez d'abords selectionner un tuteur !");
    }

    @FXML
    private void updateEnquete() {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
    	
    	if(tuteur != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de mise à jour", "Veuillez confirmer pour proceder à la mise à jour"))
    			return;
    		
    		tuteur.getSituationSociale().getEnquete().setDateEnquete(this.majDdnEnquete.getValue());
    		tuteur.getSituationSociale().getEnquete().setNomEnqueteur(this.majNomEnqueteur.getText());
    		tuteur.getSituationSociale().getEnquete().setPrenomEnqueteur(this.majPrenomEnqueteur.getText());
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() { UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur); }
    		})).start();
    	}
    	else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur de mise à jour", "Veuillez d'abords selectionner un tuteur !");
    }

    @FXML
    private void updateHabitat(ActionEvent event) {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
    	
    	if(tuteur != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de mise à jour", "Veuillez confirmer pour proceder à la mise à jour"))
    			return;
    		
    		tuteur.getHabitat().setEtat(this.majEtatHabitat.getValue());
    		tuteur.getHabitat().setTypeBien(this.majTypeBienHabitat.getValue());
    		tuteur.getHabitat().setClassementHabitat(this.updateClassementHabitat.getText());
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() { UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur); }
    		})).start();
    	}
    	else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur de mise à jour", "Veuillez d'abords selectionner un tuteur !");
    }

    @FXML
    private void updateSituationSociale(ActionEvent event) {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
    	
    	if(tuteur != null) {    		
    		if((tuteur.getSituationSociale().getNiveauVie() == null && this.majNiveauDeVieSS.getValue() == null) || 
    				(tuteur.getSituationSociale().getNiveauVie() != null && this.majNiveauDeVieSS.getValue() != null && 
    					tuteur.getSituationSociale().getNiveauVie().equals(this.majNiveauDeVieSS.getValue()))) {
    			if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de mise à jour", "Veuillez confirmer pour proceder à la mise à jour"))
    				return;
    		}
    		else if(!MainWindowController.IS_SU) {
    			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur d'authentification", "Veuillez vous connecter en tant que super utilisateur");
    			return;
    		}
    			
    		
    		tuteur.getSituationSociale().setNiveauVie(this.majNiveauDeVieSS.getValue());
    		tuteur.getSituationSociale().setFonction(this.majFonctionSS.getText());
    		tuteur.getSituationSociale().setRetraite(Double.parseDouble(this.majRetraiteSS.getText()));
    		tuteur.getSituationSociale().setConvertureSociale(Double.parseDouble(this.majCouvertureSS.getText()));
    		tuteur.getSituationSociale().setSalaire(Double.parseDouble(this.majSalaireSS.getText()));
    		tuteur.getSituationSociale().setNiveauEtude(this.majNiveauEtudeSS.getText());
    		tuteur.getSituationSociale().setCompetances(this.majCompetancesSS.getText());
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() { UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur); }
    		})).start();
    	}
    	else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur de mise à jour", "Veuillez d'abords selectionner un tuteur !");
    }
    
    @FXML
    private void updateObservations() {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();
    	
    	if(tuteur != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de mise à jour", "Veuillez confirmer pour proceder à la mise à jour"))
    			return;
    		
    		tuteur.setObservation(this.updateObservationTuteur.getText());
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() { UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur); }
    		})).start();
    	}
    	else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur de mise à jour", "Veuillez d'abords selectionner un tuteur !");
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
						resultCount.setText("Nombre de tuteurs: " + TutorsPanelController.TUTEURS.size());
					}
				});
			}
    	})).start();
    }
    
    @FXML
    private void reset() {
    	this.searchNomTuteur.setText("");
    	this.searchRegionTuteur.getSelectionModel().clearSelection();
    	this.searchNiveauVieTuteur.getSelectionModel().clearSelection();
    	this.searchEtatHabitat.getSelectionModel().clearSelection();
    	this.searchTypeHabitat.getSelectionModel().clearSelection();
    	
    	this.refreshTutors();
    }
    
    @FXML
    private void deleteTuteur() {
    	Tuteur tuteur = this.TuteursTableView.getSelectionModel().getSelectedItem();

    	if(tuteur != null) {
    		if(!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des tuteurs", "Confirmation de suppression", "Veuillez confirmer pour proceder à la suppression"))
    			return;
    		
    		(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.TUTEUR_DAO.deleteTuteur(tuteur);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() { TutorsPanelController.TUTEURS.remove(tuteur); }
					});
				}
    		})).start();    		
    	}
    	else UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des tuteurs", "Erreur suppression", "Veuillez d'abords selectionner un tuteur !");
    }

    @FXML
    private void printTutors() {
		if(UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des donations", "Message de confirmation", "Confirmer pour proceder à l'impression des tuteurs"))
			UtilitiesHolder.PRINTING_HANDLER.printTutors(TutorsPanelController.TUTEURS);
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initHamburgers();
		this.initInitialDatas();
		this.initComboBox();
		this.initLists();
		this.initTables();
		this.initComponents();
	}
	
	private void initLists() {
		this.listeAppareillages.setItems(TutorsPanelController.APPAREILLAGES);
	}
	
	private void initComboBox() {
		this.newRegionTuteur.setItems(ConfigurationStageController.REGION); 
		this.majEtatHabitat.setItems(FXCollections.observableArrayList(EtatHabitat.values()));
		this.majTypeBienHabitat.setItems(FXCollections.observableArrayList(TypeBien.values()));
		this.majNiveauDeVieSS.setItems(FXCollections.observableArrayList(NiveauVie.values()));
		this.newNiveauVie.setItems(FXCollections.observableArrayList(NiveauVie.values()));
		
		this.searchRegionTuteur.setItems(ConfigurationStageController.REGION);
		this.searchRegionTuteur.getSelectionModel().selectedItemProperty().addListener(event -> { refreshTutors(); });
		
		this.searchNiveauVieTuteur.setItems(FXCollections.observableArrayList(NiveauVie.values()));
		this.searchNiveauVieTuteur.getSelectionModel().selectedItemProperty().addListener(event -> { refreshTutors(); });
	
		this.searchTypeHabitat.setItems(FXCollections.observableArrayList(TypeBien.values())); 
		this.searchTypeHabitat.getSelectionModel().selectedItemProperty().addListener(event -> { refreshTutors(); });
		
		this.searchEtatHabitat.setItems(FXCollections.observableArrayList(EtatHabitat.values()));
		this.searchEtatHabitat.getSelectionModel().selectedItemProperty().addListener(event -> { refreshTutors(); });
	}
	
	private void initTables() {
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
        
		this.TuteursTableView.setItems(TutorsPanelController.TUTEURS);
		this.TuteursTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tuteur>() {
			@Override
			public void changed(ObservableValue<? extends Tuteur> observable, Tuteur oldValue, Tuteur newValue) {
				TutorsPanelController.APPAREILLAGES.clear();;

				if(newValue == null) {
					majBanqTuteur.setText("");
					majCCPTuteur.setText("");
					majFonctionSS.setText("");
					majNiveauEtudeSS.setText("");
					majRetraiteSS.setText("");
					majCouvertureSS.setText("");
					majSalaireSS.setText("");
					majNomEnqueteur.setText("");
					majDdnEnquete.setValue(null); 
					majTypeBienHabitat.setValue(null); 
					majEtatHabitat.setValue(null);
					majNiveauDeVieSS.setValue(null);
					majCompetancesSS.setText("");
					majCniTuteur.setText("");
					majPrenomEnqueteur.setText("");
					updatePathologie.setText("");
					updateMedicament.setText("");
					updateClassementHabitat.setText("");
					updateObservationTuteur.setText("");
					majNDossier.setText("");
				}
				else {
					DossierMedical dossierMedical = newValue.getDossierMedical();
					
					majBanqTuteur.setText(newValue.getnCarteBanquaine());
					majCCPTuteur.setText(newValue.getNccp());
					majFonctionSS.setText(newValue.getSituationSociale().getFonction());
					majNiveauEtudeSS.setText(newValue.getSituationSociale().getNiveauEtude());
					majRetraiteSS.setText(newValue.getSituationSociale().getRetraite() + "");
					majCouvertureSS.setText(newValue.getSituationSociale().getConvertureSociale() + "");
					majSalaireSS.setText(newValue.getSituationSociale().getSalaire() + "");
					majNomEnqueteur.setText(newValue.getSituationSociale().getEnquete().getNomEnqueteur());
					majDdnEnquete.setValue(newValue.getSituationSociale().getEnquete().getDateEnquete()); 
					majTypeBienHabitat.setValue(newValue.getHabitat().getTypeBien()); 
					majEtatHabitat.setValue(newValue.getHabitat().getEtat());
					majNiveauDeVieSS.setValue(newValue.getSituationSociale().getNiveauVie());
					majCompetancesSS.setText(newValue.getSituationSociale().getCompetances());
					majCniTuteur.setText(newValue.getNcni());
					majPrenomEnqueteur.setText(newValue.getSituationSociale().getEnquete().getPrenomEnqueteur());
					updatePathologie.setText(dossierMedical.getPathologie());
					updateMedicament.setText(dossierMedical.getMedicaments());
					updateClassementHabitat.setText(newValue.getHabitat().getClassementHabitat());
					updateObservationTuteur.setText(newValue.getObservation());
					majNDossier.setText(newValue.getnDossierBureautique());
					
					TutorsPanelController.APPAREILLAGES.addAll(UtilitiesHolder.TUTEUR_DAO.getAppareillages(newValue));
				}
			}
		});
		
		TableColumn<Tuteur, Integer> idColumn =  ((TableColumn<Tuteur,Integer>) this.TuteursTableView.getColumns().get(0));
									 idColumn.setCellValueFactory(tuteur -> {
										 return new SimpleIntegerProperty(tuteur.getValue().getId()).asObject();
									 });
									 
		TableColumn<Tuteur, String> nomColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(1));
									nomColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getNom());
									});
									nomColumn.setCellFactory(event -> {
										TextFieldTableCell<Tuteur, String> component = new TextFieldTableCell<Tuteur, String>(new DefaultStringConverter()) {
											@Override
											public void commitEdit(String newValue) {
												Tuteur tuteur = this.getTableView().getItems().get(this.getTableRow().getIndex());
												tuteur.setNom(newValue);
												
												UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur);
												super.commitEdit(newValue);
											}
										};

										return component;
									});
									
		TableColumn<Tuteur, String> prenomColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(2));
									prenomColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getPrenom());
									});
									prenomColumn.setCellFactory(event -> {
										TextFieldTableCell<Tuteur, String> component = new TextFieldTableCell<Tuteur, String>(new DefaultStringConverter()) {
											@Override
											public void commitEdit(String newValue) {
												Tuteur tuteur = this.getTableView().getItems().get(this.getTableRow().getIndex());
												tuteur.setPrenom(newValue);
												
												UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur);
												super.commitEdit(newValue);
											}
										};

										return component;
									});
									
		TableColumn<Tuteur, LocalDate> ddnColumn =  ((TableColumn<Tuteur,LocalDate>) this.TuteursTableView.getColumns().get(3));
									   ddnColumn.setCellValueFactory(tuteur -> {
										   return new SimpleObjectProperty(tuteur.getValue().getDdn().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
									   });
									   
	    TableColumn<Tuteur, Region> regionColumn =  ((TableColumn<Tuteur,Region>) this.TuteursTableView.getColumns().get(4));
									regionColumn.setCellValueFactory(tuteur -> {
										return new SimpleObjectProperty(tuteur.getValue().getRegion());
									});
									regionColumn.setCellFactory(event -> {return new ComboBoxTableCell<>(ConfigurationStageController.REGION);});
									regionColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, Region>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, Region> event) {
											event.getRowValue().setRegion(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, Integer> nbOrphelinColumn =  ((TableColumn<Tuteur,Integer>) this.TuteursTableView.getColumns().get(5));
									 nbOrphelinColumn.setCellValueFactory(tuteur -> {
										 return new SimpleIntegerProperty(tuteur.getValue().getNbOrphelins()).asObject();
									 });
									
		TableColumn<Tuteur, String> addrColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(6));
									addrColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getAdresse());
									});
									addrColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									addrColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setAdresse(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> addr2Column =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(7));
									addr2Column.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getSecondeAdresse());
									});
									addr2Column.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									addr2Column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setSecondeAdresse(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> nTelFixColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(8));
									nTelFixColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getnTelFix());
									});
									nTelFixColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									nTelFixColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setnTelFix(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> nTelMobColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(9));
									nTelMobColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getnTelMob());
									});
									nTelMobColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									nTelMobColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setnTelMob(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> nTel1Column =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(10));
									nTel1Column.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getnTel1());
									});
									nTel1Column.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									nTel1Column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setnTel1(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> nTel2Column =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(11));
									nTel2Column.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getnTel2());
									});
									nTel2Column.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									nTel2Column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setnTel2(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> nomPereOrphelinsColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(12));
									nomPereOrphelinsColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getNomPereOrphelins());
									});
									nomPereOrphelinsColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									nomPereOrphelinsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setNomPereOrphelins(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
									
		TableColumn<Tuteur, String> prenomPereOrphelinsColumn =  ((TableColumn<Tuteur,String>) this.TuteursTableView.getColumns().get(13));
									prenomPereOrphelinsColumn.setCellValueFactory(tuteur -> {
										return new SimpleStringProperty(tuteur.getValue().getPrenomPereOrphelins());
									});
									prenomPereOrphelinsColumn.setCellFactory(event -> {return new TextFieldTableCell<>(stringConverter);});
									prenomPereOrphelinsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tuteur, String>>() {
										@Override
								        public void handle(TableColumn.CellEditEvent<Tuteur, String> event) {
											event.getRowValue().setPrenomPereOrphelins(event.getNewValue());
								            UtilitiesHolder.TUTEUR_DAO.updateTuteur(event.getRowValue());
										}
								    });
	}
	
	private void initHamburgers() {
        HamburgerSlideCloseTransition leftHamburgerAnimator = new HamburgerSlideCloseTransition(this.leftHamburger);
        							  leftHamburgerAnimator.setRate(-1);
		  
		this.leftHamburger.setAnimation(leftHamburgerAnimator);
		this.leftHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
			this.leftHamburger.getAnimation().setRate(this.leftHamburger.getAnimation().getRate() * -1);
			this.leftHamburger.getAnimation().play();
				            
			if(this.leftPane.isShown()) 
				this.leftPane.close();
			else this.leftPane.open();			
		});
		
		// -----------------------------------------------------------------------------------------------------------
		
		HamburgerSlideCloseTransition rightHamburgerAnimator = new HamburgerSlideCloseTransition(this.rightHamburger);
						  			  rightHamburgerAnimator.setRate(-1);
				
		this.rightHamburger.setAnimation(rightHamburgerAnimator);
		this.rightHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
		this.rightHamburger.getAnimation().setRate(this.rightHamburger.getAnimation().getRate() * -1);
		this.rightHamburger.getAnimation().play();
			if(this.rightPane.isShown()) 
				this.rightPane.close();
			else this.rightPane.open();				
		});
	}
	
	private void initInitialDatas() {
		TutorsPanelController.TUTEURS.clear();
		TutorsPanelController.TUTEURS.addAll(UtilitiesHolder.TUTEUR_DAO.getAllTuteurs());
		resultCount.setText("Nombre de tuteurs: " + TutorsPanelController.TUTEURS.size());
	}
	
	private void initComponents() {
		ComponentsHolder.SEARCH_TUTORS = this.searchNomTuteur;
		ComponentsHolder.SEARCH_REGION_TUTEUR = this.searchRegionTuteur;
		ComponentsHolder.SEARCH_NIVEAU_VIE_TUTEUR = this.searchNiveauVieTuteur;
		ComponentsHolder.SEARCH_TYPE_HABITAT = this.searchTypeHabitat;
		ComponentsHolder.SEARCH_ETAT_HABITAT = this.searchEtatHabitat;
	}
}
