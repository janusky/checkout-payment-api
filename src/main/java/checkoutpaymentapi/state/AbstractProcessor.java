package checkoutpaymentapi.state;

import java.util.function.Consumer;

public abstract class AbstractProcessor implements Processor, AsyncProcessor {

    public ProcessData process(ProcessData data) {
        //subclasses implement body
        return null;
    }

    public void processAsync(ProcessData data, Consumer<ProcessData> consumerFn) {
        //subclasses implement body
    }

    public abstract boolean isAsync();
}
