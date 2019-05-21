package mssmfactory.KafylElYatim.Utilities.ComponentsHandlers;

import java.io.IOException;
import java.net.URISyntaxException;

import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class DialogsBuilder {
	private HBox journeeDisponibleDialog = null;
	private HBox vehiculesDialog = null;
	private HBox appareillagesDialog = null;
	private BorderPane newEvenementBonDialog = null;
	private BorderPane newEvenementDechargeDialog = null;
	private BorderPane newDonatorDialog = null;

	public DialogsBuilder() {
		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					setJourneeDisponibleDialog((HBox) FXMLLoader.load(getClass().getResource(
						"/mssmfactory/KafylElYatim/MVC/FXMLS/Dialogs/JourneeDisponibleDialog.fxml").toURI().toURL()
					));
					
					setVehiculesDialog((HBox) FXMLLoader.load(getClass().getResource(
						"/mssmfactory/KafylElYatim/MVC/FXMLS/Dialogs/VehiculesDialog.fxml").toURI().toURL()
					));
					
					setAppareillagesDialog((HBox) FXMLLoader.load(getClass().getResource(
						"/mssmfactory/KafylElYatim/MVC/FXMLS/Dialogs/AppareillageDialog.fxml").toURI().toURL()
					));
					
					setNewEvenementBonDialog((BorderPane) FXMLLoader.load(getClass().getResource(
						"/mssmfactory/KafylElYatim/MVC/FXMLS/Dialogs/NewEvenementBonDialog.fxml").toURI().toURL()
					));
					
					setNewEvenementDechargeDialog((BorderPane) FXMLLoader.load(getClass().getResource(
						"/mssmfactory/KafylElYatim/MVC/FXMLS/Dialogs/NewEvenementDechargeDialog.fxml").toURI().toURL()
					));
					
					setNewDonatorDialog((BorderPane) FXMLLoader.load(getClass().getResource(
							"/mssmfactory/KafylElYatim/MVC/FXMLS/Dialogs/NewDonateurDialog.fxml").toURI().toURL()
					));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		})).start();
	}

	
	public HBox getJourneeDisponibleDialog() {
		return journeeDisponibleDialog;
	}

	public void setJourneeDisponibleDialog(HBox journeeDisponibleDialog) {
		this.journeeDisponibleDialog = journeeDisponibleDialog;
	}
	
	public HBox getVehiculesDialog() {
		return vehiculesDialog;
	}

	public void setVehiculesDialog(HBox vehiculesDialog) {
		this.vehiculesDialog = vehiculesDialog;
	}

	public void setAppareillagesDialog(HBox appareillagesDialog) {
		this.appareillagesDialog = appareillagesDialog;
	}
	
	public HBox getAppareillagesDialog() {
		return this.appareillagesDialog;
	}

	public void setNewEvenementBonDialog(BorderPane newEvenementBonDialog) {
		this.newEvenementBonDialog = newEvenementBonDialog;
	}

	public BorderPane getNewEvenementBonDialog() {
		return this.newEvenementBonDialog;
	}

	public BorderPane getNewEvenementDechargeDialog() {
		return this.newEvenementDechargeDialog;
	}
	
	public void setNewEvenementDechargeDialog(BorderPane newBorderPane) {
		this.newEvenementDechargeDialog = newBorderPane;
	}

	public BorderPane getNewDonatorDialog() {
		return newDonatorDialog;
	}

	public void setNewDonatorDialog(BorderPane newDonatorDialog) {
		this.newDonatorDialog = newDonatorDialog;
	}
}
