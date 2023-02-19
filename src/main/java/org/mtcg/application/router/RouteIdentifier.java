package org.mtcg.application.router;

import java.util.Objects;

public class RouteIdentifier {
    private final String path;
    private final String pathArgument;
    private final String httpVerb;

    public RouteIdentifier(String path, String httpVerb) {
        if (path.split("/").length > 2){
            this.path = path.split("/")[1];
            this.pathArgument = path.split("/")[2];
        } else {
            this.path = path;
            this.pathArgument = null;
        }
        this.httpVerb = httpVerb;
    }

    public static RouteIdentifier routeIdentifier(String path, String httpVerb) {
        return new RouteIdentifier(path, httpVerb);
    }

    public String getPath() {
        return path;
    }

    public String getPathArgument() {
        return pathArgument;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RouteIdentifier routeIdentifier = (RouteIdentifier) o;
        return path.equals(routeIdentifier.path) &&
                httpVerb.equals(routeIdentifier.httpVerb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, httpVerb);
    }
}
