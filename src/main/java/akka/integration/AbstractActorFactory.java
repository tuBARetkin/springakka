package akka.integration;

import com.google.common.base.Preconditions;
import org.fest.reflect.util.Accessibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Abstract actor factory, that provides injection of spring beans and other actors into created actor
 * @author NGorelov
 */
public abstract class AbstractActorFactory implements DependencyInjectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(AbstractActorFactory.class);
    private static final String ERROR_STRING = "Unable to create actor instance";

    private ApplicationContext applicationContext;

    protected AbstractActorFactory(ApplicationContext applicationContext) throws IllegalArgumentException {
        if(applicationContext == null){
            IllegalArgumentException ex = new IllegalArgumentException();
            logger.error("Application context is null. Cannot initialize actor factory", ex);
            throw ex;
        }
        this.applicationContext = applicationContext;
    }

    /**
     * Create instance of actor and inject spring beans into it.
     * Injected field detected if it was marked by spring {@link org.springframework.beans.factory.annotation.Autowired}
     * annotation or by JSR-330 {@link javax.inject.Inject} annotation. You can use 3 types of injection: by
     * {@link org.springframework.beans.factory.annotation.Qualifier}, by field name and by class
     * @return UntypedActor
     */
    @Override
    public Object createAndInject(){
        Preconditions.checkNotNull(applicationContext, "applicationContext");
        Object actor = null;
        try {
            actor = create();
            Class<?> aClass = getActorClass();

            for (Field field : aClass.getDeclaredFields()) {
                if (field.getAnnotation(Autowired.class) != null || field.getAnnotation(Inject.class) != null ) {
                    injectIntoField(field, actor);
                }
            }
        }
        catch (Exception ex) {
            logger.error(ERROR_STRING, ex);
        }

        return actor;
    }

    private void injectIntoField(Field field, Object actor){
        boolean accessible = field.isAccessible();

        try {
            Accessibles.setAccessible(field, true);

            Object value = getValueByQualifier(field);
            if(value == null){
                value = getValueByFieldName(field);
                if(value == null){
                    value = getValueByClass(field);
                }
            }

            if(value != null){
                field.set(actor, value);
            }
            else{
                logger.warn("Injection into actor failed", new NoSuchBeanDefinitionException(field.getName() + " of " + field.getType()));
            }

        }
        catch (IllegalAccessException ex) {
            logger.error(ERROR_STRING, ex);
        }
        finally {
            Accessibles.setAccessibleIgnoringExceptions(field, accessible);
        }
    }

    private Object getValueByQualifier(Field field){
        Object value = null;

        Annotation annotation = field.getAnnotation(Qualifier.class);
        String name = (String) AnnotationUtils.getValue(annotation);

        if(name != null && !name.isEmpty()){
            try{
                value = applicationContext.getBean(name);
            }
            catch(NoSuchBeanDefinitionException ex){
                logger.warn("Searching bean by qualifier failed", ex);
            }
        }

        return value;
    }

    private Object getValueByFieldName(Field field){
        Object value = null;
        String name = field.getName();

        if(name != null && !name.isEmpty()){
            try{
                value = applicationContext.getBean(name);
            }
            catch(NoSuchBeanDefinitionException ex){
                logger.warn("Searching bean by name failed", ex);
            }
        }

        return value;
    }

    private Object getValueByClass(Field field){
        Object value = null;
        Class name = field.getType();

        if(name != null){
            try{
                value = applicationContext.getBean(name);
            }
            catch(NoSuchBeanDefinitionException ex){
                logger.warn("Searching bean by class failed", ex);
            }
        }

        return value;
    }

    /**
     * @return Class of created actor
     */
    protected abstract Class<?> getActorClass();

    /**
     * @return new instance of UntypedActor
     * @throws Exception
     */
    protected abstract Object create() throws Exception;
}
