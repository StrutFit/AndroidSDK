package strutfit.button.enums;

import java.util.HashMap;
import java.util.Map;

public enum SizeUnit {
    US(0),
    UK(1),
    EU(2),
    AU(3),
    FR(4),
    DE(5),
    NZ(6),
    JP(7),
    CN(8),
    MX(9),
    BR(10),
    KR(11),
    IN(12),
    RU(13),
    SA(14),
    Mondopoint(15);

    private int value;
    private static Map map = new HashMap<>();

    private SizeUnit(int value) {
        this.value = value;
    }

    static {
        for (SizeUnit sizeUnit : SizeUnit.values()) {
            map.put(sizeUnit.value, sizeUnit);
        }
    }

    public static SizeUnit valueOf(int sizeUnit) {
        return (SizeUnit) map.get(sizeUnit);
    }

    public int getValue() {
        return value;
    }

    public static String getSizeUnitString(SizeUnit sizeUnit) {
        switch(sizeUnit) {
            case US:
                return "US";
            case UK:
                return "UK";
            case EU:
                return "EU";
            case AU:
                return "AU";
            case FR:
                return "FR";
            case DE:
                return "DE";
            case NZ:
                return "NZ";
            case JP:
                return "JP";
            case CN:
                return "CN";
            case MX:
                return "MX";
            case BR:
                return "BR";
            case KR:
                return "KR";
            case IN:
                return "IN";
            case RU:
                return "RU";
            case SA:
                return "SA";
            case Mondopoint:
                return "Mondopoint";
            default:
                return null;
        }
    }
}
