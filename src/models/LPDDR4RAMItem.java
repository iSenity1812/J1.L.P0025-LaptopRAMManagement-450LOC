package models;

import java.io.Serializable;

public class LPDDR4RAMItem extends RAMItem implements Serializable{
    private static final long serialVersionUID = 1L;
    public LPDDR4RAMItem(String code, String bus, String brand, int quantity, String productionDate, boolean active) {
        super(code, "LPDDR4", bus, brand, quantity, productionDate, active);
    }

    @Override
    public String getType() {
        return "LPDDR4";
    }
}
