package pl.so5dz.aprj2.plugin;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Subscription;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.RxItem;

/**
 * Base class for plugins that intercept packets being received.
 */
@Slf4j
public abstract class PacketRxInterceptorPlugin extends InterceptorPlugin {

    private final Subscription<RxItem> rxSubscription = PubSub.topic(Topics.RX, RxItem.class).subscribe();

    /**
     * Called when a packet is received from a device that this plugin is
     * intercepting.
     *
     * @param sourceDeviceName name of device that received the packet
     * @param packet           received packet
     */
    protected abstract void onPacketReceived(String sourceDeviceName, Packet packet);

    @Override
    public final void run() {
        log.debug("Starting");
        RxItem rxItem;
        while ((rxItem = rxSubscription.awaitMessage()) != null) {
            if (isIntercepted(rxItem.sourceDeviceName())) {
                onPacketReceived(rxItem.sourceDeviceName(), rxItem.packet());
            }
        }
        log.debug("Finishing");
    }

    @Override
    public final void stop() {
        log.debug("Stopping");
        rxSubscription.cancel();
    }

}
