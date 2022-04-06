package hu.dominikvaradi.entities.enums;

public enum ERole {
    ROLE_USER("Felhasználó"),
    ROLE_CHEF("Szakács"),
    ROLE_MANAGER("Üzletvezető"),
    ROLE_ADMIN("Adminisztrátor");

    private final String name;

    ERole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
