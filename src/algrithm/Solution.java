package algrithm;

import instance.Route;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	public double testTime;
    private List<Route> routes;
    /**
     * Tổng chi phí của giải pháp. Nó được tính bằng tổng chi phí của tất cả các tuyến đường.
     */
    private double totalCost;

    /**
     *Số lượng các phương tiện
     */
    private int vehicleNr;

    public Solution() {
        this.routes = new ArrayList<>();
        this.totalCost = 0;
        this.vehicleNr = 0;
    }

    public List<Route> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    
    public int getVehicleNr() {
        return vehicleNr;
    }
    
    public void setVehicleNr(int vehicleNr) {
        this.vehicleNr = vehicleNr;
    }


    /**
     * Tạo và trả về một bản sao chính xác của giải pháp hiện tại
     */
    public Solution clone() {
        Solution clone = new Solution();

        clone.totalCost = this.totalCost;
        clone.vehicleNr = this.vehicleNr;

        for (Route route: this.routes) {
            clone.routes.add(route.cloneRoute());
        }

        return clone;
    }
    
    @Override
    public String toString() {
        String result = "Solution{" +
                "totalCost=" +  Math.round(totalCost * 100) / 100.0 +
                ", routes=[";

        for (Route vehicle: this.routes) {
        	if (vehicle.getRoute().size() > 2)
        		result += "\n\t" + vehicle;
        }

        return result + "]}";
    }
}