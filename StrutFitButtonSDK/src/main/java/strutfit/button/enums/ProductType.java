package strutfit.button.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductType {
    Footwear(0),
    Apparel(1);

    private int value;
    private static Map map = new HashMap<>();

    private ProductType(int value) {
        this.value = value;
    }

    static {
        for (ProductType productType : ProductType.values()) {
            map.put(productType.value, productType);
        }
    }

    public static ProductType valueOf(int productType) {
        return (ProductType) map.get(productType);
    }

    public int getValue() {
        return value;
    }
}
