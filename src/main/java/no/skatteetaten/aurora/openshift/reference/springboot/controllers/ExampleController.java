package no.skatteetaten.aurora.openshift.reference.springboot.controllers;

import static no.skatteetaten.aurora.AuroraMetrics.StatusValue.CRITICAL;
import static no.skatteetaten.aurora.AuroraMetrics.StatusValue.OK;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import io.micrometer.core.annotation.Timed;
import no.skatteetaten.aurora.AuroraMetrics;

/*
 * An example controller that shows how to do a REST call and how to do an operation with a operations metrics
 * There should be a metric called http_client_requests http_server_requests and operations
 */
@RestController
public class ExampleController {

    private static final String SOMETIMES = "sometimes";
    private static final int SECOND = 1000;
    private RestTemplate restTemplate;
    private AuroraMetrics metrics;

    public ExampleController(RestTemplate restTemplate, AuroraMetrics metrics) {

        this.restTemplate = restTemplate;
        this.metrics = metrics;
    }

    @Timed
    @GetMapping("/api/example/ip")
    public Map<String, Object> ip() {
        JsonNode forEntity = restTemplate.getForObject("http://httpbin.org/ip", JsonNode.class);
        Map<String, Object> response = new HashMap<>();
        response.put("ip", forEntity.get("origin").textValue());
        return response;
    }

    @Timed
    @GetMapping("/api/example/sometimes")
    public Map<String, Object> example() {
        return metrics.withMetrics(SOMETIMES, () -> {
            boolean wasSuccessful = performOperationThatMayFail();
            if (wasSuccessful) {
                metrics.status(SOMETIMES, OK);
                Map<String, Object> response = new HashMap<>();
                response.put("result", "Sometimes I succeed");
                return response;
            } else {
                metrics.status(SOMETIMES, CRITICAL);
                throw new RuntimeException("Sometimes I fail");
            }
        });
    }

    protected boolean performOperationThatMayFail() {

        long sleepTime = (long) (Math.random() * SECOND);

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interupted", e);
        }

        return sleepTime % 2 == 0;
    }
}

