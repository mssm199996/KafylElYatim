package mssmfactory.KafylElYatim.DAO;

import java.util.List;

import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Benevole;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Vehicule;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class VehiculeDAO {
	public List<Vehicule> getAllVehicules(){
		return (List<Vehicule>) (Object) (UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Vehicule"));
	}

	public void insertNewVehicule(Vehicule vehicule) {		
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(vehicule);
	}

	public void deleteVehicule(Vehicule vehicule) {
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Benevole> benevoles = (List<Benevole>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
						"FROM Benevole ben LEFT JOIN FETCH ben.vehicules"
				);
				
				for(Benevole ben: benevoles)
					ben.removeVehicule(vehicule);
				
				UtilitiesHolder.SESSION_FACTORY_HANDLER.update(benevoles.toArray(new Benevole[benevoles.size()]));
				UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(vehicule);
			}
		})).start();
	}
}
