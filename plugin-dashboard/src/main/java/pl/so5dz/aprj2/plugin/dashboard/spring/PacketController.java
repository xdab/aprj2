package pl.so5dz.aprj2.plugin.dashboard.spring;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacketController {

    @MessageMapping("/packets")
    @SendTo("/topic/packets")
    public PacketMessage sendPacketMessage(PacketMessage message, StompHeaderAccessor headerAccessor) {
        return message;
    }

    @SubscribeMapping("/packets")
    public void subscribePacketMessage() {
    }

}