package checkoutpaymentapi.state;

import checkoutpaymentapi.exception.ProcessException;

public interface StateTransitionsManager {
    public ProcessData processPreEvent(ProcessData data) throws ProcessException;
}
