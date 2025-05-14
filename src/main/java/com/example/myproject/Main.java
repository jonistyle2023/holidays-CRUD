package com.example.myproject;

import io.helidon.config.Config;
import io.helidon.health.HealthCheckResponse;
import io.helidon.health.checks.HealthChecks;
import io.helidon.http.media.jsonb.JsonbSupport;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.observe.health.HealthObserver;
import io.helidon.webserver.observe.metrics.MetricsObserver;
import io.helidon.webserver.observe.ObserveFeature;
import jakarta.json.bind.JsonbConfig;

/**
 * The application main class.
 **/

public class Main {

    /**
     * Cannot be instantiated.
     **/
    private Main() {}

    /**
     * Application main entry point.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // Configuración de logging
        LogConfig.configureRuntime();

        // Cargar configuración
        Config config = Config.create();

        // JSON-B, supuestamente para mejorar la legibilidad del health, pero no funciona
        JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true);

        // observabilidad
        ObserveFeature observeFeature = ObserveFeature.builder()
                .addObserver(HealthObserver.builder()
                        .addCheck(() -> HealthCheckResponse.builder()
                                .status(true)
                                .detail("service", "running")
                                .detail("version", "1.0.0")
                                .build())
                        .addChecks(HealthChecks.healthChecks())
                        .details(true)
                        .build())
                .addObserver(MetricsObserver.create())
                .build();

        WebServer server = WebServer.builder()
                .config(config.get("server"))
                .mediaContext(media -> media.addMediaSupport(JsonbSupport.create(config)))
                .addFeature(observeFeature)
                .routing(routing -> {
                    // Configurar rutas de aplicación
                    configureAppRoutes(routing);
                })
                .build()
                .start();

        printServerInfo(server);
    }

    /**
     * Updates HTTP Routing.
     **/
    // Aquí batallé para poder declarar la ruta correcta, descubrí que ya no se usa addMediaSupport aquí,
    // sino que se usa el metodo register, la IA no ayuda mucho para estos casos, ya que da código desactualizado e
    // incompleto, al menos me ayudo a entender la lógica de lo que se quería hacer, pero lo más recomendable es leer
    // la documentación oficial y actualizada de helidon.

    public static void configureAppRoutes(HttpRouting.Builder routing) {
        routing.register("/feriados", new FeriadoService());
    }

    // Mensajes de inicio del servidor
    private static void printServerInfo(WebServer server) {
        System.out.println("Servidor ya esta en ejecución: http://localhost:" + server.port());
        System.out.println("Health checks:  http://localhost:" + server.port() + "/observe/health");
        System.out.println("Métricas: http://localhost:" + server.port() + "/observe/metrics");
        System.out.println("Ready check: http://localhost:" + server.port() + "/observe/health/ready");
        System.out.println("Live check: http://localhost:" + server.port() + "/observe/health/live");
    }
}