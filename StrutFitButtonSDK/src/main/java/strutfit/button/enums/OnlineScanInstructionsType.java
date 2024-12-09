package strutfit.button.enums;

import java.util.HashMap;
import java.util.Map;

public enum OnlineScanInstructionsType {
    OneFootOnPaper(0),
    OneFootOffPaper(1),
    TwoFootPaper(2),
    PlasticCard(3);

    private int value;
    private static Map map = new HashMap<>();

    private OnlineScanInstructionsType(int value) {
        this.value = value;
    }

    static {
        for (OnlineScanInstructionsType onlineScanInstructionsType : OnlineScanInstructionsType.values()) {
            map.put(onlineScanInstructionsType.value, onlineScanInstructionsType);
        }
    }

    public static OnlineScanInstructionsType valueOf(int onlineScanInstructionsType) {
        return (OnlineScanInstructionsType) map.get(onlineScanInstructionsType);
    }

    public int getValue() {
        return value;
    }
}
