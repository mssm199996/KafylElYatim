package mssmfactory.KafylElYatim.Utilities.HardwareHandlers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationForm;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationType;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.Action;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.EvenementBon;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierScolaire.NiveauScolaire;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin.Genre;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.MVC.Controllers.DonationsPanelController;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;


public class PrintingHandler {
	private PrinterJob printerJob = null;
	
	public static int MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_FIRST_PAGE = 23;
	public static int MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_SECOND_PAGE = 32;
	
	public static int MAX_NUMBER_OF_ROWS_PER_PAGE_LANSCAPE_FIRST_PAGE = 13;
	public static int MAX_NUMBER_OF_ROWS_PER_PAGE_LANSCAPE_SECOND_PAGE = 22;
	
	public PrintingHandler() {
		this.setPrinterJob(PrinterJob.createPrinterJob());
	}
	
	private void printReducedDonations(ObservableList<Donation> donations, int nbDonations, double sumDonations, boolean isFirstPage) {
		try {
			BorderPane rootPane = FXMLLoader.load(getClass().getResource(
				isFirstPage ? "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Donations/FirstPage.fxml" : 
							  "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Donations/SecondPage.fxml").toURI().toURL()
			);
			
			TableView<Donation> donationsTableView = null;
			
			if(isFirstPage)
				donationsTableView = (TableView<Donation>) rootPane.getCenter();
			else donationsTableView = (TableView<Donation>) rootPane.getTop();
			
			final TableView<Donation> finalTableView = donationsTableView;
			
			donationsTableView.setItems(donations);
						
			TableColumn<Donation, Integer> idColumn =  ((TableColumn<Donation,Integer>) donationsTableView.getColumns().get(0));
										   idColumn.setCellValueFactory(donation -> {
											   return new SimpleIntegerProperty(donation.getValue().getId()).asObject();
										   });
						   
			TableColumn<Donation, DonationType> typeColumn =  ((TableColumn<Donation, DonationType>) donationsTableView.getColumns().get(1));
												typeColumn.setCellValueFactory(donation -> {
											   		return new SimpleObjectProperty(donation.getValue().getType());
											   	});
				
			TableColumn<Donation, DonationForm> formeColumn =  ((TableColumn<Donation, DonationForm>) donationsTableView.getColumns().get(2));
												formeColumn.setCellValueFactory(donation -> {
											   		return new SimpleObjectProperty(donation.getValue().getForme());
											   	});
												formeColumn.setCellFactory(TextFieldTableCell.forTableColumn(DonationsPanelController.DONATION_TYPE_STRING_CONVERTER));
			
			TableColumn<Donation, String> valeurColumn =  ((TableColumn<Donation, String>) donationsTableView.getColumns().get(3));
										  valeurColumn.setCellValueFactory(donation -> {
											  return new SimpleStringProperty(donation.getValue().getValeur());
										  });
						  
			TableColumn<Donation, LocalDate> dateColumn =  ((TableColumn<Donation, LocalDate>) donationsTableView.getColumns().get(4));
											 dateColumn.setCellValueFactory(donation -> {
												 return new SimpleObjectProperty(donation.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
										   	 });											 
			
			donationsTableView.setMinHeight(25 * donations.size() + 30);
			donationsTableView.setMaxHeight(25 * donations.size() + 30);
						
			Scene scene = new Scene(rootPane);
				  scene.getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/DonationPrinting.css");
			
			donationsTableView.layoutYProperty().addListener(event -> { finalTableView.setLayoutY(0.0); });
				  
			Stage stageToPrint = new Stage();
			  	  stageToPrint.setScene(scene);
			  	  stageToPrint.show();
			  	
			if(isFirstPage) {
				SplitPane focusRequester = (SplitPane) rootPane.getBottom();
				
				Label nbDonationsLabel = (Label)(((HBox)(focusRequester.getItems().get(0))).getChildren().get(1));
					  nbDonationsLabel.setText(nbDonations + "");
				Label sumDonationsLabel = (Label)(((HBox)(focusRequester.getItems().get(1))).getChildren().get(1));
					  sumDonationsLabel.setText(sumDonations + "");
					  
				focusRequester.layoutYProperty().addListener(event -> { focusRequester.setLayoutY(finalTableView.getHeight()); });
				nbDonationsLabel.requestFocus();
			}
			else {
				HBox focusRequester = (HBox) rootPane.getBottom();
					 focusRequester.requestFocus();
			}
			  							
			donationsTableView.getSelectionModel().clearSelection();
			
			this.getPrinterJob().printPage(stageToPrint.getScene().getRoot());
			
			stageToPrint.close();
		
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}		
	}
	public void printDonations(ObservableList<Donation> donations) {
		this.setPrinterJob(PrinterJob.createPrinterJob());
		
		this.getPrinterJob().showPageSetupDialog(null);
		
		PageLayout pageLayout = this.getPrinterJob().getJobSettings().getPageLayout();
		
		this.getPrinterJob().getJobSettings().setPageLayout(this.getPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), MarginType.HARDWARE_MINIMUM));
		
