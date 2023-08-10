package interactionWithHTTPClient;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpResponseHandler {

    public String performHttpReqRes(URI url){
        String response  ;
        HttpRequest httpRequest = HttpRequest.newBuilder(url).build();
        HttpClient httpClient = HttpClient.newBuilder().build();

        try {
            HttpResponse httpResponse =httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body().toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
