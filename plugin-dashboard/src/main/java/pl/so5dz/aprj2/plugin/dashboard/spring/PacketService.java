package pl.so5dz.aprj2.plugin.dashboard.spring;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.RxItem;

@Service
@RequiredArgsConstructor
public class PacketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService taskExecutor;

    private Future<?> packetTask;

    @PostConstruct
    public void startSendingPackets() {
        packetTask = taskExecutor.submit(() -> {
            var subscription = PubSub.topic(Topics.RX, RxItem.class).subscribe();
            RxItem rxItem;
            while (!Thread.currentThread().isInterrupted() && (rxItem = subscription.awaitMessage()) != null) {
                messagingTemplate.convertAndSend("/topic/packets", new PacketMessage(
                        "RX",
                        rxItem.sourceDeviceName(),
                        rxItem.packet().toString()));
            }
        });
    }

    @PreDestroy
    public void stopSendingPackets() {
        if (packetTask != null) {
            packetTask.cancel(true);
        }
        taskExecutor.shutdown();
    }
}