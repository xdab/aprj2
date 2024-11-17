package pl.so5dz.aprj2.plugin.beacon.config;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BeaconPluginConfig {

    @JacksonXmlProperty(localName = "beacon")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<BeaconConfig> beacons;

}
