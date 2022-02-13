package com.house.auction.client;

import com.house.auction.client.proto.CommandServiceGrpc;
import com.house.auction.client.proto.Request;
import com.house.auction.client.proto.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    private static final String HOST = "localhost";
    private static final int PORT = 9000;
    private static final Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ManagedChannel channelBuilder = ManagedChannelBuilder
                .forAddress(HOST, PORT)
                .usePlaintext()
                .build();

        CommandServiceGrpc.CommandServiceBlockingStub stub =
                CommandServiceGrpc.newBlockingStub(channelBuilder);

        UUID uuid = UUID.randomUUID();
        String authToken = uuid.toString();

        String[] params = {"user_a", "password123"};

        Request cmd = Request.newBuilder()
                .setCommandName("register")
                .setAuthToken(authToken)
                .addAllParams(Arrays.asList(params))
                .build();

        Response response = stub.execute(cmd);

        logger.info(String.format("\nstatus: %s\ncontent: %s", response.getStatus(), response.getContent()));
    }
}
