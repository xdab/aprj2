package pl.so5dz.aprj2.config.validation.validator.impl;

import java.util.List;
import java.util.stream.Collectors;

import pl.so5dz.aprj2.config.PluginConfig;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.Validator;

public class PluginsValidator implements Validator<List<PluginConfig>> {

    @Override
    public ValidationResult validate(List<PluginConfig> pluginConfigs) {
        ValidationResult result = new ValidationResult();
        if (pluginConfigs != null) {
            // Validate plugins one by one
            pluginConfigs.forEach(pluginConfig -> {
                result.addFrom(pluginConfig.validate());
            });
            // Validate unique IDs
            pluginConfigs.stream()
                    .collect(Collectors.groupingBy(PluginConfig::getId, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() > 1)
                    .forEach(invalidEntry -> result.addError(
                            "Plugin ID %s is specified multiple times", invalidEntry.getKey()));
        }
        return result;
    }

}
