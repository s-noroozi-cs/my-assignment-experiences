package com.backbase.model.service;

import com.backbase.model.confg.ConnectionManagerConfig;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import java.util.concurrent.TimeUnit;

@Component
public class HttpConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);

    private ConnectionManagerConfig connectionConfig;
    private final PoolingHttpClientConnectionManager poolingConnManager;
    private final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final CloseableHttpClient closeableHttpClient;

    @Autowired
    public HttpConnectionManager(ConnectionManagerConfig connectionConfig) {
        this.connectionConfig = connectionConfig;

        poolingConnManager = new PoolingHttpClientConnectionManager(getSocketFactoryRegistry(connectionConfig));
        poolingConnManager.setMaxTotal(connectionConfig.getMaxPoolSize());
        poolingConnManager.setDefaultMaxPerRoute(connectionConfig.getMaxPoolSize());

        IdleConnectionMonitor staleMonitor = new IdleConnectionMonitor(poolingConnManager, connectionConfig);
        staleMonitor.start();

        keepAliveStrategy = makeKeepAliveStrategy();
        closeableHttpClient = makeCloseableHttpClient(poolingConnManager, keepAliveStrategy);
    }

    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    private CloseableHttpClient makeCloseableHttpClient(HttpClientConnectionManager connectionManager,
                                                        ConnectionKeepAliveStrategy keepAliveStrategy) {
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(connectionConfig.getConnectionTimeout()). //network connection
                setConnectionRequestTimeout(connectionConfig.getConnectionRequestTimeout()). //get from connection pool
                setSocketTimeout(connectionConfig.getSocketTimeout()).build(); // read from target

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(keepAliveStrategy)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    private ConnectionKeepAliveStrategy makeKeepAliveStrategy() {
        return (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator
                    (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase
                        ("timeout")) {
                    return Long.parseLong(value) * 1000;
                }
            }
            return connectionConfig.getDefaultKeepAlive();
        };
    }

    private Registry<ConnectionSocketFactory> getSocketFactoryRegistry(ConnectionManagerConfig config) {
        try {
            SSLContextBuilder sslContextBuilder = SSLContexts.custom();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContextBuilder.build(), hostnameVerifier);

            return
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("https", sslConnectionSocketFactory)
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .build();

        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private class IdleConnectionMonitor extends Thread {
        final Logger logger = LoggerFactory.getLogger(IdleConnectionMonitor.class);

        private final HttpClientConnectionManager connMgr;
        private ConnectionManagerConfig configuration;


        public IdleConnectionMonitor(PoolingHttpClientConnectionManager connMgr,
                                     ConnectionManagerConfig configuration) {
            this.connMgr = connMgr;
            this.configuration = configuration;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    try {
                        wait(configuration.getConnectionMonitoringInterval());
                        connMgr.closeExpiredConnections();
                        connMgr.closeIdleConnections(configuration.getCloseIdleConnections(), TimeUnit.MILLISECONDS);
                    } catch (Throwable ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        }
    }
}
