package alns.repair;

import algrithm.Cost;
import algrithm.MyALNSSolution;
import instance.Node;

public class GreedyRepair extends ALNSAbstractRepair implements IALNSRepair {

	@Override
	public MyALNSSolution repair(MyALNSSolution s) {
		//nếu không có yêu cầu nào bị xóa
    	if(s.removalCustomers.size() == 0) {
			System.err.println("removalCustomers is empty!");
			return s;
		}
    	
    	int removeNr = s.removalCustomers.size();
    	
		for(int k = 0; k < removeNr; k++) {
			
			Node insertNode = s.removalCustomers.remove(0);
			
			double bestCost;
			int bestCusP = -1;
			int bestRouteP = -1;
			bestCost = Double.POSITIVE_INFINITY;
        	
			for(int j = 0; j < s.routes.size(); j++) {			
        		
				if(s.routes.get(j).getRoute().size() < 1) {
        			continue;
        		}

				// Tìm vị trí chèn tối ưu
            	for (int i = 1; i < s.routes.get(j).getRoute().size() - 1; ++i) {

					// Đánh giá vị trí chèn
    				Cost newCost = new Cost(s.cost);
    				s.evaluateInsertCustomer(j, i, insertNode, newCost);

            		if(newCost.total > Double.MAX_VALUE) {
            			newCost.total = Double.MAX_VALUE;
            		}

					// nếu tìm thấy vị trí chèn tốt hơn, hãy đặt vị trí cần chèn trong lần di chuyển
					// và cập nhật chi phí tối thiểu được tìm thấy
            		if (newCost.total < bestCost) {
            			bestCusP = i;
            			bestRouteP = j;
            			bestCost = newCost.total;	
            		}
            	}
        	}
			s.insertCustomer(bestRouteP, bestCusP, insertNode);
		}
        return s;
    }
}