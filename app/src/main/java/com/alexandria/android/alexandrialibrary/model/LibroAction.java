package com.alexandria.android.alexandrialibrary.model;

public class LibroAction {
	private Libro libro;
	private String action;

	public static final String PRESTA = "presta";
	public static final String RESTITUISCI = "restituisci";
	public static final String NO_ACTION = "no-action";
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Libro getLibro() {
		return libro;
	}
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	
}
