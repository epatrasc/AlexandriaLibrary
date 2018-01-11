package com.alexandria.android.alexandrialibrary.model;

public class StatusResponse {
	private boolean done;
	private String messaggio;
	private String param;

	public StatusResponse(boolean done, String messaggio) {
		this.done = done;
		this.messaggio = messaggio;
	}

	public StatusResponse(boolean done, String messaggio, String param) {
		this.done = done;
		this.messaggio = messaggio;
		this.setParam(param);
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
