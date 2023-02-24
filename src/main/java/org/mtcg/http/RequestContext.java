package org.mtcg.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.mtcg.http.exception.MtcgException;

public class RequestContext {

    private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";
    private String httpVerb;
    private String path;
    private List<Header> headers;
    private String body;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public String getHttpVerb() {
        return httpVerb;
    }

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    // Functional features; apply atomic functions to the stream object in
    // sequence to manipulate the data. Note that the type of the expression may
    // change at each link in the chain; this takes us in this case from a
    // Stream<Header> to a simple int.
    public int getContentLength() {
        return headers.stream()
                .filter(header -> CONTENT_LENGTH_HEADER_NAME.equals(header.getName()))
                .findFirst()
                .map(Header::getValue)
                .map(Integer::parseInt)
                .orElse(0);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToken() {
        String token = "";
        for (var header : getHeaders()) {
            if (header.getName().equals("Authorization")) {
                // we don't need the word "Bearer"
                token = header.getValue().split(" ")[1];
            }
        }
        return token;
    }

    public <T> T getBodyAs(Class<T> clazz) {
        try {
            return objectMapper.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            throw new MtcgException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void print() {
        System.out.println("HTTP-Verb: " + httpVerb);
        System.out.println("Path " + path);
        System.out.println("Headers: " + headers);
        System.out.println("Body: " + body);
    }
}
