package checkoutpaymentapi.state;

import java.util.function.Consumer;

public interface AsyncProcessor {
    public void processAsync(ProcessData data, Consumer<ProcessData> consumerFn);
}
