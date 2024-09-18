package findo.user.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    private static final Logger logger = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.debug("No authentication found");
            return Optional.of("System");
        }
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String userId = jwtToken.getToken().getClaimAsString("sub");
            logger.debug("Authenticated user ID: " + userId);
            return Optional.ofNullable(userId);
        } else {
            logger.debug("Authentication is not of type JwtAuthenticationToken");
            return Optional.of(authentication.getName());
        }
    }

}