package pl.so5dz.aprj2.config.validation.validator.impl;

import java.util.Set;

import pl.so5dz.aprj2.config.SerialConfig;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.Validator;

public class SerialValidator implements Validator<SerialConfig> {
    private static final Set<Integer> COMMON_BAUD_VALUES = Set.of(
            300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200);

    @Override
    public ValidationResult validate(SerialConfig object) {
        ValidationResult result = new ValidationResult();
        if (object.getPort() == null || object.getPort().isBlank()) {
            result.addError("Serial port is missing");
        }
        if (object.getBaud() <= 0) {
            result.addError("Serial baud rate is invalid");
        } else if (!COMMON_BAUD_VALUES.contains(object.getBaud())) {
            result.addWarning("Serial baud rate has an uncommon value");
        }
        return result;
    }

}
