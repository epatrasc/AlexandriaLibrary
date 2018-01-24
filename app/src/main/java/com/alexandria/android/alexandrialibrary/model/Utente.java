package com.alexandria.android.alexandrialibrary.model;


import com.alexandria.android.alexandrialibrary.utils.Type;

public class Utente {
	private int id;
	private String nome;
	private String password;
	private String ruolo;
	private boolean attivo;

	private enum Ruoli {
		cliente, amministratore;
	}
	
	public Utente() {
	}

	public Utente(String nome, String password) {
		this.nome = nome;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		if (Type.isInEnum(ruolo, Ruoli.class)) {
			this.ruolo = ruolo;
		}
	}
	
	public boolean isAmministratore() {
		return ruolo.equals(Ruoli.amministratore.toString());
	}
	
	public boolean isCliente() {
		return ruolo.equals(Ruoli.cliente.toString());
	}
	
	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	
	public static String amministratore(){
		return Ruoli.amministratore.toString();
	}
	
	public static String cliente(){
		return Ruoli.cliente.toString();
	}
}
