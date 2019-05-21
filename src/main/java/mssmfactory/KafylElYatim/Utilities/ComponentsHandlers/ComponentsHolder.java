package mssmfactory.KafylElYatim.Utilities.ComponentsHandlers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierScolaire.NiveauScolaire;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin.Genre;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.EtatHabitat;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.TypeBien;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.SituationSociale.NiveauVie;

public abstract class ComponentsHolder {
	public static JFXComboBox<Integer> SEARCH_ANNEE_SCOLAIRE = null;
	public static JFXComboBox<NiveauScolaire> SEARCH_NIVEAU_SCOLAIRE = null;
	public static JFXDatePicker SEARCH_DDN_MIN = null;
	public static JFXDatePicker SEARCH_DDN_MAX = null;
	public static Stage SIGN_IN = null;
	public static Stage INTRODUCTION = null;
	public static JFXTextField SEARCH_TUTORS = null;
	public static JFXTextField SEARCH_NOM_ORPHELIN = null;
	public static JFXTextField SEARCH_AGE_MIN_ORPHELIN = null;
	public static JFXTextField SEARCH_AGE_MAX_ORPHELIN = null;
	public static JFXComboBox<Region> SEARCH_REGION_TUTEUR  = null;
	public static JFXComboBox<NiveauVie> SEARCH_NIVEAU_VIE_TUTEUR = null;
	public static JFXComboBox<Genre> SEARCH_GENRE_ORPHELIN = null;
	public static JFXComboBox<TypeBien> SEARCH_TYPE_HABITAT = null;
	public static JFXComboBox<EtatHabitat> SEARCH_ETAT_HABITAT = null;
	public static Image PLUS_IMAGE = null;
	public static Image MINUS_IMAGE = null;
	public static Image WINDOW_ICON = null;
}
