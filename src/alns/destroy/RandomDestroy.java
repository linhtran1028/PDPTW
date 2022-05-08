package alns.destroy;

import algrithm.MyALNSSolution;
import alns.operation.ALNSAbstractOperation;
import instance.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomDestroy extends ALNSAbstractOperation implements IALNSDestroy {

	@Override
	public MyALNSSolution destroy(MyALNSSolution s, int removeNr) throws Exception {
		
		if(s.removalCustomers.size() != 0) {
			System.err.println("removalCustomers is not empty.");
			return s;
		}
		
		while(s.removalCustomers.size() < removeNr ) {

			// Lấy số ngẫu nhiên
			Random r = s.instance.getRandom();
			
    		ArrayList<Integer> routeList= new ArrayList<Integer>();
            for(int j = 0; j < s.routes.size(); j++)
                routeList.add(j);  
            
            Collections.shuffle(routeList);

			// Chọn đường dẫn nơi nút bị loại bỏ
			int removenRoutePosition = routeList.remove(0);
			Route removenRoute = s.routes.get(removenRoutePosition);
			
			while(removenRoute.getRoute().size() <= 2) {
				removenRoutePosition = routeList.remove(0);
				removenRoute = s.routes.get(removenRoutePosition);
			}

			// Chọn điểm đã loại bỏ
			int removenCustomerPosition = r.nextInt(removenRoute.getRoute().size() - 2) + 1;
			
			s.removeCustomer(removenRoutePosition, removenCustomerPosition);
		}

		return s;
	}

}
