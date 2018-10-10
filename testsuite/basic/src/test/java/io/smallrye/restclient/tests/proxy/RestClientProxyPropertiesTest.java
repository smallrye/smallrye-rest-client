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
package io.smallrye.restclient.tests.proxy;

import static org.junit.Assert.assertNotNull;

import io.smallrye.restclient.app.HelloResource;
import java.io.File;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class RestClientProxyPropertiesTest {


  @Inject
  @RestClient
  HelloClient restClient;

  @Deployment
  public static WebArchive createTestArchive() {
    return ShrinkWrap.create(WebArchive.class)
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsResource(new File("src/test/resources/microprofile-config.properties"),
            "META-INF/microprofile-config.properties")
        .addPackage(HelloResource.class.getPackage())
        .addPackage(RestClientProxyPropertiesTest.class.getPackage());
  }

  @Test
  public void testClient() {
    assertNotNull(restClient);

  }
}