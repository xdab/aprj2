package pl.so5dz.aprj2.plugin.aprsis;

import java.util.Map;

public record AprsIsConfig(
        String server,
        int port,
        String login,
        String passcode,
        String filter) {

    public static AprsIsConfig fromParameters(Map<String, Object> parameters) {
        String server = (String) parameters.getOrDefault("server", "rotate.aprs2.net");
        int port = Integer.parseInt((String) parameters.getOrDefault("port", 14580));
        String login = (String) parameters.getOrDefault("login", "N0CALL");
        String passcode = (String) parameters.getOrDefault("passcode", "-1");
        String filter = (String) parameters.getOrDefault("filter", "m/10");
        return new AprsIsConfig(server, port, login, passcode, filter);
    }
}
