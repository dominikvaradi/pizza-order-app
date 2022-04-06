package hu.dominikvaradi.entities.enums;

public enum EOrderStatus {
    STATUS_UNDER_PROCESS("Feldolgozás alatt"),
    STATUS_UNDER_PREPARATION("Elkészítés alatt"),
    STATUS_UNDER_DELIVER("Kiszállítás alatt"),
    STATUS_REVOKED("Visszavonva"),
    STATUS_COMPLETED("Teljesítve");

    private final String name;

    EOrderStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
