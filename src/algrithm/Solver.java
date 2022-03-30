package algrithm;

import instance.Instance;
import algrithm.Greedy;
import alns.config.ControlParameter;
import alns.config.IALNSConfig;

public class Solver {
    public Solver() {
    }

    public Solution getInitialSolution(Instance instance) {
        Greedy greedyVRP = new Greedy(instance);
        return greedyVRP.getInitialSolution();
    }

    public Solution improveSolution(Solution s, IALNSConfig ac, ControlParameter cp, Instance is) throws Exception {
        ALNSProcess ALNS = new ALNSProcess(s, is, ac, cp);
        return ALNS.improveSolution();
    }
}
