package alns.repair;

import algrithm.MyALNSSolution;
import alns.operation.IALNSOperation;

public interface IALNSRepair extends IALNSOperation {

    MyALNSSolution repair(MyALNSSolution from);
}
