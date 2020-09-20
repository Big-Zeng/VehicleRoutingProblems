package service;

import gurobi.*;
import pojo.Client;
import pojo.Vehicle;

import java.util.ArrayList;


/**
 * Created by Administrator on 2020/9/18.
 * reference from Ç. Koç, T. Bektaş, O. Jabali, G. Laporte, Thirty years of heterogeneous vehicle routing, Eur. J. Oper. Res. 249 (2016) 1–21. https://doi.org/10.1016/j.ejor.2015.07.020.
 */
public class GuriboVRP implements HVRPMainInterface {
    public int nDepots;                         //number of depots
    public ArrayList<Client> allLocations;        //All locations
    public ArrayList<Vehicle> vehicleList;      //Vehicles
    public double[][] dm;                      //distance matrix
    public double[] q;

    private void setQ() {
        q = new double[allLocations.size()];
        for (int i = 0; i < allLocations.size(); i++) {
            q[i] = allLocations.get(i).getDemand();
        }
    }

    public double getDistance(double[][] dis, int i, int j) {
        return dis[i][j];
    }

    //gurobi
    public void solution(){
        // 1. DEFINE MODEL -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        try {
            GRBEnv env = new GRBEnv();
            env.set(GRB.IntParam.OutputFlag, 0);        //set to 1 to get constraint overview

            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "VRP");

            // 2. DEFINE VARIABLES -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            //Xijqk

            GRBVar[][][] x = new GRBVar[this.allLocations.size()][this.allLocations.size()][this.vehicleList.size()];

            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    for (int k = 0; k < this.vehicleList.size(); k++) {
                        //double dij = getDistance(dm, i, j);
                        //get.Distance retrieves the distance between two nodes that are indicated by their IDs
                        if(i!=j)
                        x[i][j][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "X" + i + "_" + j + "_" + k);              //arc between node i and j used by vehicle k or not
                    }//k
                }//j
            }//i


            GRBVar[][][] f = new GRBVar[this.allLocations.size()][this.allLocations.size()][this.vehicleList.size()];
            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    for (int k = 0; k < this.vehicleList.size(); k++) {
                        if (i != j)
                        f[i][j][k] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "f" + i + "_" + j + "_" + k);

                    }//k
                }//j
            }//i
            model.update();

            //3. DEFINE CONSTRAINTS
            //Constraint 1
