package ee.andu.server.entity;

public enum PaymentState {
    INITIAL, // INITIATED
    SETTLED, // FINISHED
    FAILED, // TECHNICAL PROBLEM
    ABANDONED, // AFTER 15 MIN OF WAIT
    VOIDED, // CANCELLED
}
