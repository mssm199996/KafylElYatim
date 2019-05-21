package mssmfactory.KafylElYatim.DAO;

import java.util.List;

import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.Action;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.Evenement;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.EvenementBon;
import mssmfactory.KafylElYatim.DomainModel.EventsAdministration.EvenementDecharge;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class EvenementDAO {

	public void addNewEvenement(Evenement evenement) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(evenement);
	}
	
	public void deleteEvenement(Evenement evenement) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(evenement);
	}

	public List<EvenementBon> getAllEvenementBon() {
		return (List<EvenementBon>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM EvenementBon");
	}

	public List<EvenementDecharge> getAllEvenementDecharge() {
		return (List<EvenementDecharge>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM EvenementDecharge");
	}

	public void addNewActions(Action ... actions) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(actions);
	}
	

	public List<Action> getAllActions(Tuteur tuteur) {
		return (List<Action>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Action action LEFT OUTER JOIN action.evenement LEFT OUTER JOIN FETCH action.tuteur t WHERE t = ?", tuteur);
	}
}
