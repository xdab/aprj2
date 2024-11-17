package pl.so5dz.aprj2.aprs.representation;

import pl.so5dz.aprj2.aprs.models.Callsign;

/**
 * Interface for converting callsigns between object and representation model
 * 
 * @param <T> type of callsign representation
 */
public interface CallsignRepresentation<T> {
    /**
     * Convert callsign representation to object
     * 
     * @param repr callsign in representation model
     * @return callsign in object model
     */
    Callsign toCallsign(T repr);

    /**
     * Convert callsign object to representation
     * 
     * @param callsign callsign in object model
     * @return callsign in representation model
     */
    T toRepresentation(Callsign callsign);
}
