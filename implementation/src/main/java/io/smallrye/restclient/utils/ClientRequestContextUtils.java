/**
 * Copyright 2019 Red Hat, Inc, and individual contributors.
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
package io.smallrye.restclient.utils;

import org.jboss.resteasy.client.jaxrs.internal.ClientInvocation;
import org.jboss.resteasy.client.jaxrs.internal.ClientRequestContextImpl;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class to pull out common operations on {@link ClientRequestContext}
 *
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * <br>
 * Date: 1/12/19
 */
public class ClientRequestContextUtils {

    /**
     * Get {@link Method} for the client call from {@link ClientRequestContext}
     * @param requestContext the context
     * @return the method
     */
    public static Method getMethod(ClientRequestContext requestContext) {
        ClientInvocation invocation = shellClientInvocation(requestContext);
        return invocation.getClientInvoker().getMethod();
    }

    private static ClientInvocation shellClientInvocation(ClientRequestContext requestContext) {
        try {
            Field invocationField = ClientRequestContextImpl.class.getDeclaredField("invocation");
            invocationField.setAccessible(true);
            return (ClientInvocation) invocationField.get(requestContext);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Failed to get ClientInvocation from request context. Is RestEasy client used underneath?", e);
        }
    }

    private ClientRequestContextUtils() {
    }
}
