package pl.so5dz.aprj2.config.validation.validator.impl;

import pl.so5dz.aprj2.config.PluginConfig;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.Validator;

public class PluginValidator implements Validator<PluginConfig> {

    @Override
    public ValidationResult validate(PluginConfig object) {
        ValidationResult result = new ValidationResult();
        if (object.getId() == null || object.getId().isBlank()) {
            result.addError("Plugin ID is missing");
        }
        if (object.getParams() == null || object.getParams().isEmpty()) {
            result.addWarning("Plugin parameters are missing, no need to specify it in the configuration");
        }
        return result;
    }

}
