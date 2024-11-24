package pl.so5dz.aprj2.plugin.dashboard.packet;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/packets")
@RequiredArgsConstructor
public class PacketRestController {
    private final PacketService packetService;

    @PostMapping
    public void sendPacket(@RequestBody RawPacketDto rawPacket) {
        packetService.sendPacket(rawPacket);
    }
}