/**
 *
 * @author isepipi
 */

package models;

import java.io.Serializable;

public class DDR5RAMItem extends RAMItem implements Serializable{
    private static final long serialVersionUID = 1L;
    public DDR5RAMItem(String code, String bus, String brand, int quantity, String productionDate, boolean active) {
        super(code, "DDR5", bus, brand, quantity, productionDate, active);
    }

    @Override
    public String getType() {
        return "DDR5";
    }
}
