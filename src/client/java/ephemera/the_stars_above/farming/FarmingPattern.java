package ephemera.the_stars_above.farming;

public abstract class FarmingPattern {
    /**
     * Executes logic every client tick while macro is RUNNING.
     */
    public abstract void onTick();

    /**
     * Called when the pattern is started/resumed.
     */
    public abstract void start();

    /**
     * Called when the pattern is paused or stopped.
     */
    public abstract void stop();
    
    /**
     * Sync configuration values to this pattern instance.
     */
    public void syncConfig(int rowCount) {
        // default no-op
    }
}
