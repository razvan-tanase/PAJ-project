package input;

import common.Constants;
import common.Utils;
import consumer.Consumer;
import distributors.Distributor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import producer.Producer;
import strategies.StrategyFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The class reads and parses the data from the tests
 */
public final class InputLoader {
    /**
     * The path to the input file
     */
    private final String inputPath;

    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * The method reads the database
     *
     * @return an Input object
     */
    public Input readData() {
        JSONParser jsonParser = new JSONParser();
        StrategyFactory factory = StrategyFactory.getInstance();
        List<Consumer> consumers = new ArrayList<>();
        List<Distributor> distributors = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();
        List<List<Consumer>> newConsumers = new ArrayList<>();
        List<List<Distributor>> newDistributors = new ArrayList<>();
        List<List<Producer>> newProducers = new ArrayList<>();


        // Parsing the contents of the JSON file
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser
                    .parse(new FileReader(inputPath));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject initialData = (JSONObject) Objects.requireNonNull(jsonObject)
                .get(Constants.INITIAL_DATA);
        int numberOfTurns = Integer.parseInt(jsonObject
                .get(Constants.NUMBER_OF_TURNS).toString());
        JSONArray jsonMonthlyUpdates = (JSONArray)
                jsonObject.get(Constants.MONTHLY_UPDATES);
        JSONArray jsonConsumers = (JSONArray)
                initialData.get(Constants.CONSUMERS);
        JSONArray jsonDistributors = (JSONArray)
                initialData.get(Constants.DISTRIBUTORS);
        JSONArray jsonProducers = (JSONArray)
                initialData.get(Constants.PRODUCERS);

        if (jsonMonthlyUpdates != null) {
            for (Object jsonMonthlyUpdate : jsonMonthlyUpdates) {
                newConsumers.add(Utils.newConsumer((JSONArray) ((JSONObject) jsonMonthlyUpdate)
                        .get(Constants.NEW_CONSUMERS)));
                newDistributors.add(Utils.distributorCostChanges((JSONArray)
                        ((JSONObject) jsonMonthlyUpdate).get(Constants.DIST_CHANGES)));
                newProducers.add(Utils.producerCostChanges((JSONArray)
                        ((JSONObject) jsonMonthlyUpdate).get(Constants.PRO_CHANGES)));
            }
        }

        if (jsonConsumers != null) {
            for (Object jsonConsumer : jsonConsumers) {
                consumers.add(new Consumer(
                        Integer.parseInt(((JSONObject) jsonConsumer).get(Constants.ID)
                                .toString()),
                        Long.parseLong(((JSONObject) jsonConsumer)
                                .get(Constants.INITIAL_BUDGET).toString()),
                        Long.parseLong(((JSONObject) jsonConsumer)
                                .get(Constants.MONTHLY_INCOME).toString())
                ));
            }
        }

        if (jsonDistributors != null) {
            for (Object jsonDistributor : jsonDistributors) {
                distributors.add(new Distributor(
                        Integer.parseInt(((JSONObject) jsonDistributor).get(Constants.ID)
                                .toString()),
                        Integer.parseInt(((JSONObject) jsonDistributor).
                                get(Constants.CONTRACT_LENGTH).toString()),
                        Long.parseLong(((JSONObject) jsonDistributor)
                                .get(Constants.INITIAL_BUDGET).toString()),
                        Long.parseLong(((JSONObject) jsonDistributor)
                                .get(Constants.INIT_INFRA_COST).toString()),
                        Long.parseLong(((JSONObject) jsonDistributor)
                                .get(Constants.ENERGY_NEEDED).toString()),
                        factory.selectStrategy((String) ((JSONObject)
                                jsonDistributor).get(Constants.STRATEGY))
                ));
            }
        }

        if (jsonProducers != null) {
            for (Object jsonProducer : jsonProducers) {
                producers.add(new Producer(
                        Integer.parseInt(((JSONObject) jsonProducer).get(Constants.ID)
                                .toString()),
                        Utils.stringToEnergy((String) ((JSONObject) jsonProducer).
                                get(Constants.ENERGY_TYPE)),
                        Long.parseLong(((JSONObject) jsonProducer)
                                .get(Constants.MAX_DISTRIBUTORS).toString()),
                        Float.parseFloat(((JSONObject) jsonProducer)
                                .get(Constants.PRICE_KW).toString()),
                        Long.parseLong(((JSONObject) jsonProducer)
                                .get(Constants.ENERGY_PER_DISTRIBUTOR).toString())
                ));
            }
        }
        return new Input(consumers, distributors, producers, newConsumers,
                newDistributors, numberOfTurns, newProducers);
    }
}
