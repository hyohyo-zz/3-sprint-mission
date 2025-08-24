package com.sprint.mission.discodeit.security.jwt.event;

import com.sprint.mission.discodeit.security.jwt.store.JwtRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserAuthoritiesUpdatedListener {

    private final JwtRegistry jwtRegistry;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAuthoritiesUpdatedEvent(UserAuthoritiesUpdatedEvent event) {
        jwtRegistry.invalidateJwtInformationByUserId(event.userId());
    }

}
