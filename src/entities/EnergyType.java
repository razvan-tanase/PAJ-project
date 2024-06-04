package entities;

/**
 * Types of energy produced by EnergyProducers
 */
public enum EnergyType {
    WIND(true),
    SOLAR(true),
    HYDRO(true),
    COAL(false),
    NUCLEAR(false);

    private final boolean renewable;

    EnergyType(boolean renewable) {
        this.renewable = renewable;
    }

    public boolean isRenewable() {
        return renewable;
    }
}
