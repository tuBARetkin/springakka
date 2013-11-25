package akka.integration;

/**
 * Interface for all spring based actor factories
 * @author NGorelov
 */
public interface DependencyInjectionFactory {
    /**
     * Create instance of actor and inject spring beans into it.
     * @return UntypedActor
    */
    Object createAndInject();
}
