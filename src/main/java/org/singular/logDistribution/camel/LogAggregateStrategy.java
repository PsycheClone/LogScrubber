package org.singular.logDistribution.camel;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class LogAggregateStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange newExchange, Exchange oldExchange) {
        if(newExchange == null) {
            return oldExchange;
        }
        String first = oldExchange.getIn().getBody(String.class);
        String second = newExchange.getIn().getBody(String.class);

        newExchange.getIn().setBody(first + "\n" + second);
        return newExchange;
    }
}
