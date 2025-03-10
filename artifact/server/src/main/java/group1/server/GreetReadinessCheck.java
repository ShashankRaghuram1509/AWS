package group1.server;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class GreetReadinessCheck implements HealthCheck {
    private AtomicLong readyTime = new AtomicLong(0);


    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("CustomReadinessCheck")
                .state(isReady())
                .withData("time", readyTime.get())
                .build();
    }

    public void onStartUp(
            @Observes @Initialized(ApplicationScoped.class) Object init) {
        readyTime = new AtomicLong(System.currentTimeMillis());
    }

    /**
     * Become ready after 5 seconds
     *
     * @return true if application ready
     */
    private boolean isReady() {
        return Duration.ofMillis(System.currentTimeMillis() - readyTime.get()).getSeconds() >= 5;
    }
}
