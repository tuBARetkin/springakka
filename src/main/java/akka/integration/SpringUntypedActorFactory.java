package akka.integration;

import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.japi.Creator;
import org.springframework.context.ApplicationContext;

/**
 * Spring integrated untyped actor factory. Works with DefaultActorFactory or SpecificActorFactory
 * @see DefaultActorFactory
 * @see SpecificActorFactory
 * @author NGorelov
 */
public class SpringUntypedActorFactory implements UntypedActorFactory {
    private DependencyInjectionFactory dependencyInjectionFactory;
    private ApplicationContext applicationContext;

    public SpringUntypedActorFactory(ApplicationContext applicationContext, Class<?> actorClass) throws IllegalArgumentException {
        this.dependencyInjectionFactory = new DefaultActorFactory(actorClass, applicationContext);
        this.applicationContext = applicationContext;
    }

    public SpringUntypedActorFactory(ApplicationContext applicationContext, Creator customFactory) throws IllegalArgumentException {
        this.dependencyInjectionFactory = new SpecificActorFactory(customFactory, applicationContext);
        this.applicationContext = applicationContext;
    }

    public UntypedActor create() {
        return (UntypedActor) dependencyInjectionFactory.createAndInject();
    }
}
