package algrithm;

import instance.Instance;
import instance.Node;
import instance.Route;

import java.util.ArrayList;
import java.util.List;

public class MyALNSSolution {
	
    public List<Route> routes;
    public Cost cost;
    public int vehicleNr;
    public Instance instance;
	public double alpha;
	public double beta;
	
	public static final double  punish = 1000;
	
	public ArrayList<Node> removalCustomers;

    public MyALNSSolution(Instance instance) {
        this.routes = new ArrayList<>();
        this.cost = new Cost();
        this.vehicleNr = 0;
        this.instance = instance;
        
        this.alpha = punish;
        this.beta = punish;
        
        this.removalCustomers = new ArrayList<Node>();
    }
    
    public MyALNSSolution(Solution sol, Instance instance) {
        this.cost = new Cost();
        cost.cost = sol.getTotalCost();
        cost.calculateTotalCost();
        this.vehicleNr = sol.getVehicleNr();
        this.instance = instance;
        
        this.alpha = punish;
        this.beta = punish;
        
        this.routes = new ArrayList<>();
        for (Route route: sol.getRoutes()) {
            this.routes.add(route.cloneRoute());
        }
        
        this.removalCustomers = new ArrayList<Node>();
    }
    
    public MyALNSSolution(MyALNSSolution sol) {
    	this.cost = new Cost(sol.cost);
        this.vehicleNr = sol.vehicleNr;
        this.instance = sol.instance;
        
        this.alpha = sol.alpha;
        this.beta = sol.beta;

        this.routes = new ArrayList<>();
        for (Route route: sol.routes) {
            this.routes.add(route.cloneRoute());
        }
        
        this.removalCustomers = new ArrayList<Node>();
    }
    
	public void removeCustomer(int routePosition, int cusPosition) {
		//Không trùng lặp cửa sổ thời gian

		double[][] distance = instance.getDistanceMatrix();
		
		Route removenRoute = this.routes.get(routePosition);
		
		//System.out.println(this);
		double load = - removenRoute.getRoute().get(cusPosition).getDemand();
		double cost = 
				- distance[removenRoute.getRoute().get(cusPosition - 1).getId()][removenRoute.getRoute().get(cusPosition).getId()]
				- distance[removenRoute.getRoute().get(cusPosition).getId()][removenRoute.getRoute().get(cusPosition + 1).getId()]
				+ distance[removenRoute.getRoute().get(cusPosition - 1).getId()][removenRoute.getRoute().get(cusPosition + 1).getId()];
		
		this.cost.cost += cost;
		this.routes.get(routePosition).getCost().cost += cost;
		this.routes.get(routePosition).getCost().load += load;

		this.cost.loadViolation -= this.routes.get(routePosition).getCost().loadViolation;
		this.cost.timeViolation -= this.routes.get(routePosition).getCost().timeViolation;
		
		removalCustomers.add(removenRoute.removeNode(cusPosition));
	}
	
