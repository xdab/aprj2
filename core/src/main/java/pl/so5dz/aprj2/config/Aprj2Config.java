package pl.so5dz.aprj2.config;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.config.validation.Validable;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;
import pl.so5dz.aprj2.config.validation.validator.impl.DevicesValidator;
import pl.so5dz.aprj2.config.validation.validator.impl.PluginsValidator;

@Getter
@ToString
@JacksonXmlRootElement(localName = "config")
public class Aprj2Config implements Validable {

    @JacksonXmlProperty(localName = "device")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DeviceConfig> devices;

    @JacksonXmlProperty(localName = "plugin")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<PluginConfig> plugins;

    @Override
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();
        result.addFrom(new DevicesValidator().validate(devices));
        result.addFrom(new PluginsValidator().validate(plugins));
        return result;
    }
}
