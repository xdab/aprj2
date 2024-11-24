package pl.so5dz.aprj2.config.manager;

import java.io.File;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.Aprj2Context;
import pl.so5dz.aprj2.config.Aprj2Config;
import pl.so5dz.aprj2.config.validation.result.ValidationResult;

@Slf4j
public class Aprj2ConfigManager {
    private static final Aprj2Context context = Aprj2Context.getInstance();

    private Aprj2ConfigManager() {
    }

    private static final XmlMapper xmlMapper;
    static {
        xmlMapper = new XmlMapper();
        xmlMapper.findAndRegisterModules();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    public static Aprj2Config loadConfig(String configFilePathStr) throws Exception {
        log.info("Loading configuration from {}", configFilePathStr);
        Aprj2Config config = xmlMapper.readValue(new File(configFilePathStr), Aprj2Config.class);
        validateConfig(config);
        context.setConfig(config);
        return config;
    }

    private static void validateConfig(Aprj2Config config) {
        ValidationResult validationResult = config.validate();
        if (validationResult.hasWarning()) {
            log.warn("Configuration validation completed with warnings:");
            validationResult.getIssues().stream().filter(i -> i.isWarning()).forEach(i -> log.warn("  {}", i));
        }
        if (validationResult.hasError()) {
            log.error("Configuration validation failed with errors:");
            validationResult.getIssues().stream().filter(i -> i.isError()).forEach(i -> log.error("  {}", i));
            System.exit(1);
        }
        log.debug("Configuration validated successfully");
    }
}
