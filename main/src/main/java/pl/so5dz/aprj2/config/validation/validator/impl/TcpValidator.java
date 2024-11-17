package pl.so5dz.aprj2.config.validation.validator.impl;

import pl.so5dz.aprj2.config.TcpConfig;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.Validator;

public class TcpValidator implements Validator<TcpConfig> {
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;
    private static final int MAX_PRIVILEGED_PORT = 1023;

    @Override
    public ValidationResult validate(TcpConfig tcp) {
        ValidationResult result = new ValidationResult();
        if (tcp.getHost() == null || tcp.getHost().isBlank()) {
            result.addError("TCP host is missing");
        }
        if (tcp.getPort() == 0) {
            result.addError("TCP port is missing");
        } else if (tcp.getPort() < MIN_PORT) {
            result.addError("TCP port is below valid range");
        } else if (tcp.getPort() <= MAX_PRIVILEGED_PORT) {
            result.addWarning("TCP port is in privileged range");
        } else if (tcp.getPort() > MAX_PORT) {
            result.addError("TCP port is above valid range");
        }
        return result;
    }
}
