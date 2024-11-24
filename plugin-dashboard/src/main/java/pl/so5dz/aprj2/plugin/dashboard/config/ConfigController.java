package pl.so5dz.aprj2.plugin.dashboard.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;

    @GetMapping
    public ConfigDto getConfig() {
        return ConfigDto.builder()
                .config(configService.getConfig())
                .build();
    }
}
