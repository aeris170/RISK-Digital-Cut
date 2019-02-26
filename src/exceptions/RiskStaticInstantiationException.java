package exceptions;

public class RiskStaticInstantiationException extends RiskException {

	private static final long serialVersionUID = -2783635632688976417L;

	public RiskStaticInstantiationException() {}

	public <T> RiskStaticInstantiationException(Class<T> thrower) {
		super(thrower.getName() + " is static, therefore can not have any objects!");
	}

	public RiskStaticInstantiationException(String s) {
		super(s);
	}

	public RiskStaticInstantiationException(Throwable t) {
		super(t);
	}

	public RiskStaticInstantiationException(String s, Throwable t) {
		super(s, t);
	}

	public RiskStaticInstantiationException(String s, Throwable t, boolean b, boolean b2) {
		super(s, t, b, b2);
	}
}
