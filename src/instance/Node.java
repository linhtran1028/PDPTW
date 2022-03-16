package instance;

public class Node {
    private double timeWindows[]; // khung thoi gian khi di qua moi nut
    private double serviceTime; // thoi gian thuc hien moi yeu cau
    private double x;
    private double y;
    private int id;
    private double demand;

  // Khai bao gia tri nut trong khong gia hai chieu, khung thoi gian rang buoc o moi nut
    public Node(Node n) {
        this.x = n.x;
        this.y = n.y;
        this.id = n.id;
        this.demand = n.demand;
        this.serviceTime = n.serviceTime;
        this.timeWindows = new double[] { n.timeWindows[0], n.timeWindows[1] };
    }

//    public Node() {
//
//    }

    public double getServiceTime() {
        return this.serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double[] getTimeWindow() {
        return this.timeWindows;
    }

    public void setTimeWindow(double start, double end) {
        this.timeWindows = new double[] { start, end };
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", demand=" + demand +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;

    }
}
