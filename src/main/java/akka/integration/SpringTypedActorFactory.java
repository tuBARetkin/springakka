package akka.integration;

import akka.japi.Creator;
import org.springframework.context.ApplicationContext;

/**
 * Spring integrated typed actor factory. Works with DefaultActorFactory or SpecificActorFactory
 * @see DefaultActorFactory
 * @see SpecificActorFactory
 * @author NGorelov
 */
public class SpringTypedActorFactory implements Creator {
    private DependencyInjectionFactory dependencyInjectionFactory;
    private ApplicationContext applicationContext;

    public SpringTypedActorFactory(ApplicationContext applicationContext, Class actorClass) throws IllegalArgumentException {
        this.dependencyInjectionFactory = new DefaultActorFactory(actorClass, applicationContext);
        this.applicationContext = applicationContext;
    }

    public SpringTypedActorFactory(ApplicationContext applicationContext, Creator customFactory) throws IllegalArgumentException {
        this.dependencyInjectionFactory = new SpecificActorFactory(customFactory, applicationContext);
        this.applicationContext = applicationContext;
    }

    public Object create() {
        return dependencyInjectionFactory.createAndInject();
    }
}
