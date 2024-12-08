package pl.so5dz.aprj2.plugin.beacon;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Topic;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

/**
 * Represents a beacon that is sent periodically on a single device.
 */
@Getter
@ToString
public class Beacon {
    private static final String APRSIS_DEVICE_NAME = "aprsis";
    private static final Topic<TxItem> txTopic = PubSub.topic(Topics.TX, TxItem.class);

    /**
     * Device on which the beacon will be sent.
     */
    private final String targetDeviceName;

    /**
     * Packet to be sent in the beacon.
     */
    private final Packet packet;

    /**
     * Interval between sending beacons.
     */
    private final Duration interval;

    /**
     * Time of the last sent beacon.
     */
    private LocalDateTime lastSent;

    /**
     * Whether to also send the beacon to APRS-IS.
     */
    private boolean aprsis;

    public Beacon(String targetDeviceName, Packet packet, Duration interval, boolean aprsis) {
        this.targetDeviceName = targetDeviceName;
        this.packet = packet;
        this.interval = interval;
        this.aprsis = aprsis;
        // Randomize the time of the last sent beacon to avoid
        // all beacons being sent at the same time.
        lastSent = LocalDateTime.now()
                .minusNanos((long) (Math.random() * interval.toNanos()));
    }

    /**
     * Sends the beacon if the interval has passed since the last sent beacon.
     */
    public void trigger() {
        LocalDateTime now = LocalDateTime.now();
        if (lastSent.plus(interval).isBefore(now)) {
            txTopic.publish(new TxItem(targetDeviceName, packet));
            if (aprsis) {
                txTopic.publish(new TxItem(APRSIS_DEVICE_NAME, packet));
            }
            lastSent = now;
        }
    }
}
