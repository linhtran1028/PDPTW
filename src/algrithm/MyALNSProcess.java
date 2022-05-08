package algrithm;

import alns.config.ControlParameter;
import alns.config.IALNSConfig;
import alns.destroy.IALNSDestroy;
import alns.destroy.RandomDestroy;
import alns.destroy.ShawDestroy;
import alns.destroy.WorstCostDestroy;
import alns.repair.GreedyRepair;
import alns.repair.IALNSRepair;
import alns.repair.RandomRepair;
import alns.repair.RegretRepair;
import instance.Instance;

import java.io.IOException;
import java.util.Random;

public class MyALNSProcess {
    private final IALNSConfig config;
    private final IALNSDestroy[] destroy_ops = new IALNSDestroy[]{
            new ShawDestroy(),
            new RandomDestroy(),
            new WorstCostDestroy()
    };
    private final IALNSRepair[] repair_ops = new IALNSRepair[]{
            new RegretRepair(),
            new GreedyRepair(),
            new RandomRepair()
    };

    private final double T_end_t = 0.01;
    private MyALNSSolution s_g = null;
    private MyALNSSolution s_c = null;
    private boolean cpng = false;
    private int i = 0;
    private double T;
    private double T_s;
    private long t_start;
    private double T_end;


    public MyALNSProcess(Solution s_, Instance instance, IALNSConfig c, ControlParameter cp) throws InterruptedException {
    	cpng = cp.isSolutionImages();

        config = c;
        s_g = new MyALNSSolution(s_, instance);
        s_c = new MyALNSSolution(s_g);

        // Khởi tạo tham số alns
        initStrategies();

        // ���ӻ�
        if (cp.isSolutionsLinechart()) {
            //o.add(new SolutionsLinechart(this));
        }
        if (cp.isOperationsLinechart()) {
            //o.add(new OperationsLinechart(this));
        }
    }

    public Solution improveSolution() throws Exception {
        T_s = -(config.getDelta() / Math.log(config.getBig_omega())) * s_c.cost.total;
        T = T_s;
        T_end = T_end_t * T_s;
        t_start = System.currentTimeMillis();
        //o.onStartConfigurationObtained(this);

        while (true) {
            // sc: giải pháp tối ưu cục bộ, tạo ra một giải pháp mới từ giải pháp tối ưu cục bộ
            MyALNSSolution s_c_new = new MyALNSSolution(s_c);
            int q = getQ(s_c_new);

            // xóa bỏ giải pháp
            IALNSDestroy destroyOperator = getALNSDestroyOperator();
            IALNSRepair repairOperator = getALNSRepairOperator();
            //o.onDestroyRepairOperationsObtained(this, destroyOperator, repairOperator, s_c_new, q);

            // destroy solution
            MyALNSSolution s_destroy = destroyOperator.destroy(s_c_new, q);
            //o.onSolutionDestroy(this, s_destroy);

            // sửa lại
            MyALNSSolution s_t = repairOperator.repair(s_destroy);
            //o.onSolutionRepaired(this, s_t);

            System.out.println("Number of iterations" +  i + "current solution" + Math.round(s_t.cost.total * 100) / 100.0);

            // Cập nhật giải pháp
            if (s_t.cost.total < s_c.cost.total) {
                s_c = s_t;
                if (s_t.cost.total < s_g.cost.total) {
                    handleNewGlobalMinimum(destroyOperator, repairOperator, s_t);
                } else {
                    handleNewLocalMinimum(destroyOperator, repairOperator);
                }
            } else {
                // Xác suất chấp nhận các giải pháp kém
                handleWorseSolution(destroyOperator, repairOperator, s_t);
            }
            if (i % config.getTau() == 0 && i > 0) {
                segmentFinsihed();
            }

            T = config.getC() * T;
            i++;

            if (i > config.getOmega() && s_g.feasible()) break;
            if (i > config.getOmega() * 1.5 ) break;
        }

        Solution solution = s_g.toSolution();

        // Thời gian chương trình đầu ra
        double s = Math.round((System.currentTimeMillis() - t_start) * 1000) / 1000000.;
        System.out.println("\nALNS progress cost " + s + "s.");

        for (IALNSDestroy destroy : destroy_ops){
            System.out.println(destroy.getClass().getName() +
                    "used" + destroy.getDraws() + "´Î.");
        }

        for (IALNSRepair repair : repair_ops){
            System.out.println(repair.getClass().getName() +
                    "used" + repair.getDraws() + "´Î.");
        }
        solution.testTime = s;
        return solution;
    }

