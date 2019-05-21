package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationForm;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationType;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donator;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class BilansDonationsController implements Initializable{

	public static ObservableList<Donation> DONATIONS = FXCollections.observableList(new LinkedList<Donation>());
	public static SimpleObjectProperty<Donator> SELECTED_DONATOR = new SimpleObjectProperty<Donator>();
	
    @FXML private JFXDatePicker SearchDonationDateDebutDatePicker, SearchDonationDateFinDatePicker;
    @FXML private TableView<Donation> DonationsTableView;
    @FXML private Label resultCount, resultAmount;
    
	private void refreshDonations() {
		LocalDate startDate = this.SearchDonationDateDebutDatePicker.getValue();
		LocalDate endDate = this.SearchDonationDateFinDatePicker.getValue();
				
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Donation> result = UtilitiesHolder.DONATION_DAO.getSpecifiedDonations(-1, null, startDate, endDate, BilansDonationsController.SELECTED_DONATOR.getValue());
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						BilansDonationsController.DONATIONS.clear();
						BilansDonationsController.DONATIONS.addAll(result);
						
						double sum = 0;
						
						for(Donation donation: BilansDonationsController.DONATIONS) {
							try { sum += Double.parseDouble(donation.getValeur()); }
							catch(NumberFormatException exp) {}
						}
						
						resultCount.setText("Nombre de donations: " + BilansDonationsController.DONATIONS.size());
						resultAmount.setText("Total des donations: " + sum);
					}
				});
			}
		})).start();
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initProperties();
		this.initDatePickers();
		this.initTableView();
	}
	
	private void initProperties() {
		BilansDonationsController.SELECTED_DONATOR.addListener(new ChangeListener<Donator>() {
			@Override
			public void changed(ObservableValue<? extends Donator> observable, Donator oldValue, Donator newValue) {
				if(newValue != null) refreshDonations();
				else BilansDonationsController.DONATIONS.clear();
			}
		});
	}
	
	private void initDatePickers() {
		this.SearchDonationDateDebutDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				refreshDonations();
			}
		});
		
		this.SearchDonationDateFinDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				refreshDonations();
			}
		});
	}
	
	private void initTableView() {
		this.DonationsTableView.setItems(BilansDonationsController.DONATIONS);
		
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
}
