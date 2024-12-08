package pl.so5dz.aprj2.aprs.representation;

import lombok.NonNull;
import pl.so5dz.aprj2.aprs.packet.Packet;

/**
 * Interface for converting packets between object and representation model
 * 
 * @param <T> type of packet representation
 */
public interface PacketRepresentation<T> {
    /**
     * Convert packet representation to object
     * 
     * @param repr packet in representation model
     * @return packet in object model
     */
    @NonNull
    Packet toPacket(@NonNull T repr);

    /**
     * Convert packet object to representation
     * 
     * @param packet packet in object model
     * @return packet in representation model
     */
    @NonNull
    T toRepresentation(@NonNull Packet packet);
}
