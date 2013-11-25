package akka.integration;

import org.springframework.context.ApplicationContext;

/**
 * Factory class for creating actors by className and injecting spring beans into them.
 * @author NGorelov
 */
public class DefaultActorFactory extends AbstractActorFactory {
    private final Class<?> actorClass;
    private ApplicationContext applicationContext;

    public DefaultActorFactory(Class<?> actorClass, ApplicationContext applicationContext) throws IllegalArgumentException {
        super(applicationContext);
        this.actorClass = actorClass;
        this.applicationContext = applicationContext;
    }

    @Override
    protected Class<?> getActorClass() {
        return actorClass;
    }

    @Override
    protected Object create() throws InstantiationException, IllegalAccessException {
        return actorClass.newInstance();
    }
}
