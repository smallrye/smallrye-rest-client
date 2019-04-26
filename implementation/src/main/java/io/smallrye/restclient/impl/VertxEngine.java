package io.smallrye.restclient.impl;

import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

public class VertxEngine implements ClientHttpEngine {
    @Override
    public SSLContext getSslContext() {
        return null;
    }

    @Override
    public HostnameVerifier getHostnameVerifier() {
        return null;
    }

    @Override
    public Response invoke(Invocation invocation) {
        return null;
    }

    @Override
    public void close() {

    }
}
