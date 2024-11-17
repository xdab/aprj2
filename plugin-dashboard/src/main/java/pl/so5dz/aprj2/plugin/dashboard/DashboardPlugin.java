package pl.so5dz.aprj2.plugin.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.google.auto.service.AutoService;

import pl.so5dz.aprj2.plugin.DeviceInfo;
import pl.so5dz.aprj2.plugin.Plugin;
import pl.so5dz.aprj2.plugin.dashboard.spring.DashboardApplication;

@AutoService(Plugin.class)
public class DashboardPlugin implements Plugin {
    private ApplicationContext context;

    @Override
    public void run() {
        context = SpringApplication.run(DashboardApplication.class);
    }

    @Override
    public void stop() {
        if (context != null) {
            SpringApplication.exit(context);
        }
    }

    @Override
    public String id() {
        return "aprj2-dashboard-plugin";
    }

    @Override
    public String version() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String description() {
        return "Default dashboard plugin bundled with APRJ2";
    }

    @Override
    public void initialize(List<DeviceInfo> devices, Map<String, Object> parameters) {
        return;
    }

}
