package mssmfactory.KafylElYatim.DAO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Appareillage;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.DossierMedical;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.EtatHabitat;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Habitat.TypeBien;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Region;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.SituationSociale.NiveauVie;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class TuteurDAO {
	public void insertTuteur(Tuteur tuteur) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(tuteur);
	}
	
	public List<Tuteur> getAllTuteurs(){
		return (List<Tuteur>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
			"FROM Tuteur tuteur " + 
			"LEFT OUTER JOIN FETCH tuteur.region " +
			"LEFT OUTER JOIN FETCH tuteur.habitat " + 
			"LEFT OUTER JOIN FETCH tuteur.dossierMedical " + 
			"LEFT OUTER JOIN FETCH tuteur.situationSociale AS ss " + 
			"LEFT OUTER JOIN FETCH ss.enquete"
		);
	}
	
	public List<Tuteur> getSpecifiedTuteurs(String names, Region region, NiveauVie ndv, EtatHabitat etatHabitat, TypeBien typeBien){
		if((names == null || names.equals("")) && region == null && ndv == null && etatHabitat == null && typeBien == null)
			return this.getAllTuteurs();
		
		String request = "FROM Tuteur tuteur " + 
			"LEFT OUTER JOIN FETCH tuteur.region " +
			"LEFT OUTER JOIN FETCH tuteur.habitat " + 
			"LEFT OUTER JOIN FETCH tuteur.dossierMedical " +
			"LEFT OUTER JOIN FETCH tuteur.situationSociale AS ss " + 
			"LEFT OUTER JOIN FETCH ss.enquete " + 
			"WHERE (";
		List<Object> params = new LinkedList<Object>();
		
		if(region != null) {
			request += "(tuteur.region = ?) and ";
			
			params.add(region);
		}
		
		if(ndv != null) {
			request += "(tuteur.situationSociale.niveauVie = ?) and ";
			
			params.add(ndv);
		}
		
		if(etatHabitat != null) {
			request += "(tuteur.habitat.etat = ?) and ";
			
			params.add(etatHabitat);
		}
		
		if(typeBien != null) {
			request += "(tuteur.habitat.typeBien = ?) and ";
			
			params.add(typeBien);
		}
		
		if(names != null && !names.equals("")) {
			
			String[] nameParams = this.tokenMePlease(names, " ");
			
			for(String nameParam: nameParams) {
				request += "(tuteur.nom like ? or tuteur.prenom like ?) and ";
				
				params.add("%" + nameParam + "%");
				params.add("%" + nameParam + "%");
			}
		}
		
		request = request.substring(0, request.length() - 4);
		request += ")";
		
		return (List<Tuteur>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
			request, params.toArray(new Object[params.size()])
		);
	}
	
	public void updateTuteur(Tuteur tuteur) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(tuteur);
	}
	
	private String[] tokenMePlease(String something, String separators){
        List<String> results = new ArrayList<String>();
        
        StringTokenizer stringTokenizer = new StringTokenizer(something, separators);
        
        while(stringTokenizer.hasMoreElements())
            results.add((String) stringTokenizer.nextElement());
        
        return results.toArray(new String[results.size()]);
    }

	public void deleteTuteur(Tuteur tuteur) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.executeHql("DELETE Orphelin orph WHERE orph.tuteur = ?", tuteur);
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(tuteur);
	}
	
	public void addNewAppareillage(Tuteur tuteur, Appareillage appareillage) {
		DossierMedical dm = (DossierMedical) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From DossierMedicalTuteur dm LEFT JOIN FETCH dm.appareils WHERE dm.tuteur = ?", tuteur).get(0);
		 		 	   dm.addAppareillage(appareillage);
		 
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(dm);
	}
	
	public void deleteAppareillage(Tuteur tuteur, Appareillage appareillage) {
		DossierMedical dm = (DossierMedical) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From DossierMedicalTuteur dm LEFT JOIN FETCH dm.appareils WHERE dm.tuteur = ?", tuteur).get(0);
	 	   			   dm.removeAppareillage(appareillage);

		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(dm);
	}

	public List<Appareillage> getAppareillages(Tuteur tuteur) {
		return ((List<DossierMedical>) (Object) 
				(UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM DossierMedicalTuteur dm LEFT JOIN FETCH dm.appareils WHERE dm.tuteur = ?", tuteur))).get(0).getAppareils();
	}
}
