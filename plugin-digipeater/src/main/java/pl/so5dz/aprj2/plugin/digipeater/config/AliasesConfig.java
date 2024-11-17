package pl.so5dz.aprj2.plugin.digipeater.config;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AliasesConfig {
    private List<String> traced;
    private List<String> untraced;
}
