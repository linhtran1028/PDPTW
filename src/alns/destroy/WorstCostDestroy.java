package alns.destroy;

import java.util.ArrayList;
import java.util.Collections;

import algrithm.ALNSSolution;
import alns.operation.ALNSAbstractOperation;
import instance.Node;
import instance.Route;
import instance.Instance;


public class WorstCostDestroy extends ALNSAbstractOperation implements IALNSDestroy {

    @Override
    public ALNSSolution destroy(ALNSSolution s, int removeNr) throws Exception {

        if(s.removalCustomers.size() != 0) {
            System.err.println("removalCustomers is not empty.");
            return s;
        }

        // Tính toán giá cả để đưa ra đánh giá
        ArrayList<Fitness> customerFitness = new  ArrayList<Fitness>();
        for(Route route : s.routes) {
            for (Node customer : route.getRoute()) {
                double fitness = Fitness.calculateFitness(s.instance, customer, route);
                customerFitness.add(new Fitness(customer.getId(), fitness));
            }
        }
        Collections.sort(customerFitness);

        ArrayList<Integer> removal = new ArrayList<Integer>();
        for(int i = 0; i < removeNr; ++i) removal.add(customerFitness.get(i).customerNo);

        for(int j = 0; j < s.routes.size(); j++) {
            for (int i = 0; i < s.routes.get(j).getRoute().size();++i) {
                Node customer = s.routes.get(j).getRoute().get(i);
                if(removal.contains(customer.getId())) {
                    s.removeCustomer(j, i);
                }
            }
        }

        return s;
    }
}

class Fitness implements Comparable<Fitness>{
    public int customerNo;
    public double fitness;

    public Fitness() {}

    public Fitness(int cNo, double f) {
        customerNo = cNo;
        fitness = f;
    }

    public static double calculateFitness(Instance instance, Node customer, Route route) {
        double[][] distance = instance.getDistanceMatrix();

        double fitness =
                (route.getCost().getTimeViolation() + route.getCost().getLoadViolation() + customer.getDemand()) *
                        ( distance[customer.getId()][route.getRoute().get(0).getId()] +
                                distance[route.getRoute().get(0).getId()][customer.getId()] );

        return fitness;
    }

    @Override
    public int compareTo(Fitness o) {
        Fitness s = (Fitness) o;
        if (s.fitness > this.fitness  ) {
            return 1;
        } else if (this.fitness == s.fitness) {
            return 0;
        } else {
            return -1;
        }
    }

}

