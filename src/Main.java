import com.fasterxml.jackson.databind.ObjectMapper;
import consumer.Consumer;
import databases.Accountant;
import databases.ConsumerDB;
import databases.DistributorsDB;
import databases.ProducersDB;
import distributors.Distributor;
import input.Input;
import input.InputLoader;
import output.Consumers;
import output.Distributors;
import output.EnergyProducers;
import output.OutputLoader;
import producer.Producer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() {
    }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        InputLoader inputLoader = new InputLoader(args[0]);
        Input input = inputLoader.readData();

        /* Input data about distributors, consumers, producers and monthly updates are uploaded */
        int numberOfTurns = input.getNumberOfTurns();
        ConsumerDB consumerDB = new ConsumerDB(input.getConsumers());
        DistributorsDB distributorsDB = new DistributorsDB(input.getDistributors());
        ProducersDB producersDB = new ProducersDB(input.getProducers());
        List<List<Consumer>> newConsumers = input.getNewConsumers();
        List<List<Distributor>> newDistributors = input.getNewDistributors();
        List<List<Producer>> newProducers = input.getNewProducers();
        Accountant accountant = new Accountant();
        /*
          Start of round 0
          1. Distributors choose their producers and calculate their production cost
          2. Distributors calculate the initial cost of their contracts
          3. Initial consumers sign their first contracts
          4. Consumers pay their rates to distributors.
          5. Distributors pay their rates
         */
        distributorsDB.searchProducers(producersDB.getProducers());
        distributorsDB.updateContractsPrice();
        consumerDB.signNewContracts(distributorsDB.getCheapestContract(), accountant);
        consumerDB.payAllRates(accountant);
        distributorsDB.payAllRates();

        /*
          Start of a normal round
         */
        for (int i = 0; i < numberOfTurns; i++) {
            /*
              At the beginning of a normal round, I check if new consumers are added or if there
              are changes in distributors' costs
             */
            if (!(newConsumers == null)) {
                for (Consumer consumer : newConsumers.get(i)) {
                    if (consumer != null) {
                        consumerDB.addConsumer(consumer);
                    }
                }
            }
            if (!(newDistributors == null)) {
                for (Distributor d : newDistributors.get(i)) {
                    if (d != null) {
                        distributorsDB.updateDistributor(d);
                    }
                }
            }

            distributorsDB.updateContractsPrice();
            distributorsDB.removeContracts();
            consumerDB.signNewContracts(distributorsDB.getCheapestContract(), accountant);
            consumerDB.payAllRates(accountant);
            distributorsDB.payAllRates();
            consumerDB.removeBankrupts(accountant);
            distributorsDB.removeBankrupts(accountant);

            /*
            The end of a normal round
            */
            if (!(newProducers == null)) {
                for (Producer p : newProducers.get(i)) {
                    if (p != null) {
                        producersDB.updateProducers(p);
                    }
                }
            }
            /*
             All non-bankrupt distributors update their producers if necessary and
             calculate their production cost
            */
            distributorsDB.searchProducers(producersDB.getProducers());
            producersDB.updateMonthlyStats(i + 1);
        }
        /* END OF SIMULATION */

        /*
         Merge the lists of taxpayers who have gone bankrupt with the one who successfully
         completed the game
         */
        consumerDB.mergeLists(accountant);
        distributorsDB.mergeLists(accountant);
        producersDB.sortProducers();

        /* Creates instances that will help me display in the output file */
        List<Consumers> consumers = new ArrayList<>();
        List<Distributors> distributors = new ArrayList<>();
        List<EnergyProducers> energyProducers = new ArrayList<>();

        /*
         Create objects that keep only the details that interest me
         from each consumer and distributor
         */
        for (Consumer c : consumerDB.getConsumers()) {
            consumers.add(new Consumers(c.getConsumerID(), c.getBlackList().isBankrupt(),
                    c.getBudget()));
        }
        for (Distributor d : distributorsDB.getDistributors()) {
            distributors.add(new Distributors(d.getId(), d.getEnergyNeededKW(),
                    d.getContractPrice(), d.getBudget(), d.getProducerStrategy()
                    .strategyToString(), d.isBankrupt(), d.getContracts()));
        }

        for (Producer p : producersDB.getProducers()) {
            energyProducers.add(new EnergyProducers(p.getId(), p.getEnergyType(),
                    p.getMaxDistributors(), p.getPriceKW(), p.getEnergyPerDistributor(),
                    p.getMonthlyStats()));
        }

        /* Write to Output */
        OutputLoader outputLoader = new OutputLoader(consumers, distributors, energyProducers);
        String out = new ObjectMapper().writeValueAsString(outputLoader);
        File outFile = new File(args[1]);
        FileWriter fileWriter = new FileWriter(outFile);
        fileWriter.write(out);
        fileWriter.close();
    }
}


