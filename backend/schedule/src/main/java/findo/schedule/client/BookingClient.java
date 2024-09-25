package findo.schedule.client;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import findo.schedule.dto.BookingSeatsDTO;
import reactor.core.publisher.Mono;

@Service
public class BookingClient {
    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "booking-service";

    @Autowired
    public BookingClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
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

        return "http://" + hostName + ":" + port + "/api/v1/bookings/";
    }

    public Mono<BookingSeatsDTO> getSeatIdsByScheduleId(UUID scheduleId, String token) {
        // Build the URL for the request
        String url = getServiceUrl() + scheduleId + "/seats-ids";

        // Make the HTTP GET request
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(BookingSeatsDTO.class)
                .onErrorResume(ex -> {
                    return Mono.empty();
                });

    }
}