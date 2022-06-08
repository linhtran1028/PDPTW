package algrithm;


import instance.Instance;
import instance.Node;
import instance.Route;

import java.util.ArrayList;
import java.util.List;

public class GreedyVRP {

    private List<Node> customers;

    /**
     * Tất cả các phương tiện
     */
    private List<Route> vehicles;

    /**
     * Ma trận khoảng cách
     */
    private double[][] distanceMatrix;

    private int vehicleCapacity;

    public GreedyVRP(Instance instance) {
        this.customers = instance.getCustomers();
        this.initialCustomerNr = instance.getCustomerNr();
        this.distanceMatrix = instance.getDistanceMatrix();
        this.vehicleCapacity = instance.getVehicleCapacity();

        int vehicleNr = instance.getVehicleNr();
        this.vehicles = new ArrayList<Route>();
        for(int i = 0;i < vehicleNr; ++i) {
            Route route = new Route(i);
            this.vehicles.add(route);
        }
    }

    private int initialCustomerNr;

    public int getCustomerNr() {
        return this.initialCustomerNr;
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
     * Tìm và trả về giải pháp cho bài toán bằng cách sử dụng phương pháp thuật toán tham lam
     */
    public Solution getInitialSolution() {
        Solution solution = new Solution();

        // Nút kho
        Node depot = this.customers.remove(0);

        // Tìm phương tiện có sẵn đầu tiên
        Route currentVehicle = this.vehicles.remove(0);

        // Thêm kho vào tuyến đường
        currentVehicle.addNodeToRoute(depot);
        // Nếu tất cả các xe đều phục vụ được tới các điểm đón và nhận hàng.
        // Lặp lại cho tới khi tất cả được định tuyến hoặc hết xe .
        while (true) {

            if (this.customers.size() == 0)
                break;

            // Lấy nút cuối cùng của tuyến đường hiện tại. Tìm nút gần nhất với nó cũng thỏa mãn giới hạn dung lượng.
            Node lastInTheCurrentRoute = currentVehicle.getLastNodeOfTheRoute();
            // System.out.println(currentVehicle);
            // Khoảng cách của nút gần nhất, nếu có, đến nút cuối cùng trong tuyến.
            double smallestDistance = Double.MAX_VALUE;

            // Nút gần nhất, nếu có, đến nút cuối cùng trong tuyến cũng thỏa mãn giới hạn dung lượng.
            Node closestNode = null;

            // Tìm điểm lân cận gần nhất dựa vào khoảng cách
            for (Node n: this.customers) {
                double distance = this.distanceMatrix[lastInTheCurrentRoute.getId()][n.getId()];
                // Nếu tìm thấy điểm đến mà có giá trị gần nhất với giá trị "Khoảng cách nhỏ nhất", hãy tạm thời lưu trữ điểm đó
                if ( distance < smallestDistance &&
                        (currentVehicle.getCost().load + n.getDemand()) <= vehicleCapacity &&
                        (currentVehicle.getCost().time + distanceMatrix[currentVehicle.getId()][n.getId()]) < n.getTimeWindow()[1] &&
                        (currentVehicle.getCost().time + distanceMatrix[currentVehicle.getId()][n.getId()] + n.getServiceTime() + distanceMatrix[n.getId()][depot.getId()]) < depot.getTimeWindow()[1] )
                {
                    smallestDistance = distance;
                    closestNode = n;
                }
            }

            if (closestNode != null) {
                //Tăng chi phí của tuyến đường hiện tại bằng khoảng cách của nút cuối cùng trước đó với nút mới
                currentVehicle.getCost().cost += smallestDistance;

                // Tăng thời gian của tuyến đường hiện tại bằng khoảng cách của nút cuối cùng trước đó đến nút mới và thời gian phục vụ
                currentVehicle.getCost().time += smallestDistance;


                if (currentVehicle.getCost().time < closestNode.getTimeWindow()[0]) currentVehicle.getCost().time = closestNode.getTimeWindow()[0];

                currentVehicle.getCost().time += closestNode.getServiceTime();

                // Tăng tải trọng của xe theo nhu cầu
                currentVehicle.getCost().load += closestNode.getDemand();

                // Thêm nút gần nhất vào tuyến đường
                currentVehicle.addNodeToRoute(closestNode);

                // Xóa điểm đến khỏi list các nút không được phục vụ
                this.customers.remove(closestNode);
                //Ngược lại nếu không tìm thấy nút nào gần nhất
            } else {
                // Tăng chi phí theo khoảng cách di chuyển từ nút cuối cùng trở về kho
                currentVehicle.getCost().cost += this.distanceMatrix[lastInTheCurrentRoute.getId()][depot.getId()];
                currentVehicle.getCost().time += this.distanceMatrix[lastInTheCurrentRoute.getId()][depot.getId()];

                // Chấm dứt tuyến đường hiện tại bằng cách thêm kho làm điểm đến cuối cùng
                currentVehicle.addNodeToRoute(depot);

                currentVehicle.getCost().calculateTotalCost();

                // Thêm lộ trình cuối cùng vào giải pháp
                solution.addRoute(currentVehicle);

                // Tăng tổng chi phí của giải pháp bằng chi phí của lộ trình cuối cùng
                solution.setTotalCost(solution.getTotalCost() + currentVehicle.getCost().cost);


                if ( this.vehicles.size()==0 ) {
                    break;
                } else {

                    currentVehicle = this.vehicles.remove(0);

                    // Thêm depot làm điểm bắt đầu cho tuyến đường mới
                    currentVehicle.addNodeToRoute(depot);
                }
            }
        }

        // thêm tuyến đường cuối cùng vào giải pháp
        currentVehicle.getCost().cost += this.distanceMatrix[currentVehicle.getLastNodeOfTheRoute().getId()][depot.getId()];
        currentVehicle.getCost().time += this.distanceMatrix[currentVehicle.getLastNodeOfTheRoute().getId()][depot.getId()];
        currentVehicle.addNodeToRoute(depot);
        currentVehicle.getCost().calculateTotalCost();

        solution.addRoute(currentVehicle);
        solution.setTotalCost(solution.getTotalCost() + currentVehicle.getCost().cost);
        solution.setTotalCost((double)(Math.round(solution.getTotalCost() * 1000) / 1000.0));

        return solution;
    }
}