package findo.booking.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import findo.booking.dto.UpdateBalanceDTO;
import findo.booking.dto.UserBalanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserClient {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "user-service";

    @Autowired
    public UserClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
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

        return "http://" + hostName + ":" + port + "/api/v1/users";
    }

    public Mono<UserBalanceDTO> getUserBalance(UUID userId, String token) {
        return webClientBuilder.build()
                .get()
                .uri(getServiceUrl() + "/profile", userId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(UserBalanceDTO.class);
    }

    public Mono<Void> updateUserBalance(UUID userId, double newBalance, String token) {
        UpdateBalanceDTO updateBalanceDTO = new UpdateBalanceDTO(newBalance);
        return webClientBuilder.build()
                .put()
                .uri(getServiceUrl() + "/update-balance", userId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .bodyValue(updateBalanceDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
