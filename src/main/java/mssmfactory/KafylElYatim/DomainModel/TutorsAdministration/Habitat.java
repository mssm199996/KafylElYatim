package mssmfactory.KafylElYatim.DomainModel.TutorsAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Habitats")
public class Habitat {
	
	private int id;
	private EtatHabitat etat;
	private TypeBien typeBien;
	private String classementHabitat;
	private Tuteur tuteur;
	
	@Id
	@GeneratedValue(generator = "habitatIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "habitatIdGenerator", strategy = "increment")
	@Column(name = "ID_HABITAT")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "ETAT_HABITAT")
	public EtatHabitat getEtat() {
		return etat;
	}
	public void setEtat(EtatHabitat etat) {
		this.etat = etat;
	}
	
	@Basic
	@Column(name = "TYPE_BIEN")
	public TypeBien getTypeBien() {
		return typeBien;
	}
	public void setTypeBien(TypeBien typeBien) {
		this.typeBien = typeBien;
	}
	
	@Basic
	@Column(name = "CLASSEMENT_HABITAT")
	public String getClassementHabitat() {
		return classementHabitat;
	}
	public void setClassementHabitat(String classementHabitat) {
		this.classementHabitat = classementHabitat;
	}
	
	@Basic
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TUTEUR")
	public Tuteur getTuteur() {
		return tuteur;
	}
	public void setTuteur(Tuteur tuteur) {
		this.tuteur = tuteur;
	}

	public static enum EtatHabitat{
		Bon,
		Moyen,
		Mauvais
	}
	
	public static enum TypeBien{
		Proprietaire,
		Locataire,
		Hebergé,
		Heritage,
		Illicite
	}
}
