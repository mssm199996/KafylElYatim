package mssmfactory.KafylElYatim.DAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class RegionDAO {
	public List<Region> getAllRegions() {
		return (List<Region>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Region");
	}

	public void insertNewRegion(Region region) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(region);
	}

	public Region fetchRegionByTuteur(Tuteur tuteur) {
		List<Tuteur> result = (List<Tuteur>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER
				.select("FROM Tuteur tuteur INNER JOIN FETCH tuteur.region WHERE tuteur = ?", tuteur);

		if (!result.isEmpty()) {
			Tuteur fullTuteur = result.get(0);

			return fullTuteur.getRegion();
		}

		return null;
	}

	public void deleteRegion(Region region) {
		(new Thread(new Runnable() {
			@Override
			public void run() {
				List<Tuteur> tuteurs = (List<Tuteur>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER
						.select("FROM Tuteur tut WHERE tut.region = ?", region);

				for (Tuteur tut : tuteurs)
					tut.setRegion(null);

				UtilitiesHolder.SESSION_FACTORY_HANDLER.update(tuteurs.toArray(new Tuteur[tuteurs.size()]));
				UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(region);
			}
		})).start();
	}
}
