package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.EvenementBon;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierScolaire.NiveauScolaire;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin.Genre;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.ComponentsHolder;

public class EventsBonsController implements Initializable {

	public static ObservableList<EvenementBon> EVENEMENTS_BONS = FXCollections
			.observableList(new LinkedList<EvenementBon>());
	public static ObservableList<Orphelin> ORPHELINS_SELECTIONNES = FXCollections
			.observableList(new LinkedList<Orphelin>());

	@FXML
	private JFXTextField searchNomOrphelin, searchAgeMinOrphelin, searchAgeMaxOrphelin;
	@FXML
	private JFXComboBox<Genre> searchSexeOrphelin;
	@FXML
	private ListView<EvenementBon> listeEvents;
	@FXML
	private TableView<Orphelin> tableOrphelins;
	@FXML
	private ListView<Orphelin> listeOrphelins;
	@FXML
	private JFXComboBox<Integer> searchAnneeScolaire;
	@FXML
	private JFXComboBox<NiveauScolaire> searchNiveauScolaire;
	@FXML
	private JFXDatePicker dateMin, dateMax;

	@FXML
	private void addEvent() {
		BorderPane dialogContainer = UtilitiesHolder.DIALOGS_BUILDER.getNewEvenementBonDialog();

		VBox formContainer = (VBox) dialogContainer.getCenter();

		JFXTextField designationNode = (JFXTextField) ((BorderPane) formContainer.getChildren().get(0)).getCenter();
		JFXTextArea descriptionNode = (JFXTextArea) ((BorderPane) ((TitledPane) formContainer.getChildren().get(1))
				.getContent()).getCenter();
		JFXDatePicker dateNode = (JFXDatePicker) ((BorderPane) formContainer.getChildren().get(2)).getCenter();
		JFXTextField coutNode = (JFXTextField) ((BorderPane) formContainer.getChildren().get(3)).getCenter();
		JFXTextField etablissementNode = (JFXTextField) ((BorderPane) formContainer.getChildren().get(4)).getCenter();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Nouvel evenement");
		alert.setHeaderText("Formulaire d'ajout d'un nouvel evenement");
		alert.getDialogPane().setContent(dialogContainer);
		alert.showAndWait();

		String designation = designationNode.getText();
		String description = descriptionNode.getText();
		LocalDate date = dateNode.getValue();
		double coutBon = -1;
		String etablissement = etablissementNode.getText();

		try {
			coutBon = Double.parseDouble(coutNode.getText());
		} catch (Exception exp) {
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenement", "Erreur de saisi",
					"Veuillez verifier le cout");
			return;
		}

		if (alert.getResult() != ButtonType.OK)
			return;

		if (!designation.equals("") && date != null && coutBon > 0) {
			(new Thread(new Runnable() {
				@Override
				public void run() {
					EvenementBon evenementBon = new EvenementBon();
					evenementBon.setCoutBon(Double.parseDouble(coutNode.getText()));
					evenementBon.setDateEvenement(date);
					evenementBon.setDescriptionEvenement(description);
					evenementBon.setDesignationEvenement(designation);
					evenementBon.setEtablissementValidation(etablissement);

					UtilitiesHolder.EVENEMENT_DAO.addNewEvenement(evenementBon);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							EventsBonsController.EVENEMENTS_BONS.add(evenementBon);
						}
					});
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenement", "Erreur de saisi",
					"Veuillez verifier la designation et la date");
	}

	@FXML
	private void deleteEvent() {
		EvenementBon evenementBon = this.listeEvents.getSelectionModel().getSelectedItem();

		if (evenementBon != null) {
			if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des Evenements",
					"Confirmation de suppression", "Confirmer pour proceder à la suppression"))
				return;

