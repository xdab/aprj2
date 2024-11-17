package pl.so5dz.aprj2.config;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.config.validation.Validable;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.impl.PluginValidator;

@Getter
@ToString
public class PluginConfig implements Validable {
    private String id;
    private Map<String, Object> params;

    @Override
    public ValidationResult validate() {
        return new PluginValidator().validate(this);
    }
}
