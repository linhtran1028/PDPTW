package alns.destroy;

import algrithm.ALNSSolution;
import alns.operation.IALNSOperation;

public interface IALNSDestroy extends IALNSOperation {

    ALNSSolution destroy(ALNSSolution s, int nodes) throws Exception;

}
