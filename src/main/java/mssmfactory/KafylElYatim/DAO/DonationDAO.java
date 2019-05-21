package mssmfactory.KafylElYatim.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donator;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation;
import mssmfactory.KafylElYatim.DomainModel.DonationsAdministration.Donation.DonationType;
import mssmfactory.KafylElYatim.Utilities.UtilitiesHolder;

public class DonationDAO {
	public void insertNewDonator(Donator donateur) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(donateur);
	}
	
	public void deleteDonator(Donator donateur) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(donateur);
	}
	
	public List<Donator> getAllDonators(){
		return (List<Donator>) (Object) (UtilitiesHolder.SESSION_FACTORY_HANDLER.select("From Donator"));
	}
	
	public List<Donator> getSpecifiedDonators(String names){
		if(names != null && !names.equals("")) {
			String request = "From Donator donateur WHERE (";
			List<String> params = new LinkedList<String>();
			
			String[] nameParams = this.tokenMePlease(names, " ");
			
			for(String nameParam: nameParams) {
				request += "(donateur.nom like ? or donateur.prenom like ?) and ";
				
				params.add("%" + nameParam + "%");
				params.add("%" + nameParam + "%");
			}
			
			request = request.substring(0, request.length() - 4);
			request += ")";
						
			return (List<Donator>) (Object) 
					(UtilitiesHolder.SESSION_FACTORY_HANDLER.select(request, params.toArray(new Object[params.size()])));
		}
		else return this.getAllDonators();
	}
	
	public void insertDonation(Donation donation) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.insert(donation);
	}

	public List<Donation> getAllDonations() {
		return (List<Donation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select("FROM Donation donation LEFT OUTER JOIN FETCH donation.donator");
	}

	public void updateDonation(Donation donation) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.update(donation);
	}
	
	public List<Donation> getSpecifiedDonations(int donationId, DonationType donationType, LocalDate startDate, LocalDate endDate, Donator donator){
		if(donationId == -1 && donationType == null && startDate == null && endDate == null && donator == null)
			return this.getAllDonations();
		
		if(donationId != -1)
			return (List<Donation>) (Object) UtilitiesHolder.SESSION_FACTORY_HANDLER.select(
					"FROM Donation donation LEFT OUTER JOIN FETCH donation.donator "
					+ "WHERE donation.id = ?", donationId
			);
		
		String request = "FROM Donation donation LEFT OUTER JOIN FETCH donation.donator WHERE (";
		LinkedList<Object> params = new LinkedList<Object>();
				
		if(donationType != null) {
			request += "(donation.type = ?) and ";
			params.add(donationType);
		}
		
		if(donator != null) {
			request += "(donation.donator = ?) and ";
			
			params.add(donator);
		}
		
		if(startDate != null && endDate != null) {
			request += " (donation.date BETWEEN ? and ?) and ";
			
			params.add(startDate);
			params.add(endDate);
		}
		
		else if(startDate != null) {
			request +=  "(donation.date >= ?) and ";
			
			params.add(startDate);
		}
		
		else if(endDate != null) {
			request +=  "(donation.date <= ?) and ";
			
			params.add(endDate);
		}
		
		request = request.substring(0, request.length() - 4);
		request += ")";

		return (List<Donation>) (Object) 
					UtilitiesHolder.SESSION_FACTORY_HANDLER.select(request, params.toArray(new Object[params.size()]));
	}

	public void deleteDonation(Donation donation) {
		UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(donation);
	}
	
	private String[] tokenMePlease(String something, String separators){
        List<String> results = new ArrayList<String>();
        
        StringTokenizer stringTokenizer = new StringTokenizer(something, separators);
        
        while(stringTokenizer.hasMoreElements())
            results.add((String) stringTokenizer.nextElement());
        
        return results.toArray(new String[results.size()]);
    }
}
