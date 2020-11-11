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
package com.gluonhq.ignite.dagger;

import com.gluonhq.ignite.DIContext;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import javafx.fxml.FXMLLoader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of Dependency Injection context for Dagger
 */
public class DaggerContext implements DIContext {

    private final Object contextRoot;
    private final Supplier<Collection<Object>> modules;

    private ObjectGraph injector;

    /**
     * Create the Dagger context
     * @param contextRoot root object to inject
     * @param modules custom Dagger modules
     */
    public DaggerContext( Object contextRoot, Supplier<Collection<Object>> modules) {
        this.contextRoot = Objects.requireNonNull(contextRoot);
        this.modules = Objects.requireNonNull(modules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectMembers(Object instance) {
        injector.inject(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getInstance(Class<T> cls) {
        return injector.get(cls);
    }

    /**
     * {@inheritDoc}
     */
    public final void init() {
        Collection<Object> allModules = new HashSet<>(modules.get());
        allModules.add(new FXModule());
        injector = ObjectGraph.create(allModules.toArray());
        injectMembers(contextRoot);
    }

    @Module(library = true, complete = false)
    class FXModule  {

        @Provides
        FXMLLoader provideFxmlLoader() {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(DaggerContext.this::getInstance);
            return loader;
        }

    }

}
