package pl.so5dz.aprj2.protocol.kiss;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KissConsts {
    public static final int FRAME_END = 0xc0;
    public static final int ESCAPE = 0xdb;
    public static final int ESCAPED_FRAME_END = 0xdc;
    public static final int ESCAPED_ESCAPE = 0xdd;
}
