package mssmfactory.KafylElYatim.MVC.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin.Genre;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;

public class AuthorizedOrphens implements Initializable{

	@FXML private Label resultCount;
    @FXML private TableView<Orphelin> tableOrphelins;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initTables();
	}
	
	private void initTables() {
		this.tableOrphelins.setItems(OrphensPanelController.ORPHELINS_AGES_AUTHORIZED);
		
		OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.addListener(new ListChangeListener<Orphelin>() {
			@Override
			public void onChanged(Change<? extends Orphelin> arg0) {
				resultCount.setText("Nombre d'orphelins autorises: " + OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.size());
			}
		});
		
		resultCount.setText("Nombre d'orphelins autorises: " + OrphensPanelController.ORPHELINS_AGES_AUTHORIZED.size());
		
		TableColumn<Orphelin, Integer> idColumn =  ((TableColumn<Orphelin,Integer>) this.tableOrphelins.getColumns().get(0));
									   idColumn.setCellValueFactory(orphelin -> {
										   return new SimpleIntegerProperty(orphelin.getValue().getId()).asObject();
									   });
		   
		TableColumn<Orphelin, String> nomColumn =  ((TableColumn<Orphelin,String>) this.tableOrphelins.getColumns().get(1));
									  nomColumn.setCellValueFactory(orphelin -> {
										  return new SimpleStringProperty(orphelin.getValue().getNom());
									  });
				  
		TableColumn<Orphelin, String> prenomColumn =  ((TableColumn<Orphelin,String>) this.tableOrphelins.getColumns().get(2));
									  prenomColumn.setCellValueFactory(orphelin -> {
										  return new SimpleStringProperty(orphelin.getValue().getPrenom());
									  });
				  
		TableColumn<Orphelin, LocalDate> ddnColumn =  ((TableColumn<Orphelin, LocalDate>) this.tableOrphelins.getColumns().get(3));
									  	 ddnColumn.setCellValueFactory(orphelin -> {
									  		 return new SimpleObjectProperty(orphelin.getValue().getDdn().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
									  	 });
			
		TableColumn<Orphelin, Integer> ageColumn =  ((TableColumn<Orphelin,Integer>) this.tableOrphelins.getColumns().get(4));
									   ageColumn.setCellValueFactory(orphelin -> {
										   return new SimpleIntegerProperty(orphelin.getValue().getAge()).asObject();
									   });
				   
		TableColumn<Orphelin, Genre> genreColumn =  ((TableColumn<Orphelin,Genre>) this.tableOrphelins.getColumns().get(5));
									 genreColumn.setCellValueFactory(orphelin -> {
										 return new SimpleObjectProperty(orphelin.getValue().getGenre());
									 });
				 
		TableColumn<Orphelin, Tuteur> tuteurColumn =  ((TableColumn<Orphelin, Tuteur>) this.tableOrphelins.getColumns().get(6));
									  tuteurColumn.setCellValueFactory(orphelin -> {
										  return new SimpleObjectProperty(orphelin.getValue().getTuteur());
									  });
				 
		TableColumn<Orphelin, String> RATColumn =  ((TableColumn<Orphelin, String>) this.tableOrphelins.getColumns().get(7));
									  RATColumn.setCellValueFactory(orphelin -> {
										  return new SimpleStringProperty(orphelin.getValue().getRelationOrphelinTuteur());
									  });
	}
}
