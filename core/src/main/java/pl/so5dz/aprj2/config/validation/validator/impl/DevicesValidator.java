package pl.so5dz.aprj2.config.validation.validator.impl;

import java.util.List;
import java.util.stream.Collectors;

import pl.so5dz.aprj2.config.DeviceConfig;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.Validator;

public class DevicesValidator implements Validator<List<DeviceConfig>> {

    @Override
    public ValidationResult validate(List<DeviceConfig> devices) {
        ValidationResult result = new ValidationResult();
        if (devices == null || devices.isEmpty()) {
            result.addError("No devices configured");
        } else {
            // Validate devices one by one
            devices.forEach(device -> {
                result.addFrom(device.validate());
            });
            // Validate unique names
            devices.stream()
                    .collect(Collectors.groupingBy(DeviceConfig::getName, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() > 1)
                    .forEach(invalidEntry -> result.addError(
                            "Device name %s is not unique", invalidEntry.getKey()));
        }
        return result;
    }

}
