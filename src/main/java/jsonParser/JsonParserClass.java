package jsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

public class JsonParserClass {
    public JsonNode parserTheJsonStr(String response) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
       return mapper.readValue(response, JsonNode.class);

    }
}
