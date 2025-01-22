package strutfit.button.enums;

import java.util.HashMap;
import java.util.Map;

public enum PostMessageType {
    UserAuthData(0),
    UserFootMeasurementCodeData(1),
    UserBodyMeasurementCodeData(2),
    CloseIFrame(3),
    ShowIFrame(4),
    IframeReady(5),
    ABTestInfo(6),
    ReplicaButtonProductLoad(7),
    SizeGuideOpened(8),
    DeviceMotion(9),
    DeviceOrientation(10),
    UserAcceptedCookies(11),
    UpdateProduct(12),
    UpdateTheme(13),
    ScanSucceeded(14),
    RequestDeviceMotionAndOrientation(15),
    ReprocessSize(16),
    ReplicaButtonData(17),
    InitialAppInfo(18),
    LanguageChange(19),
    IsIframeReady(20);

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
