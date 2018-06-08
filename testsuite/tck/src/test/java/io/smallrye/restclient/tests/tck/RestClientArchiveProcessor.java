/*
 * Copyright 2018 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.smallrye.restclient.tests.tck;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.microprofile.rest.client.tck.WiremockArquillianTest;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

public class RestClientArchiveProcessor implements ApplicationArchiveProcessor {

    private static Logger LOGGER = Logger.getLogger(RestClientArchiveProcessor.class.getName());

    @Override
    public void process(Archive<?> appArchive, TestClass testClass) {

        if (appArchive instanceof WebArchive) {
            LOGGER.info("Preparing archive: " + appArchive);
            WebArchive war = appArchive.as(WebArchive.class);

            PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");

            // Wiremock classes and dependencies that need to be present
            File[] wiremockDeps = pom.resolve("com.github.tomakehurst:wiremock").withTransitivity().asFile();
            war.addAsLibraries(wiremockDeps);

            // Test classes extend WiremockArquillianTest which is not added to the test deployment
            war.addClass(WiremockArquillianTest.class);

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Augmented war: \n" + war.toString(true));
            }
        }
    }
}
