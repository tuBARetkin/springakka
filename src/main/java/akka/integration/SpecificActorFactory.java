package akka.integration;

import akka.japi.Creator;
import org.springframework.context.ApplicationContext;

/**
 * Factory class for creating actors using other factory of {@link akka.actor.UntypedActorFactory}
 * @author NGorelov
 */
public class SpecificActorFactory extends AbstractActorFactory {
    private Creator specificFactory;
    private volatile Class<?> actorClass;
    private ApplicationContext applicationContext;

    public SpecificActorFactory(Creator specificFactory, ApplicationContext applicationContext) throws IllegalArgumentException {
        super(applicationContext);
        this.specificFactory = specificFactory;
        this.applicationContext = applicationContext;
    }

    @Override
    protected Class<?> getActorClass() {
        return actorClass;
    }

    @Override
    protected Object create() throws Exception {
        Object actor = specificFactory.create();
        actorClass = actor.getClass();
        return actor;
    }
}
