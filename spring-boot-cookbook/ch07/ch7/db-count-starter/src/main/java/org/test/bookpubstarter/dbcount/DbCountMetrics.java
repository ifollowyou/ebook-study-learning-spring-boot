package org.test.bookpubstarter.dbcount;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricSet;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public class DbCountMetrics implements PublicMetrics, MetricSet {
    private Collection<CrudRepository> repositories;

    public DbCountMetrics(Collection<CrudRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public Collection<Metric<?>> metrics() {
        List<Metric<?>> metrics = new LinkedList<>();
        for (CrudRepository repository : repositories) {
            String name = DbCountRunner.getRepositoryName(repository.getClass());
            String metricName = "counter.datasource." + name;
            metrics.add(new Metric(metricName, repository.count()));
        }
        return metrics;
    }

    @Override
    public Map<String, com.codahale.metrics.Metric> getMetrics() {
        final Map<String, com.codahale.metrics.Metric> gauges = new HashMap<String, com.codahale.metrics.Metric>();
        for (Metric springMetric : metrics()) {
            gauges.put(springMetric.getName(), (Gauge<Number>) springMetric::getValue);
        }
        return gauges;
    }
}
