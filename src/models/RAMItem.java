package models;

import java.io.Serializable;

public abstract class RAMItem implements Serializable{
    private static final long serialVersionUID = 1L;
    private String code;
    private String type;
    private String bus;
    private String brand;
    private int quantity;
    private String productionDate;
    private boolean active;

    public RAMItem() {

    }

    public RAMItem(String code, String type, String bus, String brand, int quantity, String productionDate, boolean active) {
        this.code = code;
        this.type = type;
        this.bus = bus;
        this.brand = brand;
        this.quantity = quantity;
        this.productionDate = productionDate;
        this.active = active;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getproductionDate() {
        return productionDate;
    }

    public void setproductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("RAMItem{code='%s', type='%s', bus='%s', brand='%s', quantity=%d, productionDate=%s, active=%b}",
                code, type, bus, brand, quantity, productionDate, active);
    }
}
