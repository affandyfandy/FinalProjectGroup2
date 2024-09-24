package findo.schedule.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import findo.schedule.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class MovieClient {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "movie-service";

    @Autowired
    public MovieClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
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

        return "http://" + hostName + ":" + port + "/api/v1/movies/";
    }

    public Mono<MovieDTO> getMovieById(UUID movieId, String token) {
        String url = getServiceUrl() + "anonymous/" + movieId;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }
}
