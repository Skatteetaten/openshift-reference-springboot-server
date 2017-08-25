package no.skatteetaten.aurora.openshift.reference.springboot.util;

import static java.util.Arrays.asList;

import static io.micrometer.core.instrument.stats.hist.CumulativeHistogram.linear;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.stats.hist.CumulativeHistogram;
import io.micrometer.core.instrument.stats.quantile.CKMSQuantiles;
import io.prometheus.client.Histogram;
import io.prometheus.client.SimpleTimer;

@Component
public final class Operation  {

    private static final Logger logger = LoggerFactory.getLogger(Operation.class);
    private MeterRegistry registry;

    public Operation(MeterRegistry registry) {
        this.registry = registry;
    }
    public <T> T withMetrics(String name, Supplier<T> s) {
        return withMetrics(name, "operation", s);
    }

    public  <T> T withMetrics(String name, String type, Supplier<T> s) {
        long startTime = System.currentTimeMillis();

        String result = "success";
        try {
            return s.get();
        } catch (Exception e) {
            result = e.getClass().getSimpleName();
            throw e;
        } finally {
            List<Tag> tags = asList(Tag.of("result", result),
                Tag.of("type", type),
                Tag.of("name", name));

            double time = (System.currentTimeMillis() - startTime);
            double time2=time/1000;
            registry.summaryBuilder("operations")
               .histogram(CumulativeHistogram.buckets(linear(0, 100, 20), TimeUnit.MILLISECONDS))
                .tags(tags)
                .description("Manual operation that we want statistics on")
                .create().record(time2);

        }

    }



}
