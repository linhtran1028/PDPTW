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
     * vị trí 0 của danh sách chứa kho.
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

    	String dataFileName = "";
    	if (type.equals("Solomon"))
    		dataFileName = "./instances" + "/solomon" + "/solomon_" + size + "/" + name + ".txt";

    	else if (type.equals("Homberger"))
    		dataFileName = "./instances" + "/homberger" + "/homberger_" + size + "/" + name + ".txt";
    	
        BufferedReader bReader = new BufferedReader(new FileReader(dataFileName));

        int data_in_x_lines = Integer.MAX_VALUE; //2147483647

        String line;
        
        while ((line = bReader.readLine()) != null) {

            String datavalue[] = line.split("\\s+");

            if (datavalue.length > 0 && datavalue[0].equals("CUST")) {
                data_in_x_lines = 2;
            }

            if (data_in_x_lines < 1 && datavalue.length > 0) {
            	Node customer = new Node();
                customer.setId(Integer.parseInt(datavalue[1]));
                customer.setX(Double.parseDouble(datavalue[2]));
                customer.setY(Double.parseDouble(datavalue[3]));
                customer.setDemand(Double.parseDouble(datavalue[4]));
                customer.setTimeWindow(Double.parseDouble(datavalue[5]), Double.parseDouble(datavalue[6]));
                customer.setServiceTime(Double.parseDouble(datavalue[7]));
                this.customers.add(customer);
            }
            data_in_x_lines--;
        }
        bReader.close();
        
        numberOfNodes = customers.size();
        System.out.println("Path File:" + dataFileName);
        System.out.println("Input customers success !");
        
    }
    
    //Đọc dữ liệu đầu vào
    public void importVehicleData(int size, String name) throws IOException {

    	String dataFileName = "";
    	if (type.equals("Solomon"))
    		dataFileName = "./instances" + "/solomon" + "/solomon_" + size + "/" + name + ".txt";
    	else if (type.equals("Homberger"))
    		dataFileName = "./instances" + "/homberger" + "/homberger_" + size + "/" + name + ".txt";
    	
        BufferedReader bReader = new BufferedReader(new FileReader(dataFileName));

        int row = 0;

        String line;
        while ((line = bReader.readLine()) != null) {
            String datavalue[] = line.split("\\s+");
//            System.out.print(row+":     ");
//            for (int i = 0; i<  datavalue.length ; i++){
//                System.out.print(i+"-");
//                System.out.print(datavalue[i] + ", ");
//            }
            System.out.print("\n");
            if (row == 4) {
            	//Số lượng xe có sẵn
                this.vehicleNr = Integer.valueOf(datavalue[1]);
                System.out.println("Vehicle Number: " + Integer.valueOf(datavalue[1]));
                //Công suất xe
                this.vehicleCapacity = Integer.valueOf(datavalue[2]);
                System.out.println("Capacity: " + Integer.valueOf(datavalue[2]));
                break;
            }
            row++;
        }
        bReader.close();
        
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
    
    /**
     *
     */
    private void createDistanceMatrix() {
        for (int i = 0; i < this.numberOfNodes; i++) {
            Node n1 = this.customers.get(i);

            for (int j = 0; j < this.numberOfNodes; j++) {
                Node n2 = this.customers.get(j);
                //Tính khoảng cách giữa 2 nút
                this.distanceMatrix[i][j] =(double)(Math.round ( Math.sqrt(Math.pow(n1.getX() - n2.getX(), 2) +
                        Math.pow(n1.getY() - n2.getY(), 2)) * 100 ) / 100.0);;
            }
        }
    }

}