//            for (int i = nDepots; i < this.allLocations.size(); i++) {
//                GRBLinExpr constraint1 = new GRBLinExpr();
//                for (int j = 0; j < this.allLocations.size(); j++) {
//                    if(i!=j)
//                    for (int k = 0; k < this.vehicleList.size(); k++) {
//                        constraint1.addTerm(1, x[i][j][k]);
//                    }//k
//                }//i
//                model.addConstr(constraint1, GRB.EQUAL, 1, "Constraint 1_" + i);
//            }//j

            for (int p = nDepots; p < this.allLocations.size(); p++) {
                for (int k = 0; k < this.vehicleList.size(); k++) {
                    GRBLinExpr constraint1 = new GRBLinExpr();
                    for (int i = 0; i < this.allLocations.size(); i++) {
                        if(p!=i)
                        constraint1.addTerm(1, x[i][p][k]);
                    }
                    for (int j = 0; j < this.allLocations.size(); j++) {
                        //if (i != j) {
                        if(p!=j)
                        constraint1.addTerm(-1, x[p][j][k]);
                        //}
                    }
                    model.addConstr(constraint1, GRB.EQUAL, 0, "Constraint1_" + p + "_" + k);
                }
            }

            //Constraint 2
            for (int j = nDepots; j < this.allLocations.size(); j++) {
                GRBLinExpr constraint2 = new GRBLinExpr();
                for (int i = 0; i < this.allLocations.size(); i++) {
                    if(i!=j)
                    for (int k = 0; k < this.vehicleList.size(); k++) {
                        constraint2.addTerm(1, x[i][j][k]);
                    }//k
                }//i

                model.addConstr(constraint2, GRB.EQUAL, 1, "Constraint2_" + j);
            }//j


            //Constraint 3

            for (int i = nDepots; i < this.allLocations.size(); i++) {
                GRBLinExpr constraint3 = new GRBLinExpr();
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if(i!=j)
                    for (int k = 0; k < this.vehicleList.size(); k++) {
                        constraint3.addTerm(1, f[j][i][k]);
                        constraint3.addTerm(-1, f[i][j][k]);
                    }//k
                }//i
                model.addConstr(constraint3, GRB.EQUAL, q[i], "q" + i);
            }//j


            //Constraint 4
            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if (i != j) {
                        for (int k = 0; k < this.vehicleList.size(); k++) {
                            GRBLinExpr lhs = new GRBLinExpr();
                            lhs.addTerm(q[j], x[i][j][k]);
                            model.addConstr(lhs, GRB.LESS_EQUAL, f[i][j][k], "constraint4_" + i + j + k);
                        }//k
                    }

                }//i
            }//j

            //Constraint 5
            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if (i != j) {
                        for (int k = 0; k < this.vehicleList.size(); k++) {
                            GRBLinExpr lhs = new GRBLinExpr();
                            lhs.addTerm((this.vehicleList.get(k).getCapacity() - q[i]), x[i][j][k]);
                            model.addConstr(lhs, GRB.GREATER_EQUAL, f[i][j][k], "constraint5_" + i + j + k);
                        }//k
                    }
                }//i
            }//j

            //Constraint 6  限制车型数量
            for (int k = 0; k < this.vehicleList.size(); k++) {
                GRBLinExpr lhs = new GRBLinExpr();
                for (int j = nDepots; j < this.allLocations.size(); j++) {
                    lhs.addTerm(1, x[0][j][k]);
                }
                model.addConstr(lhs, GRB.LESS_EQUAL, this.vehicleList.get(k).getSize(), "constraint6_" + k);
            }


            model.update();

            //4. SET OBJECTIVE ---------------------------------------------------------------------------------------------------------------------------------------

            GRBLinExpr expr = new GRBLinExpr();

            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if (i != j) {
                        for (int k = 0; k < this.vehicleList.size(); k++) {
                            double dij = getDistance(dm, i, j);
                            double travelingDij = dij * this.vehicleList.get(k).getmCost();
                            expr.addTerm(travelingDij, x[i][j][k]);
                        }//k
                    }//if
                }//j
            }//i
            model.setObjective(expr, GRB.MINIMIZE);

            model.update();

            model.optimize();
            System.out.println("is the solution feasible? " + model.get(GRB.IntAttr.Status));

            model.write("constraintOutput.lp");

            //5. CONSTRUCT SOLUTION ---------------------------------------------------------------------------------------------------------------------------------------------
            double obj = model.get(GRB.DoubleAttr.ObjVal);

            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if (i != j) {
                        for (int k = 0; k < this.vehicleList.size(); k++) {

                            if (x[i][j][k].get(GRB.DoubleAttr.X) > 0) {
                                System.out.println(x[i][j][k].get(GRB.StringAttr.VarName) + " " + x[i][j][k].get(GRB.DoubleAttr.X));
                                System.out.println("Node: " + (i) + " goes to node: " + (j) + " by vehicle: " + (k + 1));
                            }//if
                        }//k
                    }//if
                }//j
            }//i

            System.out.println("The objective value is: " + obj);


            //6. DISPOSE OF MODEL AND ENVIRONMENT
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            e.printStackTrace();
        }
    }


   /* public void solution2() throws Exception {
        try {
            GRBEnv env = new GRBEnv();
            env.set(GRB.IntParam.OutputFlag, 0);        //set to 1 to get constraint overview

            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "VRP");

            GRBVar[][][] x = new GRBVar[this.allLocations.size()][this.allLocations.size()][this.vehicleList.size()];

            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    for (int k = 0; k < this.vehicleList.size(); k++) {
                        if(i!=j)
                            x[i][j][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "X" + i + "_" + j + "_" + k);              //arc between node i and j used by vehicle k or not
                    }//k
                }//j
            }//i

            GRBVar[][] f = new GRBVar[this.allLocations.size()][this.allLocations.size()];
            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if (i != j) {
                        f[i][j] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "f" + i + "_" + j);
                    }
                }
            }

           //constraint 1
            for (int j = nDepots; j < this.allLocations.size(); j++) {
                GRBLinExpr constraint1 = new GRBLinExpr();
                for (int k = 0; k < this.vehicleList.size(); k++) {
                    for (int i = 0; i < this.allLocations.size(); i++) {
                        constraint1.addTerm(1, x[i][j][k]);
                    }
                }
                model.addConstr(constraint1, GRB.EQUAL, 1, "Constraint2_" + j);
            }

            //constraint 2

            for (int p = nDepots; p < this.allLocations.size(); p++) {
                for (int k = 0; k < this.vehicleList.size(); k++) {
                    GRBLinExpr constraint2 = new GRBLinExpr();
                    for (int i = 0; i < this.allLocations.size(); i++) {
                        if(p!=i)
                            constraint2.addTerm(1, x[i][p][k]);
                    }
                    for (int j = 0; j < this.allLocations.size(); j++) {
                        //if (i != j) {
                        if(p!=j)
                            constraint2.addTerm(-1, x[p][j][k]);
                        //}
                    }
                    model.addConstr(constraint2, GRB.EQUAL, 0, "Constraint1_" + p + "_" + k);
                }
            }




























        } catch (Exception e) {

            e.printStackTrace();
        }

    }*/



    @Override
    public void HVRPSolution(ArrayList<Client> clients, ArrayList<Vehicle> vehicles, double[][] dis) throws Exception {
        this.vehicleList = vehicles;
        this.allLocations = clients;
        this.nDepots = 1;
        this.dm = dis;
        setQ();
        solution();
    }
}