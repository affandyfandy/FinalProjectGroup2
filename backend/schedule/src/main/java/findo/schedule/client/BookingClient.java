package findo.schedule.client;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import reactor.core.publisher.Mono;

@Service
public class BookingClient {
    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "movie-service";

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

    public Mono<List<Integer>> getSeatIdsByScheduleId(UUID scheduleId, String token) {
        // Build the URL for the request
        String url = getServiceUrl() + scheduleId + "/seats-ids";

        // Make the GET request and convert the response to a List<Integer>
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(Integer.class) // Get response body as Flux of Integers
                .collectList(); // Collect into a list
    }
}