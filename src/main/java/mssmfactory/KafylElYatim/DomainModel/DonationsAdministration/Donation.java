package mssmfactory.KafylElYatim.DomainModel.DonationsAdministration;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Donations")
public class Donation {
	private int id;
	private DonationType type;
	private DonationForm forme;
	private String valeur;
	private LocalDate date;
	private String observation;
	private Donator donator;
	
	@Id
	@Column(name = "ID_DONATION")
	@GenericGenerator(name = "donationIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "donationIdGenerator", strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "TYPE_DONATION")
	public DonationType getType() {
		return type;
	}
	public void setType(DonationType type) {
		this.type = type;
	}
	
	@Basic
	@Column(name = "VALEUR_DONATION")
	public String getValeur() {
		return valeur;
	}
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	
	@Basic
	@Column(name = "DATE_DONATION")
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@Basic
	@Column(name = "FORME_DONATION")
	public DonationForm getForme() {
		return forme;
	}
	public void setForme(DonationForm forme) {
		this.forme = forme;
	}
	
	@Basic
	@Column(name = "OBSERVATION_DONATION")
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DONATEUR")
	public Donator getDonator() {
		return donator;
	}
	public void setDonator(Donator donator) {
		this.donator = donator;
	}
	
	public static enum DonationType {
		Zakat,
		Sadaka,
	}
	
	public static enum DonationForm{
		Espece,
		Cheque,
		En_nature,
		Virement,
		Versement_bancaire
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Donation && (this.getId() == ((Donation) o).getId());
	}
}
