package akka.integration;

import isuib.actors.consumer.Consumer;
import isuib.actors.mapper.MapperImpl;
import isuib.actors.security.SecurityActor;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains actors names.
 * @author NGorelov
 */
public class RegisteredActors {
    private static RegisteredActors instance;

    public static final String ACTOR_SYSTEM = "actorSystem";
    public static final String ACTOR_BUILDER = "actorBuilder";

    public static final String CONSUMER_ACTOR = "consumer";
    public static final String SECURITY_ACTOR = "securityActor";
    public static final String MAPPER_ACTOR = "mapperActor";
    public static final String MAPPER_ACTOR_REF = "mapperActorRef";

    private Map<Class, String> actorsClasses;

    private RegisteredActors(){
        actorsClasses = new HashMap<Class, String>();
        actorsClasses.put(Consumer.class, CONSUMER_ACTOR);
        actorsClasses.put(SecurityActor.class, SECURITY_ACTOR);
        actorsClasses.put(MapperImpl.class, MAPPER_ACTOR);
    };

    public static RegisteredActors getInstance(){
        if(instance == null){
            instance = new RegisteredActors();
        }
        return instance;
    }

    public Map<Class, String> getActorsMap(){
        return actorsClasses;
    }

    public String getActorName(Class actorClass){
        return actorsClasses.get(actorClass);
    }
}
