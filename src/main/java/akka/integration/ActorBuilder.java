package akka.integration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.routing.RoundRobinRouter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.inject.Inject;

/**
 * Contains methods for initializing Untyped and Typed actors
 * @author NGorelov
 */
public class ActorBuilder implements ApplicationContextAware{
    private ApplicationContext applicationContext;
    private final int availableProcessors;

    @Inject
    private ActorSystem actorSystem;

    public ActorBuilder(){
        availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public ActorRef initUntypedActor(Class actorClass){
        return actorSystem.actorOf(new DIProps(applicationContext, actorClass)
                .withRouter(new RoundRobinRouter(availableProcessors)), TypedActor.newInstance()RegisteredActors.getInstance().getActorsMap().get(actorClass));
    }

    public Object initTypedActor(Class actorClass){
        return TypedActor.get(actorSystem).typedActorOf(new DITypedProps(applicationContext, actorClass));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
