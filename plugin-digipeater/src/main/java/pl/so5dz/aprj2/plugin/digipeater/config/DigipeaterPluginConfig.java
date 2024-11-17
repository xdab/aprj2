package pl.so5dz.aprj2.plugin.digipeater.config;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JacksonXmlRootElement(localName = "parameters")
public class DigipeaterPluginConfig {

    @JacksonXmlProperty(localName = "digipeater")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DigipeaterConfig> digipeaters;

}
