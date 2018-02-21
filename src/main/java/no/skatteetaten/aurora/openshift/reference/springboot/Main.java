package no.skatteetaten.aurora.openshift.reference.springboot;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.micrometer.core.instrument.config.MeterFilter;

@SpringBootApplication
public class Main {

    protected Main() {
    }

    private static int MIN_MILLIS = 300;

    //Example on how to override default 100 milis lower bound for buckets
    @Bean
    MeterFilter minExpectedHttp() {
        return MeterFilter.minExpected("http", Duration.ofMillis(MIN_MILLIS));
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
