package checkoutpaymentapi.exception;

public class OrderException extends ProcessException {
	private static final long serialVersionUID = 5587859227419203629L;

	public OrderException(String message) {
		super(message);
	}

	public OrderException(String message, Throwable e) {
		super(message, e);
	}
}
