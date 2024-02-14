package checkoutpaymentapi.state;
/**
 * The Enum which is configured with state transitions
 * should implement this interface
 */
public interface ProcessEvent {
    public abstract Class<? extends AbstractProcessor> nextStepProcessor();   
    public abstract ProcessState nextState();
    public abstract String getMessage();
}
