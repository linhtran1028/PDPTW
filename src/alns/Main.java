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

    private static final String[] SOLOMON_ALL = new String[]{
            "C101", "C102", "C103", "C104", "C105", "C106", "C107", "C108", "C109", "C201", "C202", "C203", "C204", "C205", "C206", "C207", "C208",
            "R101", "R102", "R103", "R104", "R105", "R106", "R107", "R108", "R109", "R110", "R111", "R112", "R201", "R202", "R203", "R204", "R205", "R206", "R207", "R208", "R209", "R210", "R211",
            "RC101", "RC102", "RC103", "RC104", "RC105", "RC106", "RC107", "RC108", "RC201", "RC202", "RC203", "RC204", "RC205", "RC206", "RC207", "RC208"
    };
    static String[] SOLOMON_CLUSTERED = new String[]{"C101", "C102", "C103", "C104", "C105", "C106", "C107", "C108", "C109", "C201", "C202", "C203", "C204", "C205", "C206", "C207", "C208"};
    static String[] SOLOMON_RANDOM = new String[]{"R101", "R102", "R103", "R104", "R105", "R106", "R107", "R108", "R109", "R110", "R111", "R112", "R201", "R202", "R203", "R204", "R205", "R206", "R207", "R208", "R209", "R210", "R211",};
    static String[] SOLOMON_CLUSTERRANDOM = new String[]{"RC101", "RC102", "RC103", "RC104", "RC105", "RC106", "RC107", "RC108", "RC201", "RC202", "RC203", "RC204", "RC205", "RC206", "RC207", "RC208"};
    static String[] VRPFD_INSTANCES = new String[]{"C108", "C206", "C203", "R202", "R207", "R104", "RC202", "RC205", "RC208"};
    static String[] Homberger_200 = new String[] {"C1_2_1", "C1_2_2", "C1_2_3", "C1_2_4"};
    static String[] Homberger_400 = new String[] {"C1_4_1", "C1_4_2", "C1_4_3", "C1_4_4"};
    
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
