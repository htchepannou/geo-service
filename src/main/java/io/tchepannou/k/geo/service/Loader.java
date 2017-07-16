package io.tchepannou.k.geo.service;

import java.io.IOException;
import java.io.InputStream;

public interface Loader {
    int load (InputStream in) throws IOException;
}
