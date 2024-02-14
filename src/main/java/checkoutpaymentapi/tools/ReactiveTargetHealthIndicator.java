package checkoutpaymentapi.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReactiveTargetHealthIndicator implements ReactiveHealthIndicator {

    private final ApplicationContext context;

    private String overallStatus="UP";

    public ReactiveTargetHealthIndicator(@NonNull ApplicationContext context) {
        this.context = context;
    }

    @Override
    public Mono<Health> health() {
        return checkTargetServiceHealth().onErrorResume(
                ex -> Mono.just(new Health.Builder().down(ex).build())
        );
    }

    private Mono<Health> checkTargetServiceHealth() {
        var target = this.context;
        Map<String, Mono<Map<String, String>>> targetServiceBeans= new LinkedHashMap<>();
        while (target != null) {
            target.getBeansOfType(WebClient.class)
                    .forEach((name, webclient) -> targetServiceBeans.put(name, createReport(name,webclient)));
            target = target.getParent();
        }
        log.info("Reactive Webclient Beans [{}]", targetServiceBeans); // Bean is empty
        var heathBuilder = new Health.Builder().withDetails(targetServiceBeans);
        return (overallStatus.equals("DOWN") )? Mono.just(heathBuilder.down().build())
                : Mono.just(heathBuilder.up().build());
    }

    private Mono<Map<String, String>> createReport(String name, WebClient webclient) {
        log.info("Reactive Webclient [{}] is triggered", name); // Here name of bean is coming
        return webclient.get()
                .uri("/actuator/health/liveness")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .onErrorResume(e -> {
                    log.error("Could not retrieve reactive health status of : {}", name, e);
                    overallStatus="DOWN";
                    return Mono.just(Map.of("status", "DOWN"));
                });
    }
}
