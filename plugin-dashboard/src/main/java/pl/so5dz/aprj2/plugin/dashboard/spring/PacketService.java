package pl.so5dz.aprj2.plugin.dashboard.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.plugin.dashboard.dto.CallsignDto;
import pl.so5dz.aprj2.plugin.dashboard.dto.PacketDirection;
import pl.so5dz.aprj2.plugin.dashboard.dto.PacketDto;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Subscription;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.RxItem;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

@Service
@RequiredArgsConstructor
public class PacketService {
    private final Subscription<RxItem> rxSubscription = PubSub.topic(Topics.RX, RxItem.class).subscribe();
    private final Subscription<TxItem> txSubscription = PubSub.topic(Topics.TX, TxItem.class).subscribe();

    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService taskExecutor;

    private List<Future<?>> submittedTasks = new ArrayList<>();

    @PostConstruct
    public void startSendingPackets() {
        var rxTranslatorTask = taskExecutor.submit(() -> {
            RxItem rxItem;
            while (!Thread.currentThread().isInterrupted() && (rxItem = rxSubscription.awaitMessage()) != null) {
                messagingTemplate.convertAndSend("/topic/packets", toPacketDto(rxItem));
            }
        });
        submittedTasks.add(rxTranslatorTask);
        var txTranslatorTask = taskExecutor.submit(() -> {
            TxItem txItem;
            while (!Thread.currentThread().isInterrupted() && (txItem = txSubscription.awaitMessage()) != null) {
                messagingTemplate.convertAndSend("/topic/packets", toPacketDto(txItem));
            }
        });
        submittedTasks.add(txTranslatorTask);
    }

    @PreDestroy
    public void stopSendingPackets() {
        rxSubscription.cancel();
        txSubscription.cancel();
        submittedTasks.forEach(future -> future.cancel(true));
        taskExecutor.shutdown();
    }

    private PacketDto toPacketDto(RxItem rxItem) {
        return toPacketDto(rxItem.packet())
                .direction(PacketDirection.RX)
                .device(rxItem.sourceDeviceName())
                .build();
    }

    private PacketDto toPacketDto(TxItem txItem) {
        return toPacketDto(txItem.packet())
                .direction(PacketDirection.TX)
                .device(txItem.targetDeviceName())
                .build();
    }

    private PacketDto.PacketDtoBuilder toPacketDto(Packet packet) {
        List<CallsignDto> path = packet.getPath()
                .stream()
                .map(this::toCallsignDto)
                .collect(Collectors.toList());
        return PacketDto.builder()
                .rawPacket(packet.toString())
                .source(toCallsignDto(packet.getSource()))
                .destination(toCallsignDto(packet.getDestination()))
                .path(path);
    }

    private CallsignDto toCallsignDto(Callsign callsign) {
        String dispCallsign = callsign.getBase();
        if (callsign.getSsid() != 0) {
            dispCallsign += "-" + callsign.getSsid();
        }
        return CallsignDto.builder()
                .callsign(dispCallsign)
                .base(callsign.getBase())
                .ssid(callsign.getSsid())
                .repeated(callsign.isRepeated())
                .build();
    }
}