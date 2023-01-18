package org.mtcg.application.router;

import org.mtcg.http.RequestContext;
import org.mtcg.http.Response;

public interface Route {
    Response process(RequestContext requestContext);
}
