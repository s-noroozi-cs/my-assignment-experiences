package com.backbase.model.confg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "http.connection.manager")
public class ConnectionManagerConfig {
    private int maxPoolSize;
    private long defaultKeepAlive;
    private int connectionTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;
    private long connectionMonitoringInterval;
    private long closeIdleConnections;

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getDefaultKeepAlive() {
        return defaultKeepAlive;
    }

    public void setDefaultKeepAlive(long defaultKeepAlive) {
        this.defaultKeepAlive = defaultKeepAlive;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public long getConnectionMonitoringInterval() {
        return connectionMonitoringInterval;
    }

    public void setConnectionMonitoringInterval(long connectionMonitoringInterval) {
        this.connectionMonitoringInterval = connectionMonitoringInterval;
    }

    public long getCloseIdleConnections() {
        return closeIdleConnections;
    }

    public void setCloseIdleConnections(long closeIdleConnections) {
        this.closeIdleConnections = closeIdleConnections;
    }
}
