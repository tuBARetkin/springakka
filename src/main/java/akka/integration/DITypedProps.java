package akka.integration;

import akka.actor.TypedProps;
import org.springframework.context.ApplicationContext;

/**
 * Akka props extension for creating spring configured typed actors
 * @author NGorelov
 */
public class DITypedProps extends TypedProps {
    public DITypedProps(ApplicationContext applicationContext, Class<?> actorClass) {
        super(actorClass, new SpringTypedActorFactory(applicationContext, actorClass));
    }
}
