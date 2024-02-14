package checkoutpaymentapi.state;

import checkoutpaymentapi.exception.ProcessException;

public abstract class AbstractStateTransitionsManager implements StateTransitionsManager {

    @Override
    public ProcessData processPreEvent(ProcessData data) throws ProcessException {
    	initializeState(data);
        return processStateTransition(data);
    }
   
    //to be called by the callback function
    protected abstract void processPostEvent(ProcessData data);

    protected abstract void initializeState(ProcessData data) throws ProcessException;
    
    //calls the processor with a callback function
    protected abstract ProcessData processStateTransition(ProcessData data) throws ProcessException;
}
