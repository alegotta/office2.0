package com.office.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
@Table(name="utenti")
public class Utente {

  
	@Id
	private int uid;
	private String name;
    private String sesso;
    private String password;
    private int annoNascita;
    private long cell;
    private int tipoUtente;
    //0 --> ADMIN
    //1 --> LAVORATORE
    
	public int getUid() {
		return uid;
	}
	public String getName() {
		return name;
    }
    public String getSesso() {
		return sesso;
    }
    public int getAnnoNascita() {
		return annoNascita;
    }
    public long getCell() {
		return cell;
    }
    public int getTipoUtente() {
		return tipoUtente;
    }
    public String getPassword() {
      return password;
    }
}