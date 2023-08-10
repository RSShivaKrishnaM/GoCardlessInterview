package interactionWithHTTPClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jsonParser.JsonParserClass;

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



    public JsonNode getJsonNode(URI url) {
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();
        String response = httpResponseHandler.performHttpReqRes(url);
        JsonNode jsonNode = null;
        if (response != null) {
            JsonParserClass jsonParserClass = new JsonParserClass();
            try {
                jsonNode = jsonParserClass.parserTheJsonStr(response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonNode;
    }
}