			(new Thread(new Runnable() {
				@Override
				public void run() {
					UtilitiesHolder.EVENEMENT_DAO.deleteEvenement(evenementBon);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							EventsBonsController.EVENEMENTS_BONS.remove(evenementBon);
						}
					});
				}
			})).start();
		} else
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenements", "Erreur de suppression",
					"Veuillez d'abords selectionner un evenement");
	}

	@FXML
	private void printBons() {
		EvenementBon evenement = this.listeEvents.getSelectionModel().getSelectedItem();

		if (evenement == null) {
			UtilitiesHolder.ALERTS_DISPLAYER.displayErrorAlert("Gestion des Evenements", "Erreur de saisi",
					"Veuillez d'abords selectionner un evenement");
			return;
		}

		if (UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des evenements",
				"Confirmation d'impression", "Confirmer pour proceder à la l'impression")) {
			(new Thread(new Runnable() {
				@Override
				public void run() {
					HashMap<Tuteur, LinkedList<Orphelin>> printableHashMap = fromListToMap(
							EventsBonsController.ORPHELINS_SELECTIONNES);
					LinkedList<LinkedList<Orphelin>> printableOrphens = new LinkedList<LinkedList<Orphelin>>();
					Action[] actions = new Action[printableHashMap.entrySet().size()];
					int i = 0;

					for (Tuteur tuteur : printableHashMap.keySet()) {
						LinkedList<Orphelin> printableList = printableHashMap.get(tuteur);

						Action action = new Action();
						action.setDateAction(LocalDate.now());
						action.setEvenement(evenement);
						action.setTuteur(tuteur);
						action.setQuantiteAction(printableList.size());

						actions[i++] = action;

						printableOrphens.add(printableList);
					}

					UtilitiesHolder.EVENEMENT_DAO.addNewActions(actions);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							UtilitiesHolder.PRINTING_HANDLER.printBons(evenement, printableOrphens);
						}
					});
				}
			})).start();
		}
	}

	@FXML
	private void resetSearch() {
		this.searchNomOrphelin.setText("");
		this.searchAgeMaxOrphelin.setText("");
		this.searchAgeMinOrphelin.setText("");
		this.searchSexeOrphelin.getSelectionModel().clearSelection();
		this.searchAnneeScolaire.getSelectionModel().clearSelection();
		this.searchNiveauScolaire.getSelectionModel().clearSelection();
		this.dateMax.setValue(null);
		this.dateMin.setValue(null);
		this.searchOrphelins();
	}

	@FXML
	private void searchOrphelins() {
		String names = this.searchNomOrphelin.getText();

		Genre gender = this.searchSexeOrphelin.getSelectionModel().getSelectedItem();

		NiveauScolaire niveauScolaire = this.searchNiveauScolaire.getSelectionModel().getSelectedItem();

		Integer anneeScolaire = this.searchAnneeScolaire.getSelectionModel().getSelectedItem();

		LocalDate dateMax = this.dateMax.getValue();
		LocalDate dateMin = this.dateMin.getValue();

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

		(new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					List<Orphelin> result = UtilitiesHolder.ORPHELIN_DAO.getSpecifiedOrphelins(null, names, gender,
							ageMinFin, ageMaxFin, niveauScolaire, anneeScolaire, dateMin, dateMax, false);

					@Override
					public void run() {
						OrphensPanelController.ORPHELINS.clear();
						OrphensPanelController.ORPHELINS.addAll(result);
					}
				});
			}
		})).start();
	}

	@FXML
	private void addAllCurrentOrphelins() {
		for (Orphelin orphelin : OrphensPanelController.ORPHELINS)
			if (!EventsBonsController.ORPHELINS_SELECTIONNES.contains(orphelin))
				EventsBonsController.ORPHELINS_SELECTIONNES.add(orphelin);
	}

	@FXML
	private void removeAllCurrentOrphelins() {
		if (!UtilitiesHolder.ALERTS_DISPLAYER.displayConfirmationAlert("Gestion des evenements",
				"Confirmation de suppression", "Confirmer pour proceder à la suppression"))
			return;

		EventsBonsController.ORPHELINS_SELECTIONNES.clear();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initInitialData();
		this.initLists();
		this.initBindings();
		this.initTables();
		this.initComboBoxes();
	}

	private void initComboBoxes() {
		this.searchSexeOrphelin.setItems(FXCollections.observableArrayList(Genre.values()));
		this.searchAnneeScolaire.setItems(FXCollections.observableArrayList(new Integer[] { 1, 2, 3, 4, 5, 6 }));
		this.searchNiveauScolaire.setItems(FXCollections.observableArrayList(NiveauScolaire.values()));
	}

	private void initTables() {
		this.tableOrphelins.setItems(OrphensPanelController.ORPHELINS);

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

		TableColumn<Orphelin, String> prenomColumn = ((TableColumn<Orphelin, String>) this.tableOrphelins.getColumns()
				.get(2));
		prenomColumn.setCellValueFactory(orphelin -> {
			return new SimpleStringProperty(orphelin.getValue().getPrenom());
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

		TableColumn<Orphelin, Boolean> takeItColumn = ((TableColumn<Orphelin, Boolean>) this.tableOrphelins.getColumns()
				.get(7));
		takeItColumn.setCellFactory(new Callback<TableColumn<Orphelin, Boolean>, TableCell<Orphelin, Boolean>>() {
			@Override
			public TableCell<Orphelin, Boolean> call(TableColumn<Orphelin, Boolean> arg0) {
				return new TableCell<Orphelin, Boolean>() {
					@Override
					public void updateItem(Boolean value, boolean empty) {
						super.updateItem(value, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							Orphelin orphelin = (Orphelin) this.getTableRow().getItem();

							if (orphelin != null) {
								HBox container = new HBox();

								JFXButton takeItButton = new JFXButton("Ajouter");
								takeItButton.setOnMouseClicked(event -> {
									if (!EventsBonsController.ORPHELINS_SELECTIONNES.contains(orphelin))
										EventsBonsController.ORPHELINS_SELECTIONNES.add(orphelin);
								});
								takeItButton.setGraphic(new ImageView(ComponentsHolder.PLUS_IMAGE));

								JFXButton notTakeItButton = new JFXButton("Enlever");
								notTakeItButton.setOnMouseClicked(event -> {
									if (EventsBonsController.ORPHELINS_SELECTIONNES.contains(orphelin))
										EventsBonsController.ORPHELINS_SELECTIONNES.remove(orphelin);
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

	private void initLists() {
		this.listeEvents.setItems(EventsBonsController.EVENEMENTS_BONS);
		this.listeOrphelins.setItems(EventsBonsController.ORPHELINS_SELECTIONNES);
	}

	private void initInitialData() {
		EventsBonsController.EVENEMENTS_BONS.clear();
		EventsBonsController.EVENEMENTS_BONS.addAll(UtilitiesHolder.EVENEMENT_DAO.getAllEvenementBon());
	}

	private void initBindings() {
		ComponentsHolder.SEARCH_NOM_ORPHELIN.textProperty().bindBidirectional(searchNomOrphelin.textProperty());
		ComponentsHolder.SEARCH_AGE_MIN_ORPHELIN.textProperty().bindBidirectional(searchAgeMinOrphelin.textProperty());
		ComponentsHolder.SEARCH_AGE_MAX_ORPHELIN.textProperty().bindBidirectional(searchAgeMaxOrphelin.textProperty());
		ComponentsHolder.SEARCH_GENRE_ORPHELIN.valueProperty().bindBidirectional(searchSexeOrphelin.valueProperty());
		ComponentsHolder.SEARCH_ANNEE_SCOLAIRE.valueProperty().bindBidirectional(searchAnneeScolaire.valueProperty());
		ComponentsHolder.SEARCH_NIVEAU_SCOLAIRE.valueProperty().bindBidirectional(searchNiveauScolaire.valueProperty());
		ComponentsHolder.SEARCH_DDN_MAX.valueProperty().bindBidirectional(dateMax.valueProperty());
		ComponentsHolder.SEARCH_DDN_MIN.valueProperty().bindBidirectional(dateMin.valueProperty());
	}

	private HashMap<Tuteur, LinkedList<Orphelin>> fromListToMap(List<Orphelin> orphelins) {
		HashMap<Tuteur, LinkedList<Orphelin>> result = new HashMap<Tuteur, LinkedList<Orphelin>>();

		for (Orphelin orphelin : orphelins) {
			try {
				LinkedList<Orphelin> subOrphelins = result.get(orphelin.getTuteur());
				subOrphelins.add(orphelin);
			} catch (NullPointerException exp) {
				LinkedList<Orphelin> subOrphelins = new LinkedList<Orphelin>();
				subOrphelins.add(orphelin);

				result.put(orphelin.getTuteur(), subOrphelins);
			}
		}

		return result;
	}
}
