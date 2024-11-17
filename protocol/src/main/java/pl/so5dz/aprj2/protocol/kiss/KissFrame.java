package pl.so5dz.aprj2.protocol.kiss;

import lombok.Data;

@Data
public class KissFrame {
    private boolean valid;
    private int port;
    private int command;
    private byte[] data;
}
