package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import interactionWithHTTPClient.HttpResponseHandler;
import jsonParser.JsonParserClass;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String url = "https://simpledebit.gocardless.io/merchants";
    private static final Map<String, Double> ibanToDues = new HashMap<>();

    public static void main(String[] args) {
        try {
            JsonNode parentJsonNode = getJsonNode(new URI(url));
            for (int i = 0; i < parentJsonNode.size(); i++) {
                JsonNode jsonNode = getJsonNode(new URI(url + "/" + parentJsonNode.get(i).asText()));
                JsonNode transactionJsonNode = jsonNode.get("transactions");
                JsonNode discountJsonNode = jsonNode.get("discount");
                double paymentDues = 0.0;
                for (int j = 0; j < transactionJsonNode.size(); j++) {
                    JsonNode paymentNode = transactionJsonNode.get(j);
                    paymentDues += paymentNode.get("amount").asDouble() - paymentNode.get("fee").asDouble();
                }
                if(transactionJsonNode.size() >= discountJsonNode.get("minimum_transaction_count").asInt()){
                    paymentDues = paymentDues - discountJsonNode.get("fees_discount").asDouble();
                }
                ibanToDues.put(jsonNode.get("iban").asText(), paymentDues);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            convertMapToCSV(ibanToDues);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void convertMapToCSV(Map<String, Double> ibanToDues) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jacksonData = objectMapper.writeValueAsString(ibanToDues);
        JsonNode jsonNode = objectMapper.readTree(jacksonData);


        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        csvSchemaBuilder.addColumn("IBAN");
        csvSchemaBuilder.addColumn("payments");
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File("src/main/resources/orderLines.csv"), jsonNode);

    }

    private static JsonNode getJsonNode(URI url) {
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