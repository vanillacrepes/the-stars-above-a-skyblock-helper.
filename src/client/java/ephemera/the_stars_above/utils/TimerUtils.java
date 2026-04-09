package ephemera.the_stars_above.utils;

public class TimerUtils {
    private long startTime;

    public TimerUtils() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Starts or resets the timer.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Starts or resets the timer (alias for start).
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Gets the elapsed time since start.
     * @return time elapsed in milliseconds.
     */
    public long elapsed() {
        return System.currentTimeMillis() - this.startTime;
    }
    
    /**
     * Checks if a certain amount of milliseconds have elapsed.
     * @param ms The time to check against.
     * @return true if the elapsed time is greater or equal to requested ms.
     */
    public boolean hasPassed(long ms) {
        return elapsed() >= ms;
    }
}
