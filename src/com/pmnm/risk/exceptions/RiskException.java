package com.pmnm.risk.exceptions;

public class RiskException extends RuntimeException {

	private static final long serialVersionUID = -1259909418166876045L;

	public RiskException() {}

	public RiskException(String s) {
		super(s);
	}

	public RiskException(Throwable t) {
		super(t);
	}

	public RiskException(String s, Throwable t) {
		super(s, t);
	}

	public RiskException(String s, Throwable t, boolean b, boolean b2) {
		super(s, t, b, b2);
	}
}
