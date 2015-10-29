package si.um.vrbancic;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

public class SentimentPrediction {

    private static StanfordCoreNLP coreNLP = null;
    private static Properties properties = new Properties();

    private static void init() {
        properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        coreNLP = new StanfordCoreNLP(properties);

    }

    public static int predictSentiment(String tweet) {
        if(coreNLP == null) {
            init();
            return predictSentiment(tweet);
        }

        int mainSentiment = 0;

        if(tweet != null && tweet.length() > 0) {
            int longest = 0;

            Annotation annotation = coreNLP.process(tweet);
            for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if(partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }

        return mainSentiment;
    }

}
