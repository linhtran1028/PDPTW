package instance;
import java.util.ArrayList;
import java.util.List;
import algrithm.Cost;
public class Route {
    private int id;


    private List<Node> route;

    /**
     * Chi phí của Tuyến đường hiện tại. Nó được tính bằng tổng khoảng cách của mọi nút tiếp theo so với nút trước đó.
     */
    private Cost cost;


    public Route(int id) {
        this.route = new ArrayList<>();
        this.id = id;
        this.cost = new Cost();
    }

    public Route cloneRoute() {
        Route clone = new Route(this.id);
        clone.cost = new Cost(this.cost);
        clone.route = new ArrayList<>(this.route);

        return clone;
    }

    public int getId() {
        return this.id;
    }

    public List<Node> getRoute() {
        return route;
    }

    /**
     * Trả về nút cuối cùng trong tuyến xe
     */
    public Node getLastNodeOfTheRoute() {
        return this.route.get(this.route.size() - 1);
    }


    public void addNodeToRoute(Node node) {
        this.route.add(node);
    }

    public void addNodeToRouteWithIndex(Node node, int index) {
        this.route.add(index, node);
    }


    public Node removeNode(int index) {
        return this.route.remove(index);
    }

    @Override
    public String toString() {
        String result =  "Route{" +
                "cost = " + this.cost +
                ", route = [";

        for (Node customer: this.route) {
            result += "\n\t\t" + customer;
        }

        return result + "]}";
    }

    public Cost getCost() {
        return this.cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

}
