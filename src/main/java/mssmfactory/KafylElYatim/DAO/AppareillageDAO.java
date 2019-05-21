package mssmfactory.KafylElYatim.DAO;

import java.util.List;

import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Benevole;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Appareillage;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class AppareillageDAO {

	public List<Appareillage> getAllAppareillages(){
		return (List<Appareillage>) (Object) (UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Appareillage"));
	}
	
	public void insertNewAppareillage(Appareillage appareillage) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(appareillage);		
	}

	public void deleteAppareillage(Appareillage appareillage) {
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Orphelin> orphelins = (List<Orphelin>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Orphelin orp LEFT JOIN FETCH orp.dossierMedical.appareils"
				);
				
				for(Orphelin orphelin: orphelins)
					orphelin.getDossierMedical().removeAppareillage(appareillage);
				
				UtilitiesHolder.SESSION_FACTORY_HANDLER.update(orphelins.toArray(new Benevole[orphelins.size()]));
				UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(appareillage);
			}
		})).start();
	}
}
