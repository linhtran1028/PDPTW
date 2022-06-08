package alns;


import algrithm.CheckSolution;
import algrithm.Solution;
import algrithm.Solver;
import alns.config.ALNSConfiguration;
import alns.config.ControlParameter;
import alns.config.IALNSConfig;
import instance.Instance;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class Main {
    
    public static void main(String args[]) {
    	
        String[] instances = { "C101" };
        String[][] result = new String[instances.length][];

        for (int j = 0; j < instances.length; j = j + 1) {
            try {
                result[j] = solve(
                        instances[j],
                        "Solomon",
                        25,
                        ALNSConfiguration.DEFAULT,
                        new ControlParameter(
                                false,
                                false,
                                false
                        ));

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private static String[] solve(String name, String instanceType, int size, IALNSConfig c, ControlParameter cp) throws Exception {

        // Đọc dữ liệu đầu vào
        Instance instance = new Instance(size, name, instanceType);
        // Kiểm tra giải pháp
        CheckSolution checkSolution = new CheckSolution(instance);
        // giải pháp mới được khởi tạo
        Solver solver = new Solver();

        Solution is = solver.getInitialSolution(instance);
        Solution ims = solver.improveSolution(is, c, cp, instance);
        System.out.println(ims);
        System.out.println(checkSolution.Check(ims));
        
        String[] result = {String.valueOf(ims.getTotalCost()), String.valueOf(ims.testTime)};
        return result;
    }
}
