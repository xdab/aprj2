package pl.so5dz.aprj2.config.validation.validator.impl;

import java.util.List;

import pl.so5dz.aprj2.config.DeviceConfig;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.Validator;

public class DeviceValidator implements Validator<DeviceConfig> {
    private static final List<String> RESERVED_NAMES = List.of(
            "aprsis", "error");

    @Override
    public ValidationResult validate(DeviceConfig device) {
        ValidationResult result = new ValidationResult();
        if (device.getName() == null || device.getName().isBlank()) {
            result.addError("Device name is missing");
        } else if (RESERVED_NAMES.contains(device.getName().toLowerCase())) {
            result.addError("Device name %s is reserved", device.getName());
        }
        if (device.getProtocol() == null) {
            result.addError("Device protocol is missing");
        }
        if (device.getTcp() == null && device.getSerial() == null) {
            result.addError("Device connectivity configuration is missing");
        }
        if (device.getTcp() != null && device.getSerial() != null) {
            result.addError("Device has both TCP and serial connectivity configuration");
        }
        if (device.getTcp() != null) {
            result.addFrom(device.getTcp().validate());
        }
        if (device.getSerial() != null) {
            result.addFrom(device.getSerial().validate());
        }
        if (!device.isRx() && !device.isTx()) {
            result.addError("Device can neither receive nor transmit");
        }
        return result;
    }
}
