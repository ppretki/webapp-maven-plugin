package pl.com.itsense.maven.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author ppretki
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Image
{
    /** */
    String path();
    
    /** */
    boolean css() default false;

    /** */
    boolean sprite() default true;
}
