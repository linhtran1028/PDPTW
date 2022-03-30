package alns.repair;

import algrithm.ALNSSolution;
import alns.operation.IALNSOperation;

public interface IALNSRepair extends IALNSOperation {

    ALNSSolution repair(ALNSSolution from);
}
