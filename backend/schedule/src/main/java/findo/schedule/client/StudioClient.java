package findo.schedule.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import findo.schedule.dto.StudioDTO;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

        return "http://" + hostName + ":" + port + "/api/v1/studios";
    }

    public Mono<StudioDTO> getStudioById(List<Integer> list, String token) {
        return webClientBuilder.build()
                .get()
                .uri(getServiceUrl() + "/" + list)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(StudioDTO.class);
    }
}
