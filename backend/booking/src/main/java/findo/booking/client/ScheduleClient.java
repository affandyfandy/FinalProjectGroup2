package findo.booking.client;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import findo.booking.dto.ScheduleMovieDTO;
import reactor.core.publisher.Mono;

@Service
public class ScheduleClient {
    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "schedule-service";

    @Autowired
    public ScheduleClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
        this.eurekaClient = eurekaClient;
        this.webClientBuilder = webClientBuilder;
    }

    private String getServiceUrl() {
        InstanceInfo service = eurekaClient
                .getApplication(SERVICE_NAME)
                .getInstances()
                .get(0);

        String hostName = service.getHostName();
        int port = service.getPort();

        return "http://" + hostName + ":" + port + "/api/v1/schedules/";
    }

    public Mono<ScheduleMovieDTO> getScheduleById(UUID scheduleId, String token) {
        String url = getServiceUrl() + scheduleId;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(ScheduleMovieDTO.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }
}
