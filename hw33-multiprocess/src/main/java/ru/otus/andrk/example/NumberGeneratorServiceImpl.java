package ru.otus.andrk.example;

import io.grpc.stub.StreamObserver;
import ru.otus.andrk.protobuf.generated.Empty;
import ru.otus.andrk.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.andrk.protobuf.generated.UserMessage;

import java.util.List;

import static ru.otus.andrk.example.Mapper.user2UserMessage;

public class NumberGeneratorServiceImpl extends NumberGeneratorServiceGrpc.NumberGeneratorServiceImplBase {

    private final DBService dbService;

    public NumberGeneratorServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void saveUser(UserMessage request, StreamObserver<UserMessage> responseObserver) {
        User user = dbService.saveUser(request.getFirstName(), request.getLastName());
        responseObserver.onNext(user2UserMessage(user));
        responseObserver.onCompleted();
    }

    @Override
    public void findAllUsers(Empty request, StreamObserver<UserMessage> responseObserver) {
        List<User> allUsers = dbService.findAllUsers();
        allUsers.forEach(u -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(user2UserMessage(u));
        });
        responseObserver.onCompleted();
    }


}
