/**
 * Copyright 2015-2017 Red Hat, Inc, and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.smallrye.restclient;

import org.jboss.resteasy.client.jaxrs.internal.ClientInvocation;
import org.jboss.resteasy.client.jaxrs.internal.ClientRequestContextImpl;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * <br>
 * Date: 12/14/18
 */
@Priority(Integer.MIN_VALUE)
public class MethodInjectionFilter implements ClientRequestFilter {
    @Override
    public void filter(ClientRequestContext requestContext) {
        ClientInvocation invocation = shellClientInvocation(requestContext);
        Method method = invocation.getClientInvoker().getMethod();
        requestContext.setProperty("org.eclipse.microprofile.rest.client.invokedMethod", method);
    }

    private ClientInvocation shellClientInvocation(ClientRequestContext requestContext) {
        try {
            Field invocationField = ClientRequestContextImpl.class.getDeclaredField("invocation");
            invocationField.setAccessible(true);
            return (ClientInvocation) invocationField.get(requestContext);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Failed to get ClientInvocation from request context. Is RestEasy client used underneath?", e);
        }
    }
}
