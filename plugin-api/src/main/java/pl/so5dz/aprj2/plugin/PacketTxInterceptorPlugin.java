package pl.so5dz.aprj2.plugin;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Subscription;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

/**
 * Base class for plugins that intercept packets about to be transmitted.
 */
@Slf4j
public abstract class PacketTxInterceptorPlugin extends InterceptorPlugin {

    protected final Subscription<TxItem> txSubscription = PubSub.topic(Topics.TX, TxItem.class).subscribe();

    /**
     * Called when a packet is being transmitted to a device that this plugin is
     * intercepting.
     *
     * @param targetDeviceName name of device that is supposed to send the packet
     * @param packet           received packet
     */
    protected abstract void onPacketToTransmit(String targetDeviceName, Packet packet);

    @Override
    public void run() {
        log.debug("Starting");
        TxItem txItem;
        while ((txItem = txSubscription.awaitMessage()) != null) {
            if (isIntercepted(txItem.targetDeviceName())) {
                onPacketToTransmit(txItem.targetDeviceName(), txItem.packet());
            }
        }
        log.debug("Finishing");
    }

}
