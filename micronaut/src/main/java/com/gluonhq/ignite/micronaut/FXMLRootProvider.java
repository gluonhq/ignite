/*
 * Copyright (c) 2020, 2021, Gluon
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
package com.gluonhq.ignite.micronaut;

import io.micronaut.context.ApplicationContext;
import javafx.scene.Node;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Singleton
public class FXMLRootProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FXMLRootProvider.class);

    @Inject private ApplicationContext ctx;
    @Inject private FXMLLoaderFactory loaderFactory;

    public <T extends Node> T getRootByClass(@NotBlank Class<?> viewClass) {
        String viewPath = getViewPath(viewClass);
        String fxml = withExt(viewPath, "fxml");
        T node;

        try {
            LOG.info("Attempting to load " + fxml);
            node = loaderFactory.getLoader().load(viewClass.getResourceAsStream(fxml));
        } catch (IOException e) {
            throw new RuntimeException("Error loading resource " + fxml, e);
        }

        // Make sure that CSS is added to a scene that node is going to be added to
        node.sceneProperty().addListener((o, oldScene, newScene) -> Optional.ofNullable(newScene)
                .map(Scene::getStylesheets)
                .ifPresent( stylesheets ->
                        Optional.ofNullable(viewClass.getResource(withExt(viewPath, "css")))
                                .map(URL::toExternalForm)
                                .filter(r -> !stylesheets.contains(r))
                                .ifPresent(stylesheets::add)
                ));
        return node;
    }

    private String getViewPath(Class<?> cls) {
        return cls.getName().replace('.', '/');
    }

    private String withExt(String viewName, String extName ) {
        String ext = extName.startsWith(".")? extName: "." + extName;
        return String.format("/%s%s", viewName, ext);
    }

}
