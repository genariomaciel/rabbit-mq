package com.leicam.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitProperties {

    private Exchanges exchanges;
    private Queues queues;
    private RoutingKeys routingKeys;

    public static class Exchanges {
        private String defaultExchange;
        private String fanout;
        private String topic;

        public String getDefaultExchange() {
            return defaultExchange;
        }

        public void setDefaultExchange(String defaultExchange) {
            this.defaultExchange = defaultExchange;
        }

        public String getFanout() {
            return fanout;
        }

        public void setFanout(String fanout) {
            this.fanout = fanout;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    public static class Queues {
        private String defaultQueue;
        private String fanout1;
        private String fanout2;
        private String topicOrders;
        private String topicNotifications;

        public String getDefaultQueue() {
            return defaultQueue;
        }

        public void setDefaultQueue(String defaultQueue) {
            this.defaultQueue = defaultQueue;
        }

        public String getFanout1() {
            return fanout1;
        }

        public void setFanout1(String fanout1) {
            this.fanout1 = fanout1;
        }

        public String getFanout2() {
            return fanout2;
        }

        public void setFanout2(String fanout2) {
            this.fanout2 = fanout2;
        }

        public String getTopicOrders() {
            return topicOrders;
        }

        public void setTopicOrders(String topicOrders) {
            this.topicOrders = topicOrders;
        }

        public String getTopicNotifications() {
            return topicNotifications;
        }

        public void setTopicNotifications(String topicNotifications) {
            this.topicNotifications = topicNotifications;
        }
    }

    public static class RoutingKeys {
        private String topicOrders;
        private String topicNotifications;

        public String getTopicOrders() {
            return topicOrders;
        }

        public void setTopicOrders(String topicOrders) {
            this.topicOrders = topicOrders;
        }

        public String getTopicNotifications() {
            return topicNotifications;
        }

        public void setTopicNotifications(String topicNotifications) {
            this.topicNotifications = topicNotifications;
        }
    }

    public Exchanges getExchanges() {
        return exchanges;
    }

    public void setExchanges(Exchanges exchanges) {
        this.exchanges = exchanges;
    }

    public Queues getQueues() {
        return queues;
    }

    public void setQueues(Queues queues) {
        this.queues = queues;
    }

    public RoutingKeys getRoutingKeys() {
        return routingKeys;
    }

    public void setRoutingKeys(RoutingKeys routingKeys) {
        this.routingKeys = routingKeys;
    }
}
