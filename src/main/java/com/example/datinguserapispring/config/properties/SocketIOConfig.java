package com.example.datinguserapispring.config.properties;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PreDestroy;

@CrossOrigin
@Component
@Log4j2
public class SocketIOConfig {

    @Value("${socket.host}")
    private String socketHost;
    @Value("${socket.port}")
    private int socketPort;
    private SocketIOServer server;

    @Bean
    SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(socketHost);
        config.setPort(socketPort);

        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);

        server = new SocketIOServer(config);
        server.start();
        server.addConnectListener(client -> log.info("new user connected with socket " + client.getSessionId()));
        server.addDisconnectListener(client -> client.getNamespace()
                .getAllClients().forEach(data -> log.info("user disconnected " + data.getSessionId().toString())));
        return server;
    }

    @PreDestroy
    void stopSocketIOServer() {
        this.server.stop();
    }

    @Bean
    SpringAnnotationScanner springAnnotationScannerSocket(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
