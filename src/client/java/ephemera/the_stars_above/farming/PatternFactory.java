package ephemera.the_stars_above.farming;

public class PatternFactory {
    public enum PatternType {
        MELON_PUMPKIN,
        MUSHROOM,
        WHEAT_CARROT_POTATO
    }

    public static FarmingPattern create(PatternType type) {
        switch (type) {
            case MELON_PUMPKIN:
                return new MelonPumpkinPattern();
            case MUSHROOM:
                return new MushroomPattern();
            case WHEAT_CARROT_POTATO:
            default:
                return new WheatCarrotPotatoPattern();
        }
    }
}
