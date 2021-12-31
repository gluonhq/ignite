/*
 * Copyright (c) 2020, Gluon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.ignite.spring;

import com.gluonhq.ignite.DIContext;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of dependency injection context for Spring Framework
 */
public class SpringContext implements DIContext {

    private final Object contextRoot;
    private ConfigurableApplicationContext appContext;

    private final Supplier<Collection<String>> scanPackages;

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
     * Create the Spring context
     * @param contextRoot root object to inject
     */
    public SpringContext( Object contextRoot ) {
        this(contextRoot, List::of);
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

        String[] uniquePackages = new HashSet<>(scanPackages.get()).toArray(new String[0]);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(FXModule.class);
        context.scan(uniquePackages);
        context.refresh();

        appContext = context;

        injectMembers(contextRoot);
    }

    /**
     * Allows application context creation customization.<br>
     * For example, it can be used to initialize application context using SpringBoot APIs.<br>
     * In case of using SpringBoot, root class has to be annotated with
     * <code>@ComponentScan("com.gluonhq.ignite.spring")</code>.
     * <br>
     * Here is a simplistic JavaFX application using SpringBoot:
     *
     * <pre>
     * {@code
     * &#064;SpringBootApplication
     * &#064;ComponentScan("com.gluonhq.ignite.spring")
     * public class SpringIgniteDemoApplication extends Application {
     *
     *    public static void main(String[] args) {
     * 	     Application.launch(SpringIgniteDemoApplication.class, args);
     *    }
     *
     *    &#064;Autowired
     *    private FXMLLoader loader;
     *
     *    private final SpringContext context = new SpringContext(this);
     *
     *    &#064;Override
     *    public void start(Stage stage) throws IOException {
     * 		context.init( () -> SpringApplication.run(SpringIgniteDemoApplication.class));
     * 		Parent simpleView = loader.load(SpringIgniteDemoApplication.class.getResourceAsStream("simpleView.fxml"));
     * 		stage.setTitle("Spring Ignite Example");
     * 		stage.setScene(new Scene(simpleView));
     * 		stage.show();
     *    }
     *
     * }
     * }</pre>
     *
     * @param applicationContextBuilder function which has to supply application context. Null result is invalid.
     */
    public final void init( Supplier<ConfigurableApplicationContext> applicationContextBuilder ) {
        appContext = Objects.requireNonNull(applicationContextBuilder).get();
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
    @Scope("prototype")
    public FXMLLoader provideFxmlLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> SpringUtils.getInstance(appContext, type));
        return loader;
    }

}

class SpringUtils {

    private SpringUtils(){}

    public static void injectMembers(ApplicationContext appContext, Object instance) {
        appContext.getAutowireCapableBeanFactory().autowireBean(instance);
    }

    public static <T> T getInstance(ApplicationContext appContext, Class<T> type) {
        T instance = null;
        try {
            instance = type.getDeclaredConstructor().newInstance();
            injectMembers(appContext, instance);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create instance of specified class:  " + type.getName(),e);
        }
        return instance;
    }

}

