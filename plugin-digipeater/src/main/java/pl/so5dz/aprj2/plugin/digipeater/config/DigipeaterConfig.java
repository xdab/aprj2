package pl.so5dz.aprj2.plugin.digipeater.config;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DigipeaterConfig {
    private String callsign;
    private String target;
    @JacksonXmlProperty(localName = "source")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> sources;
    private AliasesConfig aliases;
}
