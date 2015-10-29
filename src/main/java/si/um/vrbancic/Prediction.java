package si.um.vrbancic;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class Prediction {

    private ObjectMapper mapper = new ObjectMapper();
    private int prediction = 0;

    public String predict (String payload) {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try {
            Map<String, Object> tweet = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            prediction = SentimentPrediction.predictSentiment(tweet.get("text").toString());
            if(prediction == 1)
                tweet.put("sentiment", "negative");
            else if(prediction == 2)
                tweet.put("sentiment", "neutral");
            else
                tweet.put("sentiment", "positive");

            return mapper.writeValueAsString(tweet).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payload;
    }

}
