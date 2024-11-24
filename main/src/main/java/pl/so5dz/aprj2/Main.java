package pl.so5dz.aprj2;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.config.Aprj2Config;
import pl.so5dz.aprj2.config.manager.Aprj2ConfigManager;

@Slf4j
public class Main {
    private static final String DEFAULT_CONFIG_FILE_PATH = "./config.xml";

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setName("main");

        String configFilePathStr = DEFAULT_CONFIG_FILE_PATH;
        if (args.length > 0) {
            configFilePathStr = args[0];
        }
        Aprj2Config config = Aprj2ConfigManager.loadConfig(configFilePathStr);
        new Aprj2(config).run();

        log.info("Exiting");
    }

}
