package akka.integration;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.springframework.context.ApplicationContext;

/**
 * Akka props extension for creating spring configured untyped actors
 * @author NGorelov
 */
public class DIProps extends Props {
    public DIProps(ApplicationContext applicationContext, Class<? extends UntypedActor> actorClass) throws IllegalArgumentException {
        super(new SpringUntypedActorFactory(applicationContext, actorClass));
    }

    public DIProps(ApplicationContext applicationContext, Creator factory) throws IllegalArgumentException {
        super(new SpringUntypedActorFactory(applicationContext, factory));
    }
}
