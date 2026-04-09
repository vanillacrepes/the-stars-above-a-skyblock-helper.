package ephemera.the_stars_above.farming;

public class PatternFactory {
    public enum PatternType {
        STRAIGHT_LINE,
        SQUARE
    }

    public static FarmingPattern create(PatternType type) {
        switch (type) {
            case STRAIGHT_LINE:
                return new StraightLinePattern();
            case SQUARE:
                return new SquarePattern();
            default:
                return new StraightLinePattern();
        }
    }
}
