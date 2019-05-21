package mssmfactory.KafylElYatim.DAO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Benevole;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.JourneeDisponible;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.JourneeDisponible.JourDeSemaine;
import mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration.Vehicule;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class BenevoleDAO {
	public void insertNewBenevole(Benevole benevole) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(benevole);
	}
	
	public List<Benevole> getAllBenevoles(){
		return (List<Benevole>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Benevole");
	}
	
	public void updateBenevole(Benevole benevole) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(benevole);
	}
	
	public void deleteBenevole(Benevole benevole) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(benevole);
	}

	public List<Benevole> getSpecifiedBenevoles(String names, JourDeSemaine journee, Vehicule vehicule) {
		if((names == null || names.equals("")) && journee == null && vehicule == null)
			return this.getAllBenevoles();

		String request = "SELECT ben FROM Benevole ben ";
		
		if(journee != null)
			request += ", JourneeDisponible as jd ";
		
		if(vehicule != null)
			request += ", Vehicule as veh ";
		
		request += " WHERE (";
		
		List<Object> params = new LinkedList<Object>();		
		
		if(journee != null) {
			request += "(jd.jourSemaine = ? and jd in elements(ben.journeesDisponibles)) and ";
			
			params.add(journee);
		}
		
		if(vehicule != null) {
			request += "(veh = ? and veh in elements(ben.vehicules)) and ";
		
			params.add(vehicule);
		}
		
		if(names != null && !names.equals("")) {
			
			String[] nameParams = this.tokenMePlease(names, " ");
			
			for(String nameParam: nameParams) {
				request += "(ben.nom like ? or ben.prenom like ?) and ";
				
				params.add("%" + nameParam + "%");
				params.add("%" + nameParam + "%");
			}
		}
		
		request = request.substring(0, request.length() - 4);
		request += ")";
			
		List<Benevole> result = (List<Benevole>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
				request, params.toArray(new Object[params.size()])
		);
				
		return result;
	}
	
	public void insertNewJourneeDisponible(JourneeDisponible journeeDisponible) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(journeeDisponible);
	}
	
	public void deleteJourneeDisponible(JourneeDisponible journeeDisponible) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(journeeDisponible);
	}
	
	public void addNewVehicule(Benevole benevole, Vehicule vehicule) {
		Benevole ben = (Benevole) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From Benevole ben LEFT JOIN FETCH ben.vehicules WHERE ben = ?", benevole).get(0);
				 ben.addVehicule(vehicule);
				 
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(ben);
	}

	public void deleteVehiculeBenevole(Benevole benevole, Vehicule vehicule) {
		Benevole ben = (Benevole) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From Benevole ben LEFT JOIN FETCH ben.vehicules WHERE ben = ?", benevole).get(0);
		 		 ben.removeVehicule(vehicule);
		 
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(ben);
	}
	
	private String[] tokenMePlease(String something, String separators){
        List<String> results = new ArrayList<String>();
        
        StringTokenizer stringTokenizer = new StringTokenizer(something, separators);
        
        while(stringTokenizer.hasMoreElements())
            results.add((String) stringTokenizer.nextElement());
        
        return results.toArray(new String[results.size()]);
    }

	public List<JourneeDisponible> getJourneesDisponibles(Benevole benevole){
		return (List<JourneeDisponible>) (Object) (UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM JourneeDisponible jd "
				+ "WHERE jd.benevole = ?", benevole));
	}
	
	public List<Vehicule> getVehicules(Benevole benevole){
		return ((List<Benevole>) (Object) 
				(UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Benevole ben LEFT JOIN FETCH ben.vehicules WHERE ben = ?", benevole)))
				.get(0).getVehicules();
	}
}
