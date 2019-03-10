package com.pmnm.risk.exceptions;

public class RiskSingletonInstantiationException extends RiskException {

	private static final long serialVersionUID = 6469544450746293018L;

	public RiskSingletonInstantiationException() {}

	public <T> RiskSingletonInstantiationException(Class<T> thrower) {
		super(thrower.getName() + " is designed singleton, therefore should be instantiated more than once!");
	}

	public RiskSingletonInstantiationException(String s) {
		super(s);
	}

	public RiskSingletonInstantiationException(Throwable t) {
		super(t);
	}

	public RiskSingletonInstantiationException(String s, Throwable t) {
		super(s, t);
	}

	public RiskSingletonInstantiationException(String s, Throwable t, boolean b, boolean b2) {
		super(s, t, b, b2);
	}
}
