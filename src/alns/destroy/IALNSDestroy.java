package alns.destroy;

import algrithm.MyALNSSolution;
import alns.operation.IALNSOperation;

public interface IALNSDestroy extends IALNSOperation {

    MyALNSSolution destroy(MyALNSSolution s, int nodes) throws Exception;

}
