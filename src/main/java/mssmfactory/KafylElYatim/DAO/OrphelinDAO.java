package mssmfactory.KafylElYatim.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Appareillage;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierMedical;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.DossierScolaire.NiveauScolaire;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin.Genre;
import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;
import mssmfactory.KafylElYatim.MVC.Controllers.OrphensPanelController;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class OrphelinDAO {
	public void insertOrphelin(Orphelin orphelin) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(orphelin);
	}
	
	public List<Orphelin> getAllOrphelins(){
		return (List<Orphelin>) (Object) (UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
			"FROM Orphelin orphelin " +
			"LEFT JOIN FETCH orphelin.dossierFamilial " + 
			"LEFT JOIN FETCH orphelin.dossierMedical " + 
			"LEFT JOIN FETCH orphelin.apparencePhysique " + 
			"LEFT JOIN FETCH orphelin.dossierScolaire " + 
			"LEFT JOIN FETCH orphelin.tuteur as tuteur " + 
			"LEFT JOIN FETCH tuteur.habitat " + 
			"LEFT JOIN FETCH tuteur.dossierMedical " + 
			"LEFT JOIN FETCH tuteur.situationSociale AS ss " + 
			"LEFT JOIN FETCH ss.enquete "
		));
	}

	public List<Orphelin> getAuthorizedOrphelins(){
		return (List<Orphelin>) (Object) 
				UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
					"FROM Orphelin orphelin " +
					"LEFT JOIN FETCH orphelin.dossierFamilial " + 
					"LEFT JOIN FETCH orphelin.dossierMedical " + 
					"LEFT JOIN FETCH orphelin.apparencePhysique " +
					"LEFT JOIN FETCH orphelin.dossierScolaire " +
					"LEFT JOIN FETCH orphelin.tuteur as tuteur " + 
					"LEFT JOIN FETCH tuteur.habitat " + 
					"LEFT JOIN FETCH tuteur.dossierMedical " + 
					"LEFT JOIN FETCH tuteur.situationSociale AS ss " + 
					"LEFT JOIN FETCH ss.enquete " +
					
					"WHERE (orphelin.authorized = ?)", true
				);
	}
	
	public List<Orphelin> getAgedOrphelins(int ageMin, Genre genre){
		String request = 
				"FROM Orphelin orphelin " + 
				"LEFT  JOIN FETCH orphelin.dossierFamilial " + 
				"LEFT  JOIN FETCH orphelin.dossierMedical " + 
				"LEFT  JOIN FETCH orphelin.apparencePhysique " + 
				"LEFT  JOIN FETCH orphelin.dossierScolaire " + 
				"WHERE (orphelin.authorized = ? and orphelin.ddn <= ? and orphelin.genre = ?) order by orphelin.nom ASC, orphelin.prenom ASC";
		
		LocalDate minDate = LocalDate.now().minusYears(ageMin);
		
		LinkedList<Object> params = new LinkedList<Object>();
						   params.add(false);
						   params.add(minDate);
						   params.add(genre);
		
		return (List<Orphelin>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(request, params.toArray(new Object[params.size()]));
	}
	
	public List<Orphelin> getSpecifiedOrphelins(Tuteur tuteur, String names, Genre gender, int ageMin, int ageMax, NiveauScolaire niveauScolaire, Integer anneeScolaire, LocalDate dateMin, LocalDate dateMax, boolean includeAged) {
		if(tuteur == null && gender == null && ageMin == -1 && ageMax == -1 && (names == null || names.equals("")) && niveauScolaire == null && anneeScolaire == null &&
				dateMin == null && dateMax == null && includeAged)
			return this.getAllOrphelins();
		
		String request = "SELECT orphelin FROM Orphelin orphelin " +
						 "LEFT  JOIN FETCH orphelin.dossierFamilial " + 
						 "LEFT  JOIN FETCH orphelin.dossierMedical " + 
						 "LEFT  JOIN FETCH orphelin.apparencePhysique " +
						 "LEFT  JOIN FETCH orphelin.dossierScolaire " + 
						 "LEFT JOIN FETCH orphelin.tuteur as tuteur " + 
						 "LEFT JOIN FETCH tuteur.habitat " + 
						 "LEFT JOIN FETCH tuteur.dossierMedical " + 
						 "LEFT JOIN FETCH tuteur.situationSociale AS ss " + 
						 "LEFT JOIN FETCH ss.enquete ";
		LinkedList<Object> params = new LinkedList<Object>();
		
		if(niveauScolaire != null || anneeScolaire != null) 
			request += ", DossierScolaire as ds ";		
		
		request += "WHERE (";
		
		if(niveauScolaire != null) {
			request += "(ds.niveauScolaire = ? and orphelin.dossierScolaire = ds) and ";
			
			params.add(niveauScolaire);
		}
		
		if(anneeScolaire != null) {
			request += "(ds.anneeScolaire = ? and orphelin.dossierScolaire = ds) and ";
			
			params.add(anneeScolaire);
		}
				 
		if(tuteur != null) {
			request += "(orphelin.tuteur = ?) and ";
			params.add(tuteur);
		}
		
		if(gender != null) {
			request += "(orphelin.genre = ?) and ";
			params.add(gender);
		}
				
		if(ageMin != -1 && ageMax != -1) {
			LocalDate minDate = LocalDate.now().minusYears(ageMin);
			LocalDate maxDate = LocalDate.now().minusYears(ageMax + 1);
			
			request += "((orphelin.ddn between ? and ?) or (orphelin.ddn between ? and ?)) and ";
			params.add(minDate);
			params.add(maxDate);
			params.add(maxDate);
			params.add(minDate);
		}
		
		else if(ageMin != -1) {
			LocalDate minDate = LocalDate.now().minusYears(ageMin);
			
			request += "(orphelin.ddn <= ?) and ";
			params.add(minDate);
		}
		
		else if(ageMax != -1) {
			LocalDate maxDate = LocalDate.now().minusYears(ageMax + 1);
			
			request += "(orphelin.ddn >= ?) and ";
			params.add(maxDate);
		}
		
		if(dateMin != null && dateMax != null) {
			request += "((orphelin.ddn between ? and ?) or (orphelin.ddn between ? and ?)) and ";
			params.add(dateMin);
			params.add(dateMax);
			params.add(dateMax);
			params.add(dateMin);
		}
		else if(dateMin != null) {
			request += "(orphelin.ddn >= ?) and ";
			params.add(dateMin);
		}
		else if(dateMax != null) {
			request += "(orphelin.ddn <= ?) and ";
			params.add(dateMax);
		}
		
		if(names != null && !names.equals("")) {		
			String[] nameParams = this.tokenMePlease(names, " ");
			
			for(String nameParam: nameParams) {
				request += "(orphelin.nom like ? or orphelin.prenom like ?) and ";
				
				params.add("%" + nameParam + "%");
				params.add("%" + nameParam + "%");
			}
		}
		
		if(!includeAged){
			request += "(orphelin.ddn >= ? AND orphelin.genre = ? OR orphelin.ddn >= ? AND orphelin.genre = ?) and ";
			params.add(LocalDate.now().minusYears(OrphensPanelController.MALE_AGE_LIMIT + 1));
			params.add(Genre.Masculin);
			params.add(LocalDate.now().minusYears(OrphensPanelController.FEMALE_AGE_LIMIT + 1));
			params.add(Genre.Feminin);
		}
		
		request = request.substring(0, request.length() - 4);
		request += ")";
		
		return (List<Orphelin>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(request, params.toArray(new Object[params.size()]));
	}
	
	private String[] tokenMePlease(String something, String separators){
        List<String> results = new ArrayList<String>();
        
        StringTokenizer stringTokenizer = new StringTokenizer(something, separators);
        
        while(stringTokenizer.hasMoreElements())
            results.add((String) stringTokenizer.nextElement());
        
        return results.toArray(new String[results.size()]);
    }

	public void updateOrphelin(Orphelin orphelin) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(orphelin);
	}

	public void deleteOrphelin(Orphelin orphelin) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(orphelin);	
	}

	public void addNewAppareillage(Orphelin orphelin, Appareillage appareillage) {
		DossierMedical dm = (DossierMedical) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From DossierMedical dm LEFT JOIN FETCH dm.appareils WHERE dm.orphelin = ?", orphelin).get(0);
		 		 	   dm.addAppareillage(appareillage);
		 
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(dm);
	}
	
	public void deleteAppareillage(Orphelin orphelin, Appareillage appareillage) {
		DossierMedical dm = (DossierMedical) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From DossierMedical dm LEFT JOIN FETCH dm.appareils WHERE dm.orphelin = ?", orphelin).get(0);
	 	   			   dm.removeAppareillage(appareillage);

		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(dm);
	}

	public List<Appareillage> getAppareillages(Orphelin orphelin) {
		return ((List<DossierMedical>) (Object) 
				(UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM DossierMedical dm LEFT JOIN FETCH dm.appareils WHERE dm.orphelin = ?", orphelin))).get(0).getAppareils();
	}
}
