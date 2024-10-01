package models;

import java.io.Serializable;


public class DDR4RAMItem extends RAMItem implements Serializable{
    private static final long serialVersionUID = 1L;
    public DDR4RAMItem(String code, String bus, String brand, int quantity, String productionDate, boolean active) {
        super(code, "DDR4", bus, brand, quantity, productionDate, active);
    }

    @Override
    public String getType() {
        return "DDR4";
    }
}
