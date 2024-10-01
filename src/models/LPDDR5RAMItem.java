package models;

import java.io.Serializable;

public class LPDDR5RAMItem extends RAMItem implements Serializable{
    private static final long serialVersionUID = 1L;
    public LPDDR5RAMItem(String code, String bus, String brand, int quantity, String productionDate, boolean active) {
        super(code, "LPDDR5", bus, brand, quantity, productionDate, active);
    }

    @Override
    public String getType() {
        return "LPDDR5";
    }
}
