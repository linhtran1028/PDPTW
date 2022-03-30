package alns.repair;

import java.util.*;

import algrithm.Cost;
import algrithm.ALNSSolution;
import alns.operation.ALNSAbstractOperation;
import instance.Node;
import instance.Route;


public class RandomRepair extends ALNSAbstractOperation implements IALNSRepair {

    @Override
    public ALNSSolution repair(ALNSSolution s) {

        if(s.removalCustomers.size() == 0) {
            System.err.println("removalCustomers is empty!");
            return s;
        }

        Random r = s.instance.getRandom();
        int insertCusNr = s.removalCustomers.size();

        for (int i = 0; i < insertCusNr; i++) {

            Node insertNode = s.removalCustomers.remove(0);

            //Ngẫu nhiên con đường cần tìm
            int randomRouteNr = r.nextInt(s.routes.size() - 1) + 1;

            // Thiết lập sơ đồ chèn tối ưu
            int bestRoutePosition = -1;
            int bestCusomerPosition = -1;
            Cost bestCost = new Cost();
            bestCost.total = Double.MAX_VALUE;

            ArrayList<Integer> routeList= new ArrayList<Integer>();
            for(int j = 0; j < s.routes.size(); j++)
                routeList.add(j);

            Collections.shuffle(routeList);

            for (int j = 0; j < randomRouteNr; j++) {

                // Chọn ngẫu nhiên một tuyến đường
                int insertRoutePosition = routeList.remove(0);
                Route insertRoute = s.routes.get(insertRoutePosition);

                while(insertRoute.getRoute().size() < 1) {
                    insertRoutePosition = routeList.remove(0);
                    insertRoute = s.routes.get(insertRoutePosition);
                }

                // Lựa chọn ngẫu nhiên vị trí cần tìm
                int insertTimes = r.nextInt(insertRoute.getRoute().size() - 1) + 1;

                ArrayList<Integer> customerList= new ArrayList<Integer>();
                for(int k = 1; k < insertRoute.getRoute().size(); k++)
                    customerList.add(k);

                Collections.shuffle(customerList);

                // Chọn một vị trí ngẫu nhiên
                for (int k = 0; k < insertTimes; k++) {

                    int insertCusPosition = customerList.remove(0);
                    Cost newCost = new Cost(s.cost);
                    s.evaluateInsertCustomer(insertRoutePosition, insertCusPosition, insertNode, newCost);

                    // Cập nhật vị trí chèn tối ưu
                    if (newCost.total < bestCost.total) {
                        bestRoutePosition = insertRoutePosition;
                        bestCusomerPosition = insertCusPosition;
                        bestCost = newCost;
                    }
                }
                // thực hiện thao tác chèn
                s.insertCustomer(bestRoutePosition, bestCusomerPosition, insertNode);
            }
        }

        return s;
    }

}

