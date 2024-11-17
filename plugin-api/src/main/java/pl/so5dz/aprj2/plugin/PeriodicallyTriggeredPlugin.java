package pl.so5dz.aprj2.plugin;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PeriodicallyTriggeredPlugin implements Plugin {

    /**
     * Period between calls to {@link #onTrigger()}.
     */
    protected abstract Duration period();

    /**
     * Called periodically by the main loop.
     */
    protected abstract void onTrigger();

    @Override
    public final void run() {
        log.debug("Starting");
        while (true) {
            onTrigger();
            try {
                Thread.sleep(period().toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.debug("Finishing");
    }

}