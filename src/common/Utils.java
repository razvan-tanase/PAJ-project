package common;

import consumer.Consumer;
import distributors.Distributor;
import entities.EnergyType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import producer.Producer;

import java.util.ArrayList;
import java.util.List;

/**
 * The class contains static methods that helps with parsing
 */
public final class Utils {
    /**
     * for coding style
     */
    private Utils() {
    }

    /**
     * @param consumer Array of JSONs
     * @return A list with new consumers in a round
     */
    public static List<Consumer> newConsumer(final JSONArray consumer) {
        if (consumer == null) {
            return null;
        } else {
            List<Consumer> list = new ArrayList<>();
            for (Object object : consumer) {
                list.add(new Consumer(
                        Integer.parseInt(((JSONObject) object).get(Constants.ID)
                                .toString()),
                        Long.parseLong(((JSONObject) object).get(Constants.INITIAL_BUDGET)
                                .toString()),
                        Long.parseLong(((JSONObject) object).get(Constants.MONTHLY_INCOME)
                                .toString())
                ));
            }
            return list;
        }
    }

    /**
     * This method is used to get distributor cost changes in monthly updates
     *
     * @param distributor Array of JSONs
     * @return A list of existing distributors but with their costs changed and the other
     * fields set on default values
     */
    public static List<Distributor> distributorCostChanges(final JSONArray distributor) {
        if (distributor == null) {
            return null;
        } else {
            List<Distributor> list = new ArrayList<>();
            for (Object object : distributor) {
                list.add(new Distributor(
                        Integer.parseInt(((JSONObject) object).get(Constants.ID)
                                .toString()),
                        Long.parseLong(((JSONObject) object).get(Constants.INFRA_COST)
                                .toString())
                ));
            }
            return list;
        }
    }

    /**
     * This method is used to get the new energy per distributor of producers in monthly updates
     *
     * @param producer Array of JSONs
     * @return A list of existing producers but with their energy per distributor changed and
     * the other fields set on default values
     */
    public static List<Producer> producerCostChanges(final JSONArray producer) {
        if (producer == null) {
            return null;
        } else {
            List<Producer> list = new ArrayList<>();
            for (Object object : producer) {
                list.add(new Producer(
                        Integer.parseInt(((JSONObject) object).get(Constants.ID)
                                .toString()),
                        Long.parseLong(((JSONObject) object).get(Constants.ENERGY_PER_DISTRIBUTOR)
                                .toString())
                ));
            }
            return list;
        }
    }

    /**
     * @param energy Energy type of a producer found as a string in input files
     * @return An enum corresponding to the string
     */
    public static EnergyType stringToEnergy(final String energy) {
        return switch (energy) {
            case "WIND" -> EnergyType.WIND;
            case "SOLAR" -> EnergyType.SOLAR;
            case "HYDRO" -> EnergyType.HYDRO;
            case "COAL" -> EnergyType.COAL;
            case "NUCLEAR" -> EnergyType.NUCLEAR;
            default -> throw new IllegalStateException("Unexpected value: " + energy);
        };
    }
}
