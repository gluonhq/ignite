package com.gluonhq.ignite.spring;

import com.gluonhq.ignite.DIContext;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of dependency injection context for Spring Framework
 */
public class SpringContext implements DIContext {

    private final Object contextRoot;
    private AnnotationConfigApplicationContext appContext;

    private Supplier<Collection<String>> scanPackages;

    /**
     * Create the Spring context
     * @param contextRoot root object to inject
     * @param scanPackages packages to scan
     */
    public SpringContext( Object contextRoot, Supplier<Collection<String>> scanPackages ) {
        this.contextRoot = Objects.requireNonNull(contextRoot);
        appContext = new AnnotationConfigApplicationContext();
        this.scanPackages = Objects.requireNonNull(scanPackages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectMembers(Object instance) {
        SpringUtils.injectMembers(appContext, instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getInstance(Class<T> type) {
        return SpringUtils.getInstance( appContext, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void init() {

        HashSet<String> packages = new HashSet<>();
        packages.addAll(scanPackages.get());

        appContext = new AnnotationConfigApplicationContext();
        appContext.register(FXModule.class);
        appContext.scan(packages.toArray(new String[0]));
        appContext.refresh();

        injectMembers(contextRoot);
    }

}


@Configuration
class FXModule implements ApplicationContextAware {

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    @Bean()
    @Scope("singleton")
    public FXMLLoader provideFxmlLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> SpringUtils.<Object>getInstance(appContext, (Class<Object>) type));
        return loader;
    }


}

//Simply make use of common patterns
class SpringUtils {

    private SpringUtils(){}

    public static void injectMembers(ApplicationContext appContext, Object instance) {
        appContext.getAutowireCapableBeanFactory().autowireBean(instance);
    }

    public static <T> T getInstance(ApplicationContext appContext, Class<T> type) {
        T instance = null;
        try {
            instance = type.newInstance();
            injectMembers(appContext, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

}

