package pl.so5dz.aprj2.config;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.config.validation.Validable;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.impl.SerialValidator;

@Getter
@ToString
public class SerialConfig implements Validable {
    private String port;
    private int baud;

    @Override
    public ValidationResult validate() {
        return new SerialValidator().validate(this);
    }
}
