package strutfit.button.enums;

import java.util.HashMap;
import java.util.Map;

public enum Language {
        English(0),
        German(1),
        Italian(2),
        Dutch(3),
        French(4),
        Spanish(5),
        Swedish(6),
        Japanese(7),
        Norwegian(8),
        Portuguese(9),
        Croatian(10),
        Czech(11),
        Danish(12),
        Estonian(13),
        Finnish(14),
        Hungarian(15),
        Latvian(16),
        Lithuanian(17),
        Polish(18),
        Slovak(19),
        Slovenian(20);

    private int value;
    private static Map map = new HashMap<>();

    private Language(int value) {
        this.value = value;
    }

    static {
        for (Language language : Language.values()) {
            map.put(language.value, language);
        }
    }

    public static Language valueOf(int language) {
        return (Language) map.get(language);
    }

    public static Language valueFromCode(String languageCode) {
        switch (languageCode){
            case "en":
                return English;
            case "de":
                return German;
            case "it":
                return Italian;
            case "nl":
                return Dutch;
            case "fr":
                return French;
            case "es":
                return Spanish;
            case "sv":
                return Swedish;
            case "ja":
                return Japanese;
            case "no":
            case "nb":
                return Norwegian;
            case "pt":
                return Portuguese;
            case "hr":
                return Croatian;
            case "cs":
                return Czech;
            case "da":
                return Danish;
            case "et":
                return Estonian;
            case "fi":
                return Finnish;
            case "hu":
                return Hungarian;
            case "lv":
                return Latvian;
            case "lt":
                return Lithuanian;
            case "pl":
                return Polish;
            case "sk":
                return Slovak;
            case "sl":
                return Slovenian;
            default:
                return English;
        }
    }

    public int getValue() {
        return value;
    }
}
