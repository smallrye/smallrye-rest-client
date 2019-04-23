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

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.Test;

public class RestClientBuilderResteasyPropertiesTest {

    @Test
    public void testProperties() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        RestClientBuilder builder = RestClientBuilder.newBuilder();
        builder.property("resteasy.connectionTTL", Arrays.asList(10l, TimeUnit.SECONDS));
        builder.property("resteasy.maxPooledPerRoute", 5);
        ResteasyClientBuilder resteasyBuilder = ((RestClientBuilderImpl) builder).getBuilderDelegate();
        // Unfortunately, there is no other way to test it
        Field connectionTTLUnitField = getField("connectionTTLUnit");
        Field connectionTTLField = getField("connectionTTL");
        Field maxPooledPerRouteField = getField("maxPooledPerRoute");
        assertEquals(TimeUnit.SECONDS, connectionTTLUnitField.get(resteasyBuilder));
        assertEquals(Long.valueOf(10), connectionTTLField.get(resteasyBuilder));
        assertEquals(Integer.valueOf(5), maxPooledPerRouteField.get(resteasyBuilder));
    }

    Field getField(String name) throws NoSuchFieldException, SecurityException {
        Field field = ResteasyClientBuilderImpl.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

}
