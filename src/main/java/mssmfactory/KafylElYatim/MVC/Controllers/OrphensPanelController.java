package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Appareillage;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.ApparencePhysique;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierFamilial;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierFamilial.TypeOrphelinat;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierMedical;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierScolaire;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierScolaire.NiveauScolaire;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin.Genre;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class OrphensPanelController implements Initializable {

	public static ObservableList<Orphelin> ORPHELINS = FXCollections.observableList(new LinkedList<Orphelin>());
	public static ObservableList<Appareillage> APPAREILLAGES = FXCollections
			.observableList(new LinkedList<Appareillage>());
	public static ObservableList<Orphelin> ORPHELINS_AGES = FXCollections.observableList(new LinkedList<Orphelin>());
	public static ObservableList<Orphelin> ORPHELINS_AGES_AUTHORIZED = FXCollections
			.observableList(new LinkedList<Orphelin>());

	public static int MALE_AGE_LIMIT = 19;
	public static int FEMALE_AGE_LIMIT = 21;

	@FXML
	private JFXTextField searchNomOrphelin, searchAgeMinOrphelin, searchAgeMaxOrphelin, newNomOrphelin,
			newPrenomOrphelin, updateNomPere, updatePrenomPere, updateNomMere, updatePrenomMere, updatePointure,
			searchNomTuteur, newRelationTuteurOrphelin, updateSituationSociale;
	@FXML
	private JFXComboBox<Genre> searchSexeOrphelin;
	@FXML
	private JFXDatePicker newDdnOrphelin, updateDDMAJDS, updateDDMAJAP, dateMin, dateMax;
	@FXML
	private ToggleGroup newSexeOrphelin;
	@FXML
	private JFXComboBox<TypeOrphelinat> updateTypeOrphelinat;
	@FXML
	private JFXComboBox<NiveauScolaire> updateNiveauScolaire, searchNiveauScolaire;
	@FXML
	private JFXComboBox<Integer> updateAnneeScolaire, searchAnneeScolaire;
	@FXML
	private JFXTextArea updatePathologie, updateMedicament, updateObservationAP, updateObservationsOrphelin,
			updateObservationDossierScolaire;
	@FXML
	private ListView<Appareillage> listeAppareillages;
	@FXML
	private ListView<Tuteur> listeTuteurs;
	@FXML
	private ListView<Orphelin> listeOrphelinsAges;
	@FXML
	private TableView<Orphelin> tableOrphelins;
	@FXML
	private JFXHamburger leftHamburger, rightHamburger;
	@FXML
	private JFXDrawer leftDrawer, rightDrawer;
	@FXML
	private Label resultCount;
	@FXML
	private JFXCheckBox displayAll;

	@FXML
	private void deleteAppareillage() {
		Appareillage appareillage = this.listeAppareillages.getSelectionModel().getSelectedItem();

		if (appareillage != null) {
			Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de suppression", "Veuillez confirmer pour proceder à la suppression."))
				return;

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.ORPHELIN_DAO.deleteAppareillage(orphelin, appareillage);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							OrphensPanelController.APPAREILLAGES.remove(appareillage);
						}
					});
				}

			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un appareil !");
	}

	@FXML
	private void insertNewAppareillage() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			HBox dialogContainer = UtilitiesHolder.DIALOGS_BUILDER.getAppareillagesDialog();

			JFXComboBox<Appareillage> appareillagesNode = (JFXComboBox<Appareillage>) dialogContainer.getChildren()
					.get(1);
			appareillagesNode.setItems(FXCollections.observableArrayList(ConfigurationStageController.APPAREILLAGE));
			appareillagesNode.getSelectionModel().select(0);

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Nouvelle affection d'appareillage");
			alert.setHeaderText("Formulaire d'ajout d'une nouvelle affectation d'appareillage");
			alert.getDialogPane().setContent(dialogContainer);
			alert.showAndWait();

			Appareillage appareillage = appareillagesNode.getSelectionModel().getSelectedItem();

			if (appareillage != null && alert.getResult() == ButtonType.OK) {
				(new Thread(new Runnable() {
					@Override
					public void run() {
						UtilitiesHolder.ORPHELIN_DAO.addNewAppareillage(orphelin, appareillage);

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								OrphensPanelController.APPAREILLAGES.add(appareillage);
							}
						});
					}
				})).start();
			} else if (appareillage == null)
				UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
						"Aucun appareil selectionné !");
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void insertNewOrphelin() {
		Tuteur tuteur = this.listeTuteurs.getSelectionModel().getSelectedItem();

		if (tuteur != null) {
			String nomOrphelin = this.newNomOrphelin.getText();
			String prenomOrphelin = this.newPrenomOrphelin.getText();
			Genre genreOrphelin = this.newSexeOrphelin.getSelectedToggle()
					.equals(this.newSexeOrphelin.getToggles().get(0)) ? Genre.Masculin : Genre.Feminin;
			LocalDate ddnOrphelin = this.newDdnOrphelin.getValue();
			String relationOrphelinTuteur = this.newRelationTuteurOrphelin.getText();

			if (nomOrphelin.equals("") || prenomOrphelin.equals("") || ddnOrphelin == null
					|| LocalDate.now().compareTo(ddnOrphelin) < 0) {
				UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Erreur de saisi",
						"Veuillez revoir le formulaire d'inscription");
				return;
			}

			(new Thread(new Runnable() {
				@Override
				public void run() {
					DossierScolaire dossierScolaire = new DossierScolaire();
					dossierScolaire.setDdMaj(null);
					dossierScolaire.setNiveauScolaire(null);
					dossierScolaire.setScolarise(false);
					dossierScolaire.setAnneeScolaire(-1);

					DossierFamilial dossierFamilial = new DossierFamilial();
					dossierFamilial.setNomMere("");
					dossierFamilial.setNomPere("");
					dossierFamilial.setPrenomMere("");
					dossierFamilial.setPrenomPere("");
					dossierFamilial.setTypeOrphelinat(null);

					DossierMedical dossierMedical = new DossierMedical();
					dossierMedical.setMedicaments("");
					dossierMedical.setPathologie("");

					ApparencePhysique apparencePhysique = new ApparencePhysique();
					apparencePhysique.setDdMaj(null);
					apparencePhysique.setObservation("");
					apparencePhysique.setPointure("");

					Orphelin orphelin = new Orphelin();
					orphelin.setDdn(ddnOrphelin);
					orphelin.setGenre(genreOrphelin);
					orphelin.setNom(nomOrphelin);
					orphelin.setObsevation("");
					orphelin.setPrenom(prenomOrphelin);
					orphelin.setRelationOrphelinTuteur(relationOrphelinTuteur);

					apparencePhysique.setOrphelin(orphelin);
					dossierMedical.setOrphelin(orphelin);
					dossierFamilial.setOrphelin(orphelin);
					dossierScolaire.setOrphelin(orphelin);

					orphelin.setApparencePhysique(apparencePhysique);
					orphelin.setDossierMedical(dossierMedical);
					orphelin.setDossierFamilial(dossierFamilial);
					orphelin.setDossierScolaire(dossierScolaire);
					orphelin.setTuteur(tuteur);
					orphelin.setAuthorized(false);

					UtilitiesHolder.ORPHELIN_DAO.insertOrphelin(orphelin);

					tuteur.setNbOrphelins(tuteur.getNbOrphelins() + 1);

					UtilitiesHolder.TUTEUR_DAO.updateTuteur(tuteur);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							searchOrphelins();

							OrphensPanelController.ORPHELINS_AGES.clear();
							OrphensPanelController.ORPHELINS_AGES.addAll(
									UtilitiesHolder.ORPHELIN_DAO.getAgedOrphelins(FEMALE_AGE_LIMIT, Genre.Feminin));
							OrphensPanelController.ORPHELINS_AGES.addAll(
									UtilitiesHolder.ORPHELIN_DAO.getAgedOrphelins(MALE_AGE_LIMIT, Genre.Masculin));
						}
					});
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un tuteur !");
	}

	@FXML
	private void searchOrphelins() {
		Tuteur tuteur = this.listeTuteurs.getSelectionModel().getSelectedItem();

		String names = this.searchNomOrphelin.getText();

		Genre gender = this.searchSexeOrphelin.getSelectionModel().getSelectedItem();

		NiveauScolaire niveauScolaire = this.searchNiveauScolaire.getSelectionModel().getSelectedItem();

		Integer anneeScolaire = this.searchAnneeScolaire.getSelectionModel().getSelectedItem();

		LocalDate dateMin = this.dateMin.getValue();
		LocalDate dateMax = this.dateMax.getValue();

		int ageMin = -1;
		int ageMax = -1;

		try {
			ageMin = Integer.parseInt(this.searchAgeMinOrphelin.getText());
		} catch (NumberFormatException exp) {
		}
		try {
			ageMax = Integer.parseInt(this.searchAgeMaxOrphelin.getText());
		} catch (NumberFormatException exp) {
		}

		final int ageMinFin = ageMin;
		final int ageMaxFin = ageMax;

		boolean displayAll = this.displayAll.isSelected();

		(new Thread(new Runnable() {
			@Override
			public void run() {
				long t0 = System.currentTimeMillis();

				List<Orphelin> result = UtilitiesHolder.ORPHELIN_DAO.getSpecifiedOrphelins(tuteur, names, gender,
						ageMinFin, ageMaxFin, niveauScolaire, anneeScolaire, dateMin, dateMax, displayAll);

				long t = System.currentTimeMillis();

				System.out.println("Ellipsed time: " + (t - t0));

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						OrphensPanelController.ORPHELINS.clear();
						OrphensPanelController.ORPHELINS.addAll(result);
						resultCount.setText("Nombre d'orphelins: " + OrphensPanelController.ORPHELINS.size());
					}
				});
			}
		})).start();
	}

	@FXML
	private void searchTuteurs() {
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

	@FXML
	private void updateDossierFamillial() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de mise à jour", "Confirmer pour proceder à la mise à jour"))
				return;

			orphelin.getDossierFamilial().setNomMere(this.updateNomMere.getText());
			orphelin.getDossierFamilial().setNomPere(this.updateNomPere.getText());
			orphelin.getDossierFamilial().setPrenomMere(this.updatePrenomMere.getText());
			orphelin.getDossierFamilial().setPrenomPere(this.updatePrenomPere.getText());
			orphelin.getDossierFamilial().setSituationFamilliale(this.updateSituationSociale.getText());
			orphelin.getDossierFamilial()
					.setTypeOrphelinat(this.updateTypeOrphelinat.getSelectionModel().getSelectedItem());

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void updateDossierMedical() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de mise à jour", "Confirmer pour proceder à la mise à jour"))
				return;

			orphelin.getDossierMedical().setMedicaments(this.updateMedicament.getText());
			orphelin.getDossierMedical().setPathologie(this.updatePathologie.getText());

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void updateDossierScolaire() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de mise à jour", "Confirmer pour proceder à la mise à jour"))
				return;

			orphelin.getDossierScolaire().setDdMaj(this.updateDDMAJDS.getValue());
			orphelin.getDossierScolaire()
					.setNiveauScolaire(this.updateNiveauScolaire.getSelectionModel().getSelectedItem());
			orphelin.getDossierScolaire()
					.setScolarise(this.updateNiveauScolaire.getSelectionModel().getSelectedIndex() == 0);
			orphelin.getDossierScolaire()
					.setAnneeScolaire(this.updateAnneeScolaire.getSelectionModel().getSelectedItem() == null ? -1
							: this.updateAnneeScolaire.getSelectionModel().getSelectedItem());
			orphelin.getDossierScolaire().setObservation(this.updateObservationDossierScolaire.getText());

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void updateApparencePhysique() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de mise à jour", "Confirmer pour proceder à la mise à jour"))
				return;

			orphelin.getApparencePhysique().setDdMaj(this.updateDDMAJAP.getValue());
			orphelin.getApparencePhysique().setObservation(this.updateObservationAP.getText());
			orphelin.getApparencePhysique().setPointure(this.updatePointure.getText());

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void updateInformationsSupplimentaires() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de mise à jour", "Confirmer pour proceder à la mise à jour"))
				return;

			orphelin.setObsevation(this.updateObservationsOrphelin.getText());

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void resetSearch() {
		this.searchNomOrphelin.setText("");
		this.searchAgeMaxOrphelin.setText("");
		this.searchAgeMinOrphelin.setText("");
		this.searchSexeOrphelin.getSelectionModel().clearSelection();
		this.searchAnneeScolaire.getSelectionModel().clearSelection();
		this.searchNiveauScolaire.getSelectionModel().clearSelection();
		this.listeTuteurs.getSelectionModel().clearSelection();
		this.dateMin.setValue(null);
		this.dateMax.setValue(null);
		this.searchOrphelins();
	}

	@FXML
	private void deleteOrphelin() {
		Orphelin orphelin = this.tableOrphelins.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
					"Confirmation de suppression", "Confirmer pour proceder à la suppression"))
				return;

			(new Thread(new Runnable() {
				@Override
				public void run() {
					orphelin.getTuteur().setNbOrphelins(orphelin.getTuteur().getNbOrphelins() - 1);

					UtilitiesHolder.TUTEUR_DAO.updateTuteur(orphelin.getTuteur());
					UtilitiesHolder.ORPHELIN_DAO.deleteOrphelin(orphelin);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							OrphensPanelController.ORPHELINS.remove(orphelin);
							OrphensPanelController.ORPHELINS_AGES.remove(orphelin);
							OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.remove(orphelin);
						}
					});
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@FXML
	private void printOrphelins() {
		if (UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des orphelins",
				"Confirmation d'impression", "Confirmer pour proceder à l'impression"))
			UtilitiesHolder.PRINTING_HANDLER.printOrphelins(OrphensPanelController.ORPHELINS);
	}

	@FXML
	private void listAuthorizedOrphens() {
		UtilitiesHolder.AUTHORIZED_ORPHENS_STAGE.show();
	}

	@FXML
	private void authorizeOrphen() {
		Orphelin orphelin = this.listeOrphelinsAges.getSelectionModel().getSelectedItem();

		if (orphelin != null) {
			(new Thread(new Runnable() {
				@Override
				public void run() {
					orphelin.setAuthorized(true);
					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							OrphensPanelController.ORPHELINS_AGES.remove(orphelin);
							OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.add(orphelin);
						}
					});
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Orphelins", "Message d'erreur",
					"Veuillez d'abords selectionner un orphelin !");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.initHamburgers();
		this.initComponents();
		this.initLists();
		this.initDatePickers();
		this.initTextFields();
		this.initComboBoxes();
		this.initTables();
		this.initCheckBox();
		this.initInitialDatas();
	}

	private void initCheckBox() {
		this.displayAll.setOnMouseClicked(event -> {
			searchOrphelins();
		});
	}

	private void initDatePickers() {
		this.dateMin.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (newValue != null)
					searchOrphelins();
			}
		});

		this.dateMax.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (newValue != null)
					searchOrphelins();
			}
		});
	}

	private void initComboBoxes() {
		this.searchSexeOrphelin.setItems(FXCollections.observableArrayList(Genre.values()));
		this.searchSexeOrphelin.getSelectionModel().selectedItemProperty().addListener(event -> {
			searchOrphelins();
		});

		this.searchAnneeScolaire.setItems(FXCollections.observableArrayList(new Integer[] { 1, 2, 3, 4, 5, 6 }));
		this.searchAnneeScolaire.getSelectionModel().selectedItemProperty().addListener(event -> {
			searchOrphelins();
		});

		this.searchNiveauScolaire.setItems(FXCollections.observableArrayList(NiveauScolaire.values()));
		this.searchNiveauScolaire.getSelectionModel().selectedItemProperty().addListener(event -> {
			searchOrphelins();
		});
		this.searchNiveauScolaire.setConverter(OrphensPanelController.NIVEAU_SCOLAIRE_STRING_CONVERTER);

		this.updateTypeOrphelinat.setItems(FXCollections.observableArrayList(TypeOrphelinat.values()));
		this.updateTypeOrphelinat.setConverter(TYPE_ORPHELINAT_STRING_CONVERTER);

		this.updateNiveauScolaire.setItems(FXCollections.observableArrayList(NiveauScolaire.values()));
		this.updateNiveauScolaire.setConverter(OrphensPanelController.NIVEAU_SCOLAIRE_STRING_CONVERTER);

		this.updateAnneeScolaire.setItems(FXCollections.observableArrayList(new Integer[] { 1, 2, 3, 4, 5, 6 }));
	}

	private void initTables() {
		this.tableOrphelins.setItems(OrphensPanelController.ORPHELINS);
		this.tableOrphelins.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Orphelin>() {
			@Override
			public void changed(ObservableValue<? extends Orphelin> observable, Orphelin oldValue, Orphelin newValue) {
				OrphensPanelController.APPAREILLAGES.clear();

				if (newValue != null) {
					ApparencePhysique apparencePhysique = newValue.getApparencePhysique();
					DossierFamilial dossierFamilial = newValue.getDossierFamilial();
					DossierMedical dossierMedical = newValue.getDossierMedical();
					DossierScolaire dossierScolaire = newValue.getDossierScolaire();
					String observation = newValue.getObsevation();

					updateDDMAJAP.setValue(apparencePhysique.getDdMaj());
					updateDDMAJDS.setValue(dossierScolaire.getDdMaj());
					updateMedicament.setText(dossierMedical.getMedicaments());
					updateNiveauScolaire.setValue(dossierScolaire.getNiveauScolaire());
					updateNomMere.setText(dossierFamilial.getNomMere());
					updateNomPere.setText(dossierFamilial.getNomPere());
					updateObservationAP.setText(apparencePhysique.getObservation());
					updateObservationsOrphelin.setText(observation);
					updatePathologie.setText(dossierMedical.getPathologie());
					updatePointure.setText(apparencePhysique.getPointure());
					updatePrenomMere.setText(dossierFamilial.getPrenomMere());
					updatePrenomPere.setText(dossierFamilial.getPrenomPere());
					updateSituationSociale.setText(dossierFamilial.getSituationFamilliale());
					updateTypeOrphelinat.setValue(dossierFamilial.getTypeOrphelinat());
					updateAnneeScolaire.setValue(
							dossierScolaire.getAnneeScolaire() != -1 ? dossierScolaire.getAnneeScolaire() : null);
					updateObservationDossierScolaire.setText(dossierScolaire.getObservation());

					OrphensPanelController.APPAREILLAGES
							.addAll(UtilitiesHolder.ORPHELIN_DAO.getAppareillages(newValue));
				} else {
					updateDDMAJAP.setValue(null);
					updateDDMAJDS.setValue(null);
					updateMedicament.setText("");
					updateNiveauScolaire.setValue(null);
					updateNomMere.setText("");
					updateNomPere.setText("");
					updateObservationAP.setText("");
					updateObservationsOrphelin.setText("");
					updatePathologie.setText("");
					updatePointure.setText("");
					updatePrenomMere.setText("");
					updatePrenomPere.setText("");
					updateSituationSociale.setText("");
					updateObservationDossierScolaire.setText("");
					updateTypeOrphelinat.setValue(null);
					updateAnneeScolaire.setValue(null);
				}
			}
		});

		TableColumn<Orphelin, Integer> idColumn = ((TableColumn<Orphelin, Integer>) this.tableOrphelins.getColumns()
				.get(0));
		idColumn.setCellValueFactory(orphelin -> {
			return new SimpleIntegerProperty(orphelin.getValue().getId()).asObject();
		});

		TableColumn<Orphelin, String> nomColumn = ((TableColumn<Orphelin, String>) this.tableOrphelins.getColumns()
				.get(1));
		nomColumn.setCellValueFactory(orphelin -> {
			return new SimpleStringProperty(orphelin.getValue().getNom());
		});
		nomColumn.setCellFactory(event -> {
			TextFieldTableCell<Orphelin, String> component = new TextFieldTableCell<Orphelin, String>(
					new DefaultStringConverter()) {
				@Override
				public void commitEdit(String newValue) {
					Orphelin orphelin = this.getTableView().getItems().get(this.getTableRow().getIndex());
					orphelin.setNom(newValue);

					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
					super.commitEdit(newValue);
				}
			};

			return component;
		});

		TableColumn<Orphelin, String> prenomColumn = ((TableColumn<Orphelin, String>) this.tableOrphelins.getColumns()
				.get(2));
		prenomColumn.setCellValueFactory(orphelin -> {
			return new SimpleStringProperty(orphelin.getValue().getPrenom());
		});
		prenomColumn.setCellFactory(event -> {
			TextFieldTableCell<Orphelin, String> component = new TextFieldTableCell<Orphelin, String>(
					new DefaultStringConverter()) {
				@Override
				public void commitEdit(String newValue) {
					Orphelin orphelin = this.getTableView().getItems().get(this.getTableRow().getIndex());
					orphelin.setPrenom(newValue);

					UtilitiesHolder.ORPHELIN_DAO.updateOrphelin(orphelin);
					super.commitEdit(newValue);
				}
			};

			return component;
		});

		TableColumn<Orphelin, LocalDate> ddnColumn = ((TableColumn<Orphelin, LocalDate>) this.tableOrphelins
				.getColumns().get(3));
		ddnColumn.setCellValueFactory(orphelin -> {
			return new SimpleObjectProperty(
					orphelin.getValue().getDdn().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		});

		TableColumn<Orphelin, Integer> ageColumn = ((TableColumn<Orphelin, Integer>) this.tableOrphelins.getColumns()
				.get(4));
		ageColumn.setCellValueFactory(orphelin -> {
			return new SimpleIntegerProperty(orphelin.getValue().getAge()).asObject();
		});

		TableColumn<Orphelin, Genre> genreColumn = ((TableColumn<Orphelin, Genre>) this.tableOrphelins.getColumns()
				.get(5));
		genreColumn.setCellValueFactory(orphelin -> {
			return new SimpleObjectProperty(orphelin.getValue().getGenre());
		});

		TableColumn<Orphelin, Tuteur> tuteurColumn = ((TableColumn<Orphelin, Tuteur>) this.tableOrphelins.getColumns()
				.get(6));
		tuteurColumn.setCellValueFactory(orphelin -> {
			return new SimpleObjectProperty(orphelin.getValue().getTuteur());
		});

		TableColumn<Orphelin, String> RATColumn = ((TableColumn<Orphelin, String>) this.tableOrphelins.getColumns()
				.get(7));
		RATColumn.setCellValueFactory(orphelin -> {
			return new SimpleStringProperty(orphelin.getValue().getRelationOrphelinTuteur());
		});
	}

	private void initLists() {
		this.listeTuteurs.setItems(TutorsPanelController.TUTEURS);
		this.listeTuteurs.getSelectionModel().selectedItemProperty().addListener(event -> {
			searchOrphelins();
		});

		this.listeAppareillages.setItems(OrphensPanelController.APPAREILLAGES);

		this.listeOrphelinsAges.setItems(OrphensPanelController.ORPHELINS_AGES);
	}

	private void initTextFields() {
		this.searchNomTuteur.textProperty().bindBidirectional(ComponentsHolder.SEARCH_TUTORS.textProperty());
	}

	private void initInitialDatas() {
		OrphensPanelController.ORPHELINS.clear();
		// OrphensPanelController.ORPHELINS.addAll(UtilitiesHolder.ORPHELIN_DAO.getAllOrphelins());
		this.searchOrphelins();

		resultCount.setText("Nombre d'orphelins: " + OrphensPanelController.ORPHELINS.size());

		OrphensPanelController.ORPHELINS_AGES.clear();
		OrphensPanelController.ORPHELINS_AGES
				.addAll(UtilitiesHolder.ORPHELIN_DAO.getAgedOrphelins(FEMALE_AGE_LIMIT, Genre.Feminin));
		OrphensPanelController.ORPHELINS_AGES
				.addAll(UtilitiesHolder.ORPHELIN_DAO.getAgedOrphelins(MALE_AGE_LIMIT, Genre.Masculin));

		OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.clear();
		OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.addAll(UtilitiesHolder.ORPHELIN_DAO.getAuthorizedOrphelins());
	}

	private void initHamburgers() {
		HamburgerSlideCloseTransition leftHamburgerAnimator = new HamburgerSlideCloseTransition(this.leftHamburger);
		leftHamburgerAnimator.setRate(-1);

		this.leftHamburger.setAnimation(leftHamburgerAnimator);
		this.leftHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			this.leftHamburger.getAnimation().setRate(this.leftHamburger.getAnimation().getRate() * -1);
			this.leftHamburger.getAnimation().play();

			if (this.leftDrawer.isShown())
				this.leftDrawer.close();
			else
				this.leftDrawer.open();
		});

		// -----------------------------------------------------------------------------------------------------------

		HamburgerSlideCloseTransition rightHamburgerAnimator = new HamburgerSlideCloseTransition(this.rightHamburger);
		rightHamburgerAnimator.setRate(-1);

		this.rightHamburger.setAnimation(rightHamburgerAnimator);
		this.rightHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			this.rightHamburger.getAnimation().setRate(this.rightHamburger.getAnimation().getRate() * -1);
			this.rightHamburger.getAnimation().play();
			if (this.rightDrawer.isShown())
				this.rightDrawer.close();
			else
				this.rightDrawer.open();
		});
	}

	private void initComponents() {
		ComponentsHolder.SEARCH_NOM_ORPHELIN = this.searchNomOrphelin;
		ComponentsHolder.SEARCH_AGE_MIN_ORPHELIN = this.searchAgeMinOrphelin;
		ComponentsHolder.SEARCH_AGE_MAX_ORPHELIN = this.searchAgeMaxOrphelin;
		ComponentsHolder.SEARCH_GENRE_ORPHELIN = this.searchSexeOrphelin;
		ComponentsHolder.SEARCH_ANNEE_SCOLAIRE = this.searchAnneeScolaire;
		ComponentsHolder.SEARCH_NIVEAU_SCOLAIRE = this.searchNiveauScolaire;
		ComponentsHolder.SEARCH_DDN_MAX = this.dateMax;
		ComponentsHolder.SEARCH_DDN_MIN = this.dateMin;
	}

	public static StringConverter<TypeOrphelinat> TYPE_ORPHELINAT_STRING_CONVERTER = new StringConverter<TypeOrphelinat>() {
		@Override
		public TypeOrphelinat fromString(String arg0) {
			return null;
		}

		@Override
		public String toString(TypeOrphelinat arg0) {
			return arg0.toString().replaceAll("_", " ");
		}
	};

	public static StringConverter<NiveauScolaire> NIVEAU_SCOLAIRE_STRING_CONVERTER = new StringConverter<NiveauScolaire>() {
		@Override
		public NiveauScolaire fromString(String arg0) {
			return null;
		}

		@Override
		public String toString(NiveauScolaire arg0) {
			return arg0.toString().replaceAll("_", " ");
		}
	};
}
