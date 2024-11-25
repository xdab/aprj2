package pl.so5dz.aprj2.plugin.dashboard.packet;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.so5dz.aprj2.plugin.dashboard.packet.outgoing.ItemDto;
import pl.so5dz.aprj2.plugin.dashboard.packet.outgoing.MessageDto;
import pl.so5dz.aprj2.plugin.dashboard.packet.outgoing.ObjectDto;
import pl.so5dz.aprj2.plugin.dashboard.packet.outgoing.PositionReportDto;
import pl.so5dz.aprj2.plugin.dashboard.packet.outgoing.RawPacketDto;
import pl.so5dz.aprj2.plugin.dashboard.packet.outgoing.StatusDto;

@RestController
@RequestMapping("/api/packet")
@RequiredArgsConstructor
public class PacketRestController {
    private final PacketService packetService;

    @PostMapping
    public void sendPacket(@RequestBody RawPacketDto rawPacketDto) {
        packetService.sendPacket(rawPacketDto);
    }

    @PostMapping("/position")
    public void sendPositionReport(@RequestBody PositionReportDto positionReportDto) {
        packetService.sendPositionReport(positionReportDto);
    }

    @PostMapping("/status")
    public void sendStatus(@RequestBody StatusDto statusDto) {
        packetService.sendStatus(statusDto);
    }

    @PostMapping("/message")
    public void sendMessage(@RequestBody MessageDto messageDto) {
        packetService.sendMessage(messageDto);
    }

    @PostMapping("/object")
    public void sendObject(@RequestBody ObjectDto objectDto) {
        packetService.sendObject(objectDto);
    }

    @PostMapping("/item")
    public void sendItem(@RequestBody ItemDto itemDto) {
        packetService.sendItem(itemDto);
    }
}