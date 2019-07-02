package com.solucionesdigitales.partner.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="APP")
public class App implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="N_ID_APP", nullable=false, unique=true)
	private Long idApp;

	@Column(name="C_ABBREVIATION", nullable=false, columnDefinition="CHAR(2) DEFAULT 'MA'")
	private String abbreviation;

	@Column(name="C_NAME", nullable=false, columnDefinition="CHAR(8) DEFAULT 'MAIN APP'")
	private String name;

	@Column(name="C_AUTHOR", nullable=false, columnDefinition="CHAR(12) DEFAULT 'OSAIN DABIAN'")
	private String author;

	@Temporal(TemporalType.DATE)
	@Column(name="D_DATE", nullable=false, columnDefinition="DATE DEFAULT '2018-08-18'")
	private Date date;	
	
	public App() {
	}
	public Long getIdApp() {
		return idApp;
	}
	public void setIdApp(Long idApp) {
		this.idApp = idApp;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "App [idApp="+idApp+", abbreviation="+abbreviation+", name="+name+", author="+author+", date="+date+"]";
	}
}
