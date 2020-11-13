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
package com.gluonhq.ignite.micronaut.view;

import com.gluonhq.ignite.micronaut.FXMLRootProvider;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Parent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Implementation of the View, which automatically loads the related XML file by convention.
 * Actual view should conform to following requirements
 * - inherit from FXMLView
 * - FXML file should have exactly the same name, but with ".fxml" extension
 * - The view and FXML file should be located in the same package
 *
 * @param <T> Root node type
 */
public class FXMLView<T extends Parent> implements View<T> {

    @Inject
    private FXMLRootProvider rootProvider;

    // rootProperty
    private final ReadOnlyObjectWrapper<T> rootProperty = new ReadOnlyObjectWrapper<>(this, "root");
    public ReadOnlyObjectProperty<T> rootProperty() {
       return rootProperty.getReadOnlyProperty();
    }

    @Override
    public final T getRoot() {
        return rootProperty().get();
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        rootProperty.set((T) rootProvider.getRootByClass(this.getClass()));
    }

}
