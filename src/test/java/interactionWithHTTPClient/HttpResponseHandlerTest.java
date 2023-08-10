package interactionWithHTTPClient;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpResponseHandlerTest {

    HttpResponseHandler httpResponseHandler = new HttpResponseHandler();
    @Test
    public void testHttpRespose(){
        String response = null;
        try {
            response = httpResponseHandler.performHttpReqRes(new URI("https://simpledebit.gocardless.io/merchants"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testHttpResposeWithMerchanID(){
        String response = null;
        try {
            response = httpResponseHandler.performHttpReqRes(new URI("https://simpledebit.gocardless.io/merchants/M28A9"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
