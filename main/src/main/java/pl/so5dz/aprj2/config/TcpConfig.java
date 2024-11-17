package pl.so5dz.aprj2.config;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.config.validation.Validable;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.impl.TcpValidator;

@Getter
@ToString
public class TcpConfig implements Validable {
    private String host;
    private int port;

    @Override
    public ValidationResult validate() {
        return new TcpValidator().validate(this);
    }
}