	public void insertCustomer(int routePosition, int insertCusPosition, Node insertCustomer) {
		//Sao chép cửa sổ thời gian không được xử lý
		double[][] distance = instance.getDistanceMatrix();
		
		Route insertRoute = this.routes.get(routePosition);

		// Tính toán sự thay đổi về chi phí và tải trọng
		double load = + insertCustomer.getDemand();
		double cost = 
				+ distance[insertRoute.getRoute().get(insertCusPosition - 1).getId()][insertCustomer.getId()]
				+ distance[insertCustomer.getId()][insertRoute.getRoute().get(insertCusPosition).getId()]
				- distance[insertRoute.getRoute().get(insertCusPosition - 1).getId()][insertRoute.getRoute().get(insertCusPosition).getId()];

		// Cập nhật về chi phí và tải trọng
		this.cost.cost += cost;
		this.routes.get(routePosition).getCost().cost += cost;
		this.routes.get(routePosition).getCost().load += load;
		if (this.routes.get(routePosition).getCost().load > this.instance.getVehicleCapacity())
			this.cost.loadViolation += this.routes.get(routePosition).getCost().load - this.instance.getVehicleCapacity();
		
		insertRoute.addNodeToRouteWithIndex(insertCustomer, insertCusPosition);;

		// Tính toán đường đi hiện tại, sự vi phạm cửa sổ thời gian, thời gian
		double time = 0;
		double timeWindowViolation = 0;
		for (int i = 1; i < insertRoute.getRoute().size(); i++) {	
			time += distance[insertRoute.getRoute().get(i - 1).getId()][insertRoute.getRoute().get(i).getId()];
			if (time < insertRoute.getRoute().get(i).getTimeWindow()[0])
				time = insertRoute.getRoute().get(i).getTimeWindow()[0];
			else if (time > insertRoute.getRoute().get(i).getTimeWindow()[1])
				timeWindowViolation += time - insertRoute.getRoute().get(i).getTimeWindow()[1];
			
			time += insertRoute.getRoute().get(i).getServiceTime();
		}

		//  Cập nhật sự vi phạm cửa sổ thời gian, thời gian
		this.routes.get(routePosition).getCost().time = time;
		this.routes.get(routePosition).getCost().timeViolation = timeWindowViolation;
		this.cost.timeViolation += timeWindowViolation;
		this.cost.calculateTotalCost(this.alpha, this.beta);
	}
	
	public void evaluateInsertCustomer(int routePosition, int insertCusPosition, Node insertCustomer, Cost newCost) {
		double[][] distance = instance.getDistanceMatrix();
		
		Route insertRoute = this.routes.get(routePosition).cloneRoute();
		
		double load = + insertCustomer.getDemand();
		double cost = 
				+ distance[insertRoute.getRoute().get(insertCusPosition - 1).getId()][insertCustomer.getId()]
				+ distance[insertCustomer.getId()][insertRoute.getRoute().get(insertCusPosition).getId()]
				- distance[insertRoute.getRoute().get(insertCusPosition - 1).getId()][insertRoute.getRoute().get(insertCusPosition).getId()];
		
		newCost.load += load;
		newCost.cost += cost;
		if (newCost.load > this.instance.getVehicleCapacity())
			newCost.loadViolation += this.cost.load - this.instance.getVehicleCapacity();
		
		insertRoute.addNodeToRouteWithIndex(insertCustomer, insertCusPosition);;
		
		double time = 0;
		double timeWindowViolation = 0;
		for (int i = 1; i < insertRoute.getRoute().size(); i++) {	
			time += distance[insertRoute.getRoute().get(i - 1).getId()][insertRoute.getRoute().get(i).getId()];
			if (time < insertRoute.getRoute().get(i).getTimeWindow()[0])
				time = insertRoute.getRoute().get(i).getTimeWindow()[0];
			else if (time > insertRoute.getRoute().get(i).getTimeWindow()[1])
				timeWindowViolation += time - insertRoute.getRoute().get(i).getTimeWindow()[1];
			
			time += insertRoute.getRoute().get(i).getServiceTime();
		}
		
		newCost.time = time;
		newCost.timeViolation = timeWindowViolation;
		
		newCost.calculateTotalCost(this.alpha, this.beta);
	}
	
	public boolean feasible() {
		return (cost.timeViolation < 0.01 && cost.loadViolation < 0.01);
	}
	
	public Solution toSolution() {
		Solution sol = new Solution();
		
		List<Route> solutionRoutes = new ArrayList<>();
        for (Route route: this.routes) {
        	solutionRoutes.add(route.cloneRoute());
        }
		
		sol.setRoutes(solutionRoutes);
		sol.setTotalCost(cost.cost);
		sol.setVehicleNr(vehicleNr);
		
		return sol;
	}
        
    @Override
    public String toString() {
        String result = "Solution{" +
                "Cost = " + cost +
                ", routes = [";

        for (Route vehicle: this.routes) {
        	result += "\n\t" + vehicle;
        }

        return result + "]}";
    }
    
	public int compareTo(MyALNSSolution arg0) {
		if (this.cost.total >  arg0.cost.total ) {
			return 1;
		} else if (this.cost == arg0.cost) {
			return 0;
		} else {
			return -1;
		}
	}

}
