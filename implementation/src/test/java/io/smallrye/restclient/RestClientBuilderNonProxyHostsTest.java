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
package io.smallrye.restclient;

import org.apache.http.HttpHost;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RestClientBuilderNonProxyHostsTest {

    @After
    public void cleanUp() {
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("http.nonProxyHosts");
    }

    @Test
    public void testProxyHostAndPort_without_nonProxyHosts() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException {
        System.setProperty("http.proxyHost", "http://proxy.com");
        System.setProperty("http.proxyPort", "80");

        RestClientBuilder builder = RestClientBuilder.newBuilder().baseUri(new URI("http://hello.com/my-app"));
        builder.build(HelloClient.class);

        HttpHost defaultProxy = getDefaultProxy(builder);

        assertNotNull(defaultProxy);
        assertEquals("http://proxy.com", defaultProxy.getHostName());
        assertEquals(80, defaultProxy.getPort());
        assertEquals("http", defaultProxy.getSchemeName());
    }

    @Test
    public void testProxyHostAndPort_with_not_matching_nonProxyHosts() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException {
        System.setProperty("http.proxyHost", "http://proxy.com");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("http.nonProxyHosts", "localhost|127.*");

        RestClientBuilder builder = RestClientBuilder.newBuilder().baseUri(new URI("http://hello.com/my-app"));
        builder.build(HelloClient.class);

        HttpHost defaultProxy = getDefaultProxy(builder);

        assertNotNull(defaultProxy);
        assertEquals("http://proxy.com", defaultProxy.getHostName());
        assertEquals(80, defaultProxy.getPort());
        assertEquals("http", defaultProxy.getSchemeName());
    }

    @Test
    public void testProxyHostAndPort_with_matching_nonProxyHosts() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException {
        System.setProperty("http.proxyHost", "http://proxy.com");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("http.nonProxyHosts", "10.0.*");

        RestClientBuilder builder = RestClientBuilder.newBuilder().baseUri(new URI("http://10.0.0.1/my-app"));
        builder.build(HelloClient.class);

        HttpHost defaultProxy = getDefaultProxy(builder);

        assertNull(defaultProxy);
    }

    private HttpHost getDefaultProxy(RestClientBuilder builder) throws NoSuchFieldException, SecurityException, IllegalAccessException {
        // Unfortunately, there is no other way to test it
        ResteasyClientBuilder resteasyBuilder = ((RestClientBuilderImpl) builder).getBuilderDelegate();
        Field field = ResteasyClientBuilder.class.getDeclaredField("defaultProxy");
        field.setAccessible(true);
        return (HttpHost) field.get(resteasyBuilder);
    }

    interface HelloClient {
        @GET
        Response hello();
    }
}
