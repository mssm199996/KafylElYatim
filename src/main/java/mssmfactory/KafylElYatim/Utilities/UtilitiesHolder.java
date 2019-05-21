package mssmfactory.KafylElYatim.Utilities;

import mssmfactory.KafylElYatim.DAO.AppareillageDAO;
import mssmfactory.KafylElYatim.DAO.BenevoleDAO;
import mssmfactory.KafylElYatim.DAO.DonationDAO;
import mssmfactory.KafylElYatim.DAO.EvenementDAO;
import mssmfactory.KafylElYatim.DAO.OrphelinDAO;
import mssmfactory.KafylElYatim.DAO.RegionDAO;
import mssmfactory.KafylElYatim.DAO.TuteurDAO;
import mssmfactory.KafylElYatim.DAO.VehiculeDAO;
import mssmfactory.KafylElYatim.MVC.SubStages.AuthorizedOrphensStage;
import mssmfactory.KafylElYatim.MVC.SubStages.BilansDonationsStage;
import mssmfactory.KafylElYatim.MVC.SubStages.ConfigurationStage;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.AlertsDisplayer;
import mssmfactory.KafylElYatim.Utilities.ComponentsHandlers.DialogsBuilder;
import mssmfactory.KafylElYatim.Utilities.DataHandlers.SessionFactoryHandler;
import mssmfactory.KafylElYatim.Utilities.HardwareHandlers.PrintingHandler;

public abstract class UtilitiesHolder {
	public static SessionFactoryHandler SESSION_FACTORY_HANDLER = new SessionFactoryHandler();
	public static PrintingHandler PRINTING_HANDLER = new PrintingHandler();
	public static DonationDAO DONATION_DAO = new DonationDAO();
	public static TuteurDAO TUTEUR_DAO = new TuteurDAO();
	public static RegionDAO REGION_DAO = new RegionDAO();
	public static OrphelinDAO ORPHELIN_DAO = new OrphelinDAO();
	public static BenevoleDAO BENEVOLE_DAO = new BenevoleDAO();
	public static VehiculeDAO VEHICULE_DAO = new VehiculeDAO();
	public static EvenementDAO EVENEMENT_DAO = new EvenementDAO();
	public static DialogsBuilder DIALOGS_BUILDER = new DialogsBuilder();
	public static AppareillageDAO APPAREILLAGE_DAO = new AppareillageDAO();
	public static ConfigurationStage CONFIGURATION_STAGE = null;
	public static AuthorizedOrphensStage AUTHORIZED_ORPHENS_STAGE = null;
	public static BilansDonationsStage BILANS_DONATIONS_STAGE = null;
	public static AlertsDisplayer ALERTS_DISPLAYER = new AlertsDisplayer();
}
