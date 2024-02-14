package checkoutpaymentapi.model;

import checkoutpaymentapi.state.ProcessState;

/**  
 * DEFAULT    -  submit -> orderProcessor()   -> orderCreated   -> PMTPENDING
 * PMTPENDING -  pay    -> paymentProcessor() -> paymentError   -> PMTERROREMAILPENDING
 * PMTERROREMAILPENDING- errorEmail  -> paymentErrorEmailProcessor()   -> pmtErrorEmailSent   -> PMTPENDING
 * PMTPENDING -  pay    -> paymentProcessor() -> paymentSuccess -> PMTSUCCESSEMAILPENDING
 * PMTSUCCESSEMAILPENDING - successEmail -> paymentSuccessEmailProcessor() -> pmtSuccessEmailSent -> COMPLETED
 */
public enum OrderState implements ProcessState {
    Default,
    paymentErrorEmailPending,	
    PaymentPending,
    paymentSuccessEmailPending,
    Completed;
}
