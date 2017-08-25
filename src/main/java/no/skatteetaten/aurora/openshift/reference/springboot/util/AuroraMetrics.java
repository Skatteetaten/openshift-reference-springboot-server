package no.skatteetaten.aurora.openshift.reference.springboot.util;

import static io.micrometer.core.instrument.stats.hist.CumulativeHistogram.linear;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.stats.hist.CumulativeHistogram;

//@Component
public class AuroraMetrics implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {

        meterRegistry.summaryBuilder("operations")
            .histogram(CumulativeHistogram.buckets(linear(0, 10, 20), TimeUnit.SECONDS))
            .description("Manual operation that we want statistics on")
            .create();

    }
}
