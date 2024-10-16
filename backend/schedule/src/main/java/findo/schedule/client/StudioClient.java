package findo.schedule.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import findo.schedule.dto.AllSeatStudioDTO;
import findo.schedule.dto.StudioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StudioClient {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "studio-service";

    @Autowired
    public StudioClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
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

        return "http://" + hostName + ":" + port + "/api/v1/studios/";
    }

    public Mono<StudioDTO> getStudioById(Integer integer, String token) {
        String url = getServiceUrl() + integer;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(StudioDTO.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }

    public Mono<AllSeatStudioDTO> getSeatsByStudioId(Integer integer, String token) {
        String url = getServiceUrl() + integer + "/seats";

        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(AllSeatStudioDTO.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }
}
