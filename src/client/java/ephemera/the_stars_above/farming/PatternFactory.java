package ephemera.the_stars_above.farming;

public class PatternFactory {
    public enum PatternType {
        STRAIGHT_LINE,
        SQUARE,
        MELON_PUMPKIN,
        MUSHROOM,
        WHEAT_CARROT_POTATO
    }

    public static FarmingPattern create(PatternType type) {
        switch (type) {
            case STRAIGHT_LINE:
                return new StraightLinePattern();
            case SQUARE:
                return new SquarePattern();
            case MELON_PUMPKIN:
                return new MelonPumpkinPattern();
            case MUSHROOM:
                return new MushroomPattern();
            case WHEAT_CARROT_POTATO:
                return new WheatCarrotPotatoPattern();
            default:
                return new StraightLinePattern();
        }
    }
}