    private void handleWorseSolution(IALNSDestroy destroyOperator, IALNSRepair repairOperator, MyALNSSolution s_t) {
        // Xác suất chấp nhận các giải pháp kém
    	double p_accept = calculateProbabilityToAcceptTempSolutionAsNewCurrent(s_t);
        if (Math.random() < p_accept) {
            s_c = s_t;
        }
        destroyOperator.addToPi(config.getSigma_3());
        repairOperator.addToPi(config.getSigma_3());
    }

    private void handleNewLocalMinimum(IALNSDestroy destroyOperator, IALNSRepair repairOperator) {
        destroyOperator.addToPi(config.getSigma_2());
        repairOperator.addToPi(config.getSigma_2());
    }

    private void handleNewGlobalMinimum(IALNSDestroy destroyOperator, IALNSRepair repairOperator, MyALNSSolution s_t) throws IOException {
        if (this.cpng) {
        }
        //Giải pháp tốt nhất được chấp nhận
        s_g = s_t;
        destroyOperator.addToPi(config.getSigma_1());
        repairOperator.addToPi(config.getSigma_1());
    }

    private double calculateProbabilityToAcceptTempSolutionAsNewCurrent(MyALNSSolution s_t) {
        return Math.exp (-(s_t.cost.total - s_c.cost.total) / T);
    }

    private int getQ(MyALNSSolution s_c2) {
        int q_l = Math.min((int) Math.ceil(0.05 * s_c2.instance.getCustomerNr()), 10);
        int q_u = Math.min((int) Math.ceil(0.20 * s_c2.instance.getCustomerNr()), 30);

        Random r = new Random();
        return r.nextInt(q_u - q_l + 1) + q_l;
    }


    private void segmentFinsihed() {
        double w_sum = 0;

        for (IALNSDestroy dstr : destroy_ops) {
            double w_old1 = dstr.getW() * (1 - config.getR_p());
            double recentFactor = dstr.getDraws() < 1 ? 0 : (double) dstr.getPi() / (double) dstr.getDraws();
            double w_old2 = config.getR_p() * recentFactor;
            double w_new = w_old1 + w_old2;
            w_sum += w_new;
            dstr.setW(w_new);
        }

        for (IALNSDestroy dstr : destroy_ops) {
            dstr.setP(dstr.getW() / w_sum);
        }
        w_sum = 0;

        for (IALNSRepair rpr : repair_ops) {
            double recentFactor = rpr.getDraws() < 1 ? 0 : (double) rpr.getPi() / (double) rpr.getDraws();
            double w_new = (rpr.getW() * (1 - config.getR_p())) + config.getR_p() * recentFactor;
            w_sum += w_new;
            rpr.setW(w_new);
        }

        for (IALNSRepair rpr : repair_ops) {
            rpr.setP(rpr.getW() / w_sum);
        }
    }


    private IALNSRepair getALNSRepairOperator() {
        double random = Math.random();
        double threshold = 0.;
        for (IALNSRepair rpr : repair_ops) {
            threshold += rpr.getP();
            if (random <= threshold) {
                rpr.drawn();
                return rpr;
            }
        }
        repair_ops[repair_ops.length - 1].drawn();
        return repair_ops[repair_ops.length - 1];
    }


    private IALNSDestroy getALNSDestroyOperator() {
        double random = Math.random();
        double threshold = 0.;
        for (IALNSDestroy dstr : destroy_ops) {
            threshold += dstr.getP();
            if (random <= threshold) {
                dstr.drawn();
                return dstr;
            }
        }

        destroy_ops[destroy_ops.length - 1].drawn();
        return destroy_ops[destroy_ops.length - 1];
    }


    private void initStrategies() {
        for (IALNSDestroy dstr : destroy_ops) {
        	dstr.setDraws(0);
            dstr.setPi(0);
            dstr.setW(1.);
            dstr.setP(1 / (double) destroy_ops.length);
        }
        for (IALNSRepair rpr : repair_ops) {
            rpr.setDraws(0);
        	rpr.setPi(0);
            rpr.setW(1.);
            rpr.setP(1 / (double) repair_ops.length);
        }
    }

    public IALNSConfig getConfig() {
        return this.config;
    }

    public IALNSDestroy[] getDestroy_ops() {
        return this.destroy_ops;
    }

    public IALNSRepair[] getRepair_ops() {
        return this.repair_ops;
    }

    public MyALNSSolution getS_g() {
        return this.s_g;
    }

    public MyALNSSolution getS_c() {
        return this.s_c;
    }

    public boolean isCpng() {
        return this.cpng;
    }

    public int getI() {
        return this.i;
    }

    public double getT() {
        return this.T;
    }

    public double getT_s() {
        return this.T_s;
    }

    public long getT_start() {
        return this.t_start;
    }

    public double getT_end_t() {
        return this.T_end_t;
    }

    public double getT_end() {
        return this.T_end;
    }
}
