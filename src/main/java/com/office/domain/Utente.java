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
    private String tipoUtente;
    
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
    public String getTipoUtente() {
		return tipoUtente;
    }
    public String getPassword() {
      return password;
    }
}