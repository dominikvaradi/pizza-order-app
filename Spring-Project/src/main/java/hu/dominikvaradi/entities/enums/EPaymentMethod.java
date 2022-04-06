package hu.dominikvaradi.entities.enums;

public enum EPaymentMethod {
    PAYMENT_METHOD_CASH("Készpénz"),
    PAYMENT_METHOD_CREDIT_CARD("Bankkártya/Hitelkártya");

    private final String name;

    EPaymentMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
