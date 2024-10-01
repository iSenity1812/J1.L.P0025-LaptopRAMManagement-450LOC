/**
 *
 * @author isepipi
 */

package Factory;

import java.util.HashMap;
import java.util.Map;

import models.DDR4RAMItem;
import models.DDR5RAMItem;
import models.LPDDR4RAMItem;
import models.LPDDR5RAMItem;
import models.RAMItem;

public class RAMFactory {
    private static final Map<String, Integer> typeCounters = new HashMap<>();

    private RAMFactory() {

    }

    // Cap nhat typeCounters dua tren id da ton tai ()
    public static void initializeTypeCounters(Map<String, RAMItem> items) {
        for (RAMItem item : items.values())  {
            String type = item.getType();
            int id = extractId(item.getCode());

            // Lay id co stt cao nhat cua type input
            int currentMaxId = Math.max(typeCounters.getOrDefault(type, 0), id);
            typeCounters.put(type, currentMaxId);
        }
    }

    public static RAMItem createRAMItem(String type, String bus, String brand, int quantity, String productionDate, boolean active) {
        String code = generateUniqueCode(type);
        switch (type) {
            case "DDR4":
                return new DDR4RAMItem(code, bus, brand, quantity, productionDate, active);
            case "LPDDR4":
                return new LPDDR4RAMItem(code, bus, brand, quantity, productionDate, active);
            case "DDR5":
                return new DDR5RAMItem(code, bus, brand, quantity, productionDate, active);
            case "LPDDR5":
                return new LPDDR5RAMItem(code, bus, brand, quantity, productionDate, active);
            default:
                throw new IllegalArgumentException("Unknown RAM type: " + type);
        }
    }

    private static String generateUniqueCode(String type) {
        int nextId = typeCounters.getOrDefault(type, 0) + 1;
        typeCounters.put(type, nextId);
        return String.format("RAM%s_%d", type, nextId);
    }

    private static int extractId(String code) {
        // Lay stt cua id (eg: RAMDDR4_1 -> 1)
        String[] parts = code.split("_");
        return Integer.parseInt(parts[1]);
    }
}