		double sum = 0;
		
		for(Donation donation: donations) {
			double montant = 0;
			try { montant = Double.parseDouble(donation.getValeur()); }
			catch(NumberFormatException exp) {}
			sum += montant;
		}
			
		int remainingDonations = donations.size();
		int pageIndex = 0;
		int currentDonation = 0;
		
		while(remainingDonations != 0) {
			ObservableList<Donation> currentDonations = FXCollections.observableList(new LinkedList<Donation>());
			
			int MAX_NUMBER_OF_ROWS_PER_PAGE = (pageIndex == 0 ? MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_FIRST_PAGE : MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_SECOND_PAGE);
			int initialIndex = currentDonation;
			
			while((currentDonation < initialIndex + MAX_NUMBER_OF_ROWS_PER_PAGE) && (remainingDonations != 0)) {
				currentDonations.add(donations.get(currentDonation++));
				
				remainingDonations--;
			}
			
			this.printReducedDonations(currentDonations, donations.size(), sum, pageIndex == 0);
			
			pageIndex++;
		}
		
		this.getPrinterJob().endJob();
	}
	
	// --------------------------------------------------------------------------------------------------------------------------
	
	public void printTutors(ObservableList<Tuteur> tuteurs) {
		this.setPrinterJob(PrinterJob.createPrinterJob());
		
		this.getPrinterJob().showPageSetupDialog(null);
		
		PageLayout pageLayout = this.getPrinterJob().getJobSettings().getPageLayout();
		
		this.getPrinterJob().getJobSettings().setPageLayout(this.getPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), MarginType.HARDWARE_MINIMUM));
		
		int remainingTuteurs = tuteurs.size();
		int pageIndex = 0;
		int currentTuteur = 0;
		
		while(remainingTuteurs != 0) {
			ObservableList<Tuteur> currentTuteurs = FXCollections.observableList(new LinkedList<Tuteur>());
			
			int MAX_NUMBER_OF_ROWS_PER_PAGE = (pageIndex == 0 ? MAX_NUMBER_OF_ROWS_PER_PAGE_LANSCAPE_FIRST_PAGE : MAX_NUMBER_OF_ROWS_PER_PAGE_LANSCAPE_SECOND_PAGE);
			int initialIndex = currentTuteur;
			
			while((currentTuteur < initialIndex + MAX_NUMBER_OF_ROWS_PER_PAGE) && (remainingTuteurs != 0)) {
				currentTuteurs.add(tuteurs.get(currentTuteur++));
				
				remainingTuteurs--;
			}
			
			this.printReducedTuteurs(currentTuteurs, tuteurs.size(), pageIndex == 0);
			
			pageIndex++;
		}
		
		this.getPrinterJob().endJob();
	}
	private void printReducedTuteurs(ObservableList<Tuteur> tuteurs, int nbTuteurs, boolean isFirstPage) {
		try {
			BorderPane rootPane = FXMLLoader.load(getClass().getResource(
					isFirstPage ? "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Tutors/FirstPage.fxml" : 
								  "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Tutors/SecondPage.fxml").toURI().toURL()
			);
										  
			TableView<Tuteur> tuteursTableView = null;
			
			if(isFirstPage)
				tuteursTableView = (TableView<Tuteur>) rootPane.getCenter();
			else tuteursTableView = (TableView<Tuteur>) rootPane.getTop();
						
			final TableView<Tuteur> finalTableView = tuteursTableView;
									finalTableView.setItems(tuteurs);
			
			TableColumn<Tuteur, Integer> idColumn =  ((TableColumn<Tuteur,Integer>) tuteursTableView.getColumns().get(0));
										 idColumn.setCellValueFactory(tuteur -> {
											 return new SimpleIntegerProperty(tuteur.getValue().getId()).asObject();
										 });
						   
			TableColumn<Tuteur, String> nomColumn =  ((TableColumn<Tuteur, String>) tuteursTableView.getColumns().get(1));
										nomColumn.setCellValueFactory(tuteur -> {
											return new SimpleStringProperty(tuteur.getValue().getNom());
										});
												
			TableColumn<Tuteur, String> prenomColumn =  ((TableColumn<Tuteur, String>) tuteursTableView.getColumns().get(2));
										prenomColumn.setCellValueFactory(tuteur -> {
											return new SimpleStringProperty(tuteur.getValue().getPrenom());
										});
						  
			TableColumn<Tuteur, Region> regionColumn =  ((TableColumn<Tuteur, Region>) tuteursTableView.getColumns().get(3));
										regionColumn.setCellValueFactory(tuteur -> {
											return new SimpleObjectProperty(tuteur.getValue().getRegion());
										});
											 
			TableColumn<Tuteur, String> telColumn =  ((TableColumn<Tuteur, String>) tuteursTableView.getColumns().get(4));
										telColumn.setCellValueFactory(tuteur -> {
											return new SimpleStringProperty(!tuteur.getValue().getnTelMob().equals("") ? tuteur.getValue().getnTelMob() : !tuteur.getValue().getnTelFix().equals("") ? tuteur.getValue().getnTelFix() : "Inconnu");
										});
											 
			TableColumn<Tuteur, String> adresseColumn =  ((TableColumn<Tuteur, String>) tuteursTableView.getColumns().get(5));
										adresseColumn.setCellValueFactory(tuteur -> {
											return new SimpleStringProperty(tuteur.getValue().getAdresse());
										});
										
			TableColumn<Tuteur, Integer> nbOrphelinsColumn =  ((TableColumn<Tuteur, Integer>) tuteursTableView.getColumns().get(6));
										 nbOrphelinsColumn.setCellValueFactory(tuteur -> {
											 return new SimpleIntegerProperty(tuteur.getValue().getNbOrphelins()).asObject();
										 });
		
			tuteursTableView.setMinHeight(25 * tuteurs.size() + 30);
			tuteursTableView.setMaxHeight(25 * tuteurs.size() + 30);
			
			Scene scene = new Scene(rootPane);
			  	  scene.getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/DonationPrinting.css");
			
			tuteursTableView.layoutYProperty().addListener(event -> { finalTableView.setLayoutY(0.0); });
				  
			Stage stageToPrint = new Stage();
			  	  stageToPrint.setScene(scene);
			  	  stageToPrint.show();
			  	
			if(isFirstPage) {
				SplitPane focusRequester = (SplitPane) rootPane.getBottom();
					
				Label nbTuteursLabel = (Label)(((HBox)(focusRequester.getItems().get(0))).getChildren().get(1));
					  nbTuteursLabel.setText(nbTuteurs + "");
						  
				focusRequester.layoutYProperty().addListener(event -> { focusRequester.setLayoutY(finalTableView.getHeight()); });
				nbTuteursLabel.requestFocus();
			}
			else {
				HBox focusRequester = (HBox) rootPane.getBottom();
					 focusRequester.requestFocus();
			}
			
			tuteursTableView.getSelectionModel().clearSelection();
			
			this.getPrinterJob().printPage(stageToPrint.getScene().getRoot());
			
			stageToPrint.close();
		
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------
	
	public void printOrphelins(ObservableList<Orphelin> orphelins) {
		this.setPrinterJob(PrinterJob.createPrinterJob());
		
		this.getPrinterJob().showPageSetupDialog(null);
		
		PageLayout pageLayout = this.getPrinterJob().getJobSettings().getPageLayout();
		
		this.getPrinterJob().getJobSettings().setPageLayout(
				this.getPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), MarginType.HARDWARE_MINIMUM));
		
		int remainingOrphelins = orphelins.size();
		int pageIndex = 0;
		int currentOrphelin = 0;
		
		while(remainingOrphelins != 0) {
			ObservableList<Orphelin> currentOrphelins = FXCollections.observableList(new LinkedList<Orphelin>());
			
			int MAX_NUMBER_OF_ROWS_PER_PAGE = (pageIndex == 0 ? MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_FIRST_PAGE : MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_SECOND_PAGE);
			int initialIndex = currentOrphelin;
			
			while((currentOrphelin < initialIndex + MAX_NUMBER_OF_ROWS_PER_PAGE) && (remainingOrphelins != 0)) {
				currentOrphelins.add(orphelins.get(currentOrphelin++));
				
				remainingOrphelins--;
			}
			
			this.printReducedOrphelins(currentOrphelins, orphelins.size(), pageIndex == 0);
			
			pageIndex++;
		}
		
		this.getPrinterJob().endJob();
	}
	private void printReducedOrphelins(ObservableList<Orphelin> orphelins, int nbOrphelins, boolean isFirstPage) {
		try {
			BorderPane rootPane = FXMLLoader.load(getClass().getResource(
				isFirstPage ? "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Orphens/FirstPage.fxml" : 
							  "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Orphens/SecondPage.fxml").toURI().toURL()
			);
			
			TableView<Orphelin> orphelinsTableView = null;
			
			if(isFirstPage)
				orphelinsTableView = (TableView<Orphelin>) rootPane.getCenter();
			else orphelinsTableView = (TableView<Orphelin>) rootPane.getTop();
			
			final TableView<Orphelin> finalTableView = orphelinsTableView;
									  finalTableView.setItems(orphelins);
			
			TableColumn<Orphelin, Integer> idColumn =  ((TableColumn<Orphelin,Integer>) orphelinsTableView.getColumns().get(0));
										   idColumn.setCellValueFactory(orphelin -> {
											   return new SimpleIntegerProperty(orphelin.getValue().getId()).asObject();
										   });
			   
			TableColumn<Orphelin, String> nomColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(1));
										  nomColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(orphelin.getValue().getNom());
										  });
						  
			TableColumn<Orphelin, String> prenomColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(2));
										  prenomColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(orphelin.getValue().getPrenom());
										  });
					
			TableColumn<Orphelin, Integer> ageColumn =  ((TableColumn<Orphelin,Integer>) orphelinsTableView.getColumns().get(3));
										   ageColumn.setCellValueFactory(orphelin -> {
											   return new SimpleIntegerProperty(orphelin.getValue().getAge()).asObject();
										   });
										   
			TableColumn<Orphelin, Genre> genreColumn =  ((TableColumn<Orphelin,Genre>) orphelinsTableView.getColumns().get(4));
										 genreColumn.setCellValueFactory(orphelin -> {
											 return new SimpleObjectProperty(orphelin.getValue().getGenre());
										 });
			
			orphelinsTableView.setMinHeight(25 * orphelins.size() + 30);
			orphelinsTableView.setMaxHeight(25 * orphelins.size() + 30);
			
			Scene scene = new Scene(rootPane);
				  scene.getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/DonationPrinting.css");
			
			orphelinsTableView.layoutYProperty().addListener(event -> { finalTableView.setLayoutY(0.0); });
				  
			Stage stageToPrint = new Stage();
			  	  stageToPrint.setScene(scene);
			  	  stageToPrint.show();
			  	
			if(isFirstPage) {
				SplitPane focusRequester = (SplitPane) rootPane.getBottom();
				
				Label nbOrphelinsLabel = (Label)(((HBox) (focusRequester.getItems().get(0))).getChildren().get(1));
					  nbOrphelinsLabel.setText(nbOrphelins + "");
					  
				focusRequester.layoutYProperty().addListener(event -> { focusRequester.setLayoutY(finalTableView.getHeight()); });
				focusRequester.requestFocus();
			}
			else {
				HBox focusRequester = (HBox) rootPane.getBottom();
					 focusRequester.requestFocus();
			}
			
			orphelinsTableView.getSelectionModel().clearSelection();
			
			this.getPrinterJob().printPage(stageToPrint.getScene().getRoot());
			
			stageToPrint.close();
		
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	public void printDecharges(Action[] actions) {
		this.setPrinterJob(PrinterJob.createPrinterJob());
		
		this.getPrinterJob().showPageSetupDialog(null);
		
		PageLayout pageLayout = this.getPrinterJob().getJobSettings().getPageLayout();
		
		this.getPrinterJob().getJobSettings().setPageLayout(
				this.getPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), MarginType.HARDWARE_MINIMUM));
		
		for(Action action: actions) 
			this.printDecharge(action);
		
		this.getPrinterJob().endJob();
	}
	public void printDecharge(Action action) {
		try {
			BorderPane rootPane = FXMLLoader.load(getClass().getResource(
					"/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Decharges/EvenementDecharge.fxml").toURI().toURL()
			);
			
			VBox detailsContainer = (VBox) rootPane.getCenter();
			
			Label tuteurNode = (Label) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(1)).getChildren().get(1);
			Label nomPereOrphelinsNode = (Label) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(2)).getChildren().get(1);

			Label addrNode = (Label) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(3)).getChildren().get(1);
			Label addr2Node = (Label) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(4)).getChildren().get(1);

			Label telPortNode = (Label) ((HBox) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(5)).getChildren().get(0)).getChildren().get(1);
			Label telFixNode = (Label) ((HBox) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(5)).getChildren().get(1)).getChildren().get(1);
			
			Label nCniNode = (Label) ((HBox) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(6)).getChildren().get(0)).getChildren().get(1);
			Label zoneNode = (Label) ((HBox) ((HBox) ((VBox) detailsContainer.getChildren().get(0)).getChildren().get(6)).getChildren().get(1)).getChildren().get(1);
			
			// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			Label eventIdNode = (Label) ((HBox) ((HBox) ((VBox) detailsContainer.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getChildren().get(1);
			Label quantiteNode = (Label) ((HBox) ((HBox) ((VBox) detailsContainer.getChildren().get(1)).getChildren().get(1)).getChildren().get(1)).getChildren().get(1);
			
			Label designationNode = (Label) ((HBox) ((BorderPane) ((VBox) detailsContainer.getChildren().get(1)).getChildren().get(2)).getLeft()).getChildren().get(1);
			Label dateLivraisonNode = (Label) ((HBox) ((BorderPane) ((VBox) detailsContainer.getChildren().get(1)).getChildren().get(2)).getRight()).getChildren().get(1);
			
			Label descriptionNode = (Label) ((HBox) ((VBox) detailsContainer.getChildren().get(1)).getChildren().get(3)).getChildren().get(1);
			
			Tuteur tuteur = action.getTuteur();
			
			tuteurNode.setText(tuteur.toString());
			addrNode.setText(tuteur.getAdresse());
			addr2Node.setText(tuteur.getSecondeAdresse());
			telPortNode.setText(tuteur.getnTelMob());
			telFixNode.setText(tuteur.getnTelFix());
			nCniNode.setText(tuteur.getNcni());
			dateLivraisonNode.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			zoneNode.setText(tuteur.getRegion().getNom());
			eventIdNode.setText(action.getEvenement().getIdEvenement() + "");
			quantiteNode.setText(action.getQuantiteAction() + "");
			designationNode.setText(action.getEvenement().getDesignationEvenement());
			descriptionNode.setText(action.getEvenement().getDescriptionEvenement());
			nomPereOrphelinsNode.setText(action.getTuteur().getNomPereOrphelins() + " " + action.getTuteur().getPrenomPereOrphelins());
			
			Scene scene = new Scene(rootPane);
			
			Stage stageToPrint = new Stage();
			  	  stageToPrint.setScene(scene);
			      stageToPrint.show();
																									
			getPrinterJob().printPage(stageToPrint.getScene().getRoot());
		
			stageToPrint.close();
			
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void printBons(EvenementBon evenementBon, LinkedList<LinkedList<Orphelin>> printableOrphens) {
		this.setPrinterJob(PrinterJob.createPrinterJob());
		
		this.getPrinterJob().showPageSetupDialog(null);
		
		PageLayout pageLayout = this.getPrinterJob().getJobSettings().getPageLayout();
		
		this.getPrinterJob().getJobSettings().setPageLayout(
				this.getPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), MarginType.HARDWARE_MINIMUM));
		
		for(List<Orphelin> orphelins: printableOrphens) 
			this.printBon(evenementBon, orphelins);
		
		this.getPrinterJob().endJob();
	}
	public void printBon(EvenementBon evenementBon, List<Orphelin> orphelins) {
		try {
			BorderPane rootPane = FXMLLoader.load(getClass().getResource(
					"/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Bons/EvenementBon.fxml").toURI().toURL()
			);
			
			TableView<Orphelin> orphelinsTableView = (TableView<Orphelin>) ((BorderPane) rootPane.getBottom()).getTop();
								orphelinsTableView.getItems().addAll(orphelins);
								
			Label etablissementNode = (Label) ((BorderPane) rootPane.getTop()).getCenter();
								
			VBox detailsContainer = (VBox) rootPane.getCenter();
			
			Label eventIdNode = (Label) ((HBox) ((HBox) detailsContainer.getChildren().get(1)).getChildren().get(0)).getChildren().get(1);
			Label eventDesignationNode = (Label) ((HBox) ((HBox) detailsContainer.getChildren().get(1)).getChildren().get(1)).getChildren().get(1);
			
			Label eventDescriptionNode = (Label) ((HBox) ((BorderPane) (detailsContainer.getChildren().get(2))).getLeft()).getChildren().get(1);
			Label dateCourrantNode = (Label) ((HBox) ((BorderPane) (detailsContainer.getChildren().get(2))).getRight()).getChildren().get(1);

			Label tuteurNameNode = (Label) ((HBox) (detailsContainer.getChildren().get(3))).getChildren().get(1);
			Label telTuteurNode = (Label) ((HBox)((HBox) detailsContainer.getChildren().get(4)).getChildren().get(0)).getChildren().get(1);
			Label addrtelTuteurNode = (Label) ((HBox)((HBox) detailsContainer.getChildren().get(4)).getChildren().get(1)).getChildren().get(1);
			Label zoneTuteurNode = (Label) ((HBox)((HBox) detailsContainer.getChildren().get(4)).getChildren().get(2)).getChildren().get(1);
			
			Label nbOrphelinsNode = (Label) ((HBox)((SplitPane) ((BorderPane) rootPane.getBottom()).getCenter()).getItems().get(0)).getChildren().get(1);
			Label montantTotalNode = (Label) ((HBox)((SplitPane) ((BorderPane) rootPane.getBottom()).getCenter()).getItems().get(1)).getChildren().get(1);
			
			// -------------------------------------------------------------------------------------------------------------------------------------------------
			Tuteur tuteur = orphelins.get(0).getTuteur();
			
			eventIdNode.setText(evenementBon.getIdEvenement() + "");
			eventDescriptionNode.setText(evenementBon.getDescriptionEvenement());
			dateCourrantNode.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			eventDesignationNode.setText(evenementBon.getDesignationEvenement());
			tuteurNameNode.setText(tuteur.getNom() + " " + tuteur.getPrenom());
			telTuteurNode.setText(!tuteur.getnTelMob().equals("") ? tuteur.getnTelMob() : tuteur.getnTelFix());
			addrtelTuteurNode.setText(tuteur.getAdresse());
			
			Region region = UtilitiesHolder.REGION_DAO.fetchRegionByTuteur(tuteur);
			
			zoneTuteurNode.setText(region != null ? region.getNom() : "");
			
			nbOrphelinsNode.setText(orphelins.size() + "");
			montantTotalNode.setText(evenementBon.getCoutBon() * orphelins.size() + "");
			
			etablissementNode.setText("Décharge bon d'achat " + evenementBon.getEtablissementValidation());
			
			TableColumn<Orphelin, Integer> idColumn =  ((TableColumn<Orphelin,Integer>) orphelinsTableView.getColumns().get(0));
										   idColumn.setCellValueFactory(orphelin -> {
											   return new SimpleIntegerProperty(orphelin.getValue().getId()).asObject();
										   });

			TableColumn<Orphelin, String> nomColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(1));
										  nomColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(orphelin.getValue().getNom());
										  });
			
			TableColumn<Orphelin, String> prenomColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(2));
										  prenomColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(orphelin.getValue().getPrenom());
										  });
			
			TableColumn<Orphelin, Integer> ageColumn =  ((TableColumn<Orphelin,Integer>) orphelinsTableView.getColumns().get(3));
										   ageColumn.setCellValueFactory(orphelin -> {
											   return new SimpleIntegerProperty(orphelin.getValue().getAge()).asObject();
										   });
						   
			TableColumn<Orphelin, Genre> genreColumn =  ((TableColumn<Orphelin,Genre>) orphelinsTableView.getColumns().get(4));
										 genreColumn.setCellValueFactory(orphelin -> {
											 return new SimpleObjectProperty(orphelin.getValue().getGenre().toString().charAt(0));
										 });
										 
			TableColumn<Orphelin, String> coutColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(5));
										  coutColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(evenementBon.getCoutBon() + "");
										  });
			
			orphelinsTableView.setMinHeight(25 * orphelins.size() + 30);
			orphelinsTableView.setMaxHeight(25 * orphelins.size() + 30);
			
			Scene scene = new Scene(rootPane);
			  	  scene.getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/DonationPrinting.css");
			
			orphelinsTableView.layoutYProperty().addListener(event -> { orphelinsTableView.setLayoutY(0.0); });
			
			Stage stageToPrint = new Stage();
				  stageToPrint.setScene(scene);
				  stageToPrint.show();
		
			((HBox) ((BorderPane) rootPane.getBottom()).getBottom()).requestFocus();
						
			orphelinsTableView.getSelectionModel().clearSelection();
				
			getPrinterJob().printPage(stageToPrint.getScene().getRoot());
				
			stageToPrint.close();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void printJoinedListTutorsOrphens(List<Orphelin> orphelins) {
		this.setPrinterJob(PrinterJob.createPrinterJob());
		
		this.getPrinterJob().showPageSetupDialog(null);
		
		PageLayout pageLayout = this.getPrinterJob().getJobSettings().getPageLayout();
		
		this.getPrinterJob().getJobSettings().setPageLayout(
				this.getPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), MarginType.HARDWARE_MINIMUM));
		
		int remainingOrphelins = orphelins.size();
		int pageIndex = 0;
		int currentOrphelin = 0;
		
		while(remainingOrphelins != 0) {
			ObservableList<Orphelin> currentOrphelins = FXCollections.observableList(new LinkedList<Orphelin>());
			
			int MAX_NUMBER_OF_ROWS_PER_PAGE = (pageIndex == 0 ? MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_FIRST_PAGE : MAX_NUMBER_OF_ROWS_PER_PAGE_PORTRAIT_SECOND_PAGE);
			int initialIndex = currentOrphelin;
			
			while((currentOrphelin < initialIndex + MAX_NUMBER_OF_ROWS_PER_PAGE) && (remainingOrphelins != 0)) {
				currentOrphelins.add(orphelins.get(currentOrphelin++));
				
				remainingOrphelins--;
			}
			
			this.printJoinedListTutorsOrphens(currentOrphelins, orphelins.size(), pageIndex == 0);
			
			pageIndex++;
		}
		
		this.getPrinterJob().endJob();
	}
	public void printJoinedListTutorsOrphens(List<Orphelin> orphelins, int nbOrphelins, boolean isFirstPage) {
		try {
			BorderPane rootPane = FXMLLoader.load(getClass().getResource(
				isFirstPage ? "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Summary/FirstPage.fxml" : 
							  "/mssmfactory/KafylElYatim/MVC/FXMLS/PrintingFXMLS/Summary/SecondPage.fxml").toURI().toURL()
			);
			
			TableView<Orphelin> orphelinsTableView = null;
			
			if(isFirstPage)
				orphelinsTableView = (TableView<Orphelin>) rootPane.getCenter();
			else orphelinsTableView = (TableView<Orphelin>) rootPane.getTop();
			
			final TableView<Orphelin> finalTableView = orphelinsTableView;
									  finalTableView.setItems(FXCollections.observableArrayList(orphelins));
			   
			TableColumn<Orphelin, String> tuteurColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(0));
										  tuteurColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(orphelin.getValue().getTuteur().getNom() + " " + orphelin.getValue().getTuteur().getPrenom());
										  });
						  
			TableColumn<Orphelin, String> orphelinColumn =  ((TableColumn<Orphelin,String>) orphelinsTableView.getColumns().get(1));
										  orphelinColumn.setCellValueFactory(orphelin -> {
											  return new SimpleStringProperty(orphelin.getValue().getNom() + " " + orphelin.getValue().getPrenom());
										  });
					
			TableColumn<Orphelin, Integer> ageColumn =  ((TableColumn<Orphelin,Integer>) orphelinsTableView.getColumns().get(2));
										   ageColumn.setCellValueFactory(orphelin -> {
											   return new SimpleIntegerProperty(orphelin.getValue().getAge()).asObject();
										   });
										   
			TableColumn<Orphelin, String> genreColumn =  ((TableColumn<Orphelin, String>) orphelinsTableView.getColumns().get(3));
										  genreColumn.setCellValueFactory(orphelin -> {
											  return new SimpleObjectProperty(orphelin.getValue().getGenre().equals(Genre.Masculin) ? "M" : "F");
										  });
										  
			TableColumn<Orphelin, String> nvScolColumn =  ((TableColumn<Orphelin, String>) orphelinsTableView.getColumns().get(4));
										  nvScolColumn.setCellValueFactory(orphelin -> {
											  NiveauScolaire nvSco = orphelin.getValue().getDossierScolaire().getNiveauScolaire();
											  return new SimpleStringProperty(nvSco == null ? "" : (String) nvSco.toString().replaceAll("_", " "));
										  });
			
			orphelinsTableView.setMinHeight(25 * orphelins.size() + 30);
			orphelinsTableView.setMaxHeight(25 * orphelins.size() + 30);
			
			Scene scene = new Scene(rootPane);
				  scene.getStylesheets().add("mssmfactory/KafylElYatim/MVC/CSS/SummaryPrinting.css");
				  
			orphelinsTableView.layoutYProperty().addListener(event -> { finalTableView.setLayoutY(0.0); });
				  
			Stage stageToPrint = new Stage();
			  	  stageToPrint.setScene(scene);
			  	  stageToPrint.show();
			  	
			if(isFirstPage) {
				HBox focusRequester = (HBox) rootPane.getBottom();
				
				Label nbOrphelinsLabel = (Label)(focusRequester.getChildren().get(1));
					  nbOrphelinsLabel.setText(nbOrphelins + "");
					  
				focusRequester.layoutYProperty().addListener(event -> { focusRequester.setLayoutY(finalTableView.getHeight()); });
				focusRequester.requestFocus();
			}
			else {
				HBox focusRequester = (HBox) rootPane.getBottom();
					 focusRequester.requestFocus();
			}
			
			orphelinsTableView.getSelectionModel().clearSelection();
			
			this.getPrinterJob().printPage(stageToPrint.getScene().getRoot());
			
			stageToPrint.close();
		
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public PrinterJob getPrinterJob() {
		return printerJob;
	}

	public void setPrinterJob(PrinterJob printer) {
		this.printerJob = printer;
	}
}
