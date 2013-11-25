package akka.integration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import isuib.actors.consumer.Consumer;
import isuib.actors.mapper.MapperImpl;
import isuib.model.domain.*;
import isuib.others.springintegration.ActorBuilder;
import isuib.others.springintegration.RegisteredActors;
import isuib.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.util.Random;

@Component
public class Bootstrap{

    private ActorBuilder actorBuilder;

    @Bean(name = RegisteredActors.ACTOR_SYSTEM, destroyMethod = "shutdown")
    public ActorSystem actorSystem() {
        return ActorSystem.create(RegisteredActors.ACTOR_SYSTEM);
    }

    @Bean(name = RegisteredActors.ACTOR_BUILDER)
    public ActorBuilder actorBuilder() {
        actorBuilder = new ActorBuilder();
        return actorBuilder;
    }

    @Bean(name = RegisteredActors.CONSUMER_ACTOR)
    @DependsOn({RegisteredActors.ACTOR_SYSTEM})
    public ActorRef consumerActor() {
        return actorBuilder.initUntypedActor(Consumer.class);
    }

    @Bean(name = RegisteredActors.MAPPER_ACTOR)
    @DependsOn({RegisteredActors.ACTOR_SYSTEM})
    public Mapper mapperActor(){
        return (Mapper) actorBuilder.initTypedActor(MapperImpl.class);
    }

    @Bean(name = RegisteredActors.MAPPER_ACTOR_REF)
    @DependsOn({RegisteredActors.ACTOR_SYSTEM})
    public ActorRef mapperActorRef(){
        return actorBuilder.initUntypedActor(MapperImpl.class);
    }
}
