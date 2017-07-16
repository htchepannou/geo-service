package io.tchepannou.k.geo.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Component
public class DownloadService {
    private static final String VERSION = "1.0";

    @Autowired
    RequestConfig httpConfig;

    @Autowired
    CloseableHttpClient client;

    public DownloadService() {
    }

    protected DownloadService(final CloseableHttpClient client, RequestConfig config) {
        this.client = client;
        this.httpConfig = config;
    }

    /**
     * Download the content of a web resource
     *
     * @param url - URL of the web resource to download
     * @param out - OutputStream where to store the content of the link
     * @return content size
     * @throws IOException
     */
    public int download(final URL url, final OutputStream out) throws IOException {
        final HttpGet method = createHttpGet(url);
        try (CloseableHttpResponse response = client.execute(method)) {
            final InputStream in = response.getEntity().getContent();
            return IOUtils.copy(in, out);
        }
    }

    private HttpGet createHttpGet(final URL url) {
        final RequestConfig config = RequestConfig.copy(httpConfig)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        final HttpGet method = new HttpGet(url.toString());
        method.setHeader("Connection", "keep-alive");
        method.setConfig(config);
        return method;
    }
}
