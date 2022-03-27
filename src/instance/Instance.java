package instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Instance {

    private int[] vehicleInfo;
    private int[][] customerInfo;
    private String name;
    private String type;
    private Random r;

    public Random getRandom() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    /**
     * Tạo list danh sách chứa các nút của yêu cầu
     * vị trí 0 của danh sách chứa tổng kho.
     */
    private List<Node> customers;

    /**
     * Số lượng xe có sẵn
     */
    private int vehicleNr;

    private int vehicleCapacity;

    /**
     * Ma trận 2-D sẽ giữ khoảng cách của mọi nút với nhau.
     */
    private double[][] distanceMatrix;


    private int numberOfNodes;

    public List<Node> getCustomers() {
        return new ArrayList<>(this.customers);
    }

    public double[][] getDistanceMatrix() {
        return this.distanceMatrix;
    }

    public int getVehicleNr() {
        return this.vehicleNr;
    }

    public int getVehicleCapacity() {
        return this.vehicleCapacity;
    }

    public int getCustomerNr() {
        return this.numberOfNodes;
    }

    public Instance(int size, String name, String instanceType) throws IOException {
        //Đọc dữ liệu
        this.name = name;
        this.type = instanceType;
        importVehicleData(size, name);

        this.customers = new ArrayList<Node>();
        importCustomerData(size, name);

        this.distanceMatrix = new double[size + 5][size + 5];
        createDistanceMatrix();

        r = new Random();
        r.setSeed(-1);
    }

    public void importCustomerData(int size, String name) throws IOException {


    }

    public void importVehicleData(int size, String name) throws IOException {

        System.out.println("Input vehicle success !");
    }

    public int[] getVehicleInfo() {
        return vehicleInfo;
    }

    public int[][] getCustomerInfo() {
        return customerInfo;
    }

    public String getName() {
        return this.name;
    }

    private void createDistanceMatrix() {
        for (int i = 0; i < this.numberOfNodes; i++) {
            Node n1 = this.customers.get(i);

            for (int j = 0; j < this.numberOfNodes; j++) {
                Node n2 = this.customers.get(j);

                this.distanceMatrix[i][j] =(double)(Math.round ( Math.sqrt(Math.pow(n1.getX() - n2.getX(), 2) +
                        Math.pow(n1.getY() - n2.getY(), 2)) * 100 ) / 100.0);;
            }
        }
    }

}
