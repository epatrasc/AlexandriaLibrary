package com.alexandria.android.alexandrialibrary.model;

import java.sql.Date;

public class Prestito {
	private int idUtente;
	private String nomeUtente;
	private int idLibro;
	private String titoloLibro;
	private String urlImageLibro;
	private Date dataPrestito;
	private Date dataRestituzione;
	private boolean restituito;

	public Prestito() {
	}

	public Prestito(int idLibro) {
		this.idLibro = idLibro;
	}

	public Prestito(int idUtente, int idLibro) {
		this.idUtente = idUtente;
		this.idLibro = idLibro;
	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public Date getDataPrestito() {
		return dataPrestito;
	}

	public void setDataPrestito(Date dataPrestito) {
		this.dataPrestito = dataPrestito;
	}

	public Date getDataRestituzione() {
		return dataRestituzione;
	}

	public void setDataRestituzione(Date dataRestituzione) {
		this.dataRestituzione = dataRestituzione;
	}

	public boolean isRestituito() {
		return restituito;
	}

	public void setRestituito(boolean restituito) {
		this.restituito = restituito;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	public String getTitoloLibro() {
		return titoloLibro;
	}

	public void setTitoloLibro(String titoloLibro) {
		this.titoloLibro = titoloLibro;
	}

	public String getUrlImageLibro() {
		return urlImageLibro;
	}

	public void setUrlImageLibro(String urlImageLibro) {
		this.urlImageLibro = urlImageLibro;
	}
}
