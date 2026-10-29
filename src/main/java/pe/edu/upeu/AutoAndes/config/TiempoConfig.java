package pe.edu.upeu.AutoAndes.config;

import java.time.*;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class TiempoConfig {
    @Bean
    Clock reloj(@Value("${app.zone:America/Lima}") String zone) {
        return Clock.system(ZoneId.of(zone));
    }
}
