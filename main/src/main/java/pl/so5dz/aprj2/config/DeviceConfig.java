package pl.so5dz.aprj2.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.config.validation.Validable;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.impl.DeviceValidator;

@Getter
@ToString
public class DeviceConfig implements Validable {
    private String name;
    private ProtocolConfig protocol;
    private TcpConfig tcp;
    private SerialConfig serial;
    private boolean tx;
    private boolean rx;

    @JacksonXmlProperty(localName = "init")
    private String initCommand;

    @Override
    public ValidationResult validate() {
        return new DeviceValidator().validate(this);
    }

    public boolean isTcp() {
        return tcp != null;
    }

    public boolean isSerial() {
        return serial != null;
    }
}
