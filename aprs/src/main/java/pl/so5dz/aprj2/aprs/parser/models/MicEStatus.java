package pl.so5dz.aprj2.aprs.parser.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MicEStatus {
    OFF_DUTY(7, false),
    EN_ROUTE(6, false),
    IN_SERVICE(5, false),
    RETURNING(4, false),
    COMMITED(3, false),
    SPECIAL(2, false),
    PRIORITY(1, false),

    CUSTOM_0(7, true),
    CUSTOM_1(6, true),
    CUSTOM_2(5, true),
    CUSTOM_3(4, true),
    CUSTOM_4(3, true),
    CUSTOM_5(2, true),
    CUSTOM_6(1, true),

    EMERGENCY(0, false);

    private final int bits;
    private final boolean custom;
}
