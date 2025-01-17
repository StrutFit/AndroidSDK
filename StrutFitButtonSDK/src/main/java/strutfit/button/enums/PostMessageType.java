package strutfit.button.enums;

import java.util.HashMap;
import java.util.Map;

public enum PostMessageType {
    UserAuthData(0),
    UserFootMeasurementCodeData(1),
    UserBodyMeasurementCodeData(2),
    CloseIFrame(3),
    ShowIFrame(4),
    ProductInfo(5),
    IframeReady(6),
    ABTestInfo(7),
    ReplicaButtonProductLoad(8),
    SizeGuideOpened(9),
    DeviceMotion(10),
    DeviceOrientation(11),
    UserAcceptedCookies(12),
    UpdateProduct(13),
    UpdateTheme(14),
    ScanSucceeded(15),
    RequestDeviceMotionAndOrientation(16),
    ReprocessSize(17),
    ReplicaButtonData(18),
    InitialAppInfo(19),
    LanguageChange(20);

    private int value;
    private static Map map = new HashMap<>();

    private PostMessageType(int value) {
        this.value = value;
    }

    static {
        for (PostMessageType postMessageType : PostMessageType.values()) {
            map.put(postMessageType.value, postMessageType);
        }
    }

    public static PostMessageType valueOf(int postMessageType) {
        return (PostMessageType) map.get(postMessageType);
    }

    public int getValue() {
        return value;
    }
}
