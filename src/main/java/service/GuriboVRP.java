package service;

import gurobi.*;
import pojo.Client;
import pojo.Vehicle;

import java.util.ArrayList;


/**
 * Created by Administrator on 2020/9/18.
 */
public class GuriboVRP {


    public int nDepots;                         //number of depots
    public int nCustomers;                      //number of customers to be visited
    public ArrayList<Client> allLocations;        //All locations
    public ArrayList<Vehicle> vehicleList;      //Vehicles
    public double[][] dm;                      //distance matrix
    public double[] q;

    public GuriboVRP(ArrayList<Client> n, ArrayList<Client> d,ArrayList<Vehicle>  vehicleList, double[][] distanceMatrix) throws Exception {
        this.vehicleList = vehicleList;
        this.allLocations = new ArrayList<Client>();
        this.allLocations.addAll(d);
        this.allLocations.addAll(n);
        this.nDepots = d.size();
        this.nCustomers = n.size();
        this.dm = distanceMatrix;
        setQ();
    }

    private void setQ(){
        q = new double[allLocations.size()];
        for (int i = 0; i < allLocations.size(); i++) {
            q[i] = allLocations.get(i).getDemand();
        }
    }

    public double getDistance(double[][] dis, int i, int j) {
        return dis[i][j];
    }


    public void solution() throws Exception {


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

                        x[i][j][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "X" + i + "_" + j + "_" + k);              //arc between node i and j used by vehicle k or not
                    }//k
                }//j
            }//i
            GRBVar[][][] f = new GRBVar[this.allLocations.size()][this.allLocations.size()][this.vehicleList.size()];
            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    for (int k = 0; k < this.vehicleList.size(); k++) {

                        f[i][j][k] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "f" + i + "_" + j + "_" + k);

                    }//k
                }//j
            }//i
            model.update();

           /* //aik: arival time at customer i by vehicle k

            GRBVar[][] a = new GRBVar[this.allLocations.size()][this.vehicleList.size()];

            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int k = 0; k < this.vehicleList.size(); k++) {
                    a[i][k] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "a" + i + k);                //arival time at node i by vehicle k
                }//k
            }//i

            //dep: departure time from customer i by vehicle k

            GRBVar[][] dep = new GRBVar[nDepots][this.vehicleList.size()];

            for (int i = 0; i < nDepots; i++) {
                for (int k = 0; k < this.vehicleList.size(); k++) {
                    dep[i][k] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "dep" + i + k);                //departure time from depot i by vehicle k
                }//k
            }//i
*/

            //3. DEFINE CONSTRAINTS
           //Constraint 1

            for (int i = nDepots; i < this.allLocations.size(); i++) {
                GRBLinExpr constraint1 = new GRBLinExpr();
                for (int j = 0; j < this.allLocations.size(); j++) {

                        for (int k = 0; k < this.vehicleList.size(); k++) {
                            constraint1.addTerm(1, x[i][j][k]);
                        }//k

                }//i

                model.addConstr(constraint1, GRB.EQUAL, 1, "Constraint 1_" + i);
            }//j

            //Constraint 2

            for (int j = nDepots; j < this.allLocations.size(); j++){
                GRBLinExpr constraint2 = new GRBLinExpr();
                for (int i = 0; i < this.allLocations.size(); i++) {
                        for (int k = 0; k < this.vehicleList.size(); k++) {
                            constraint2.addTerm(1, x[i][j][k]);
                        }//k
                }//i

                model.addConstr(constraint2, GRB.EQUAL, 1, "Constraint2_"+j);
            }//j



            //Constraint 3

            for (int i = nDepots; i < this.allLocations.size(); i++) {
                GRBLinExpr constraint3 = new GRBLinExpr();
                for (int k = 0; k < this.vehicleList.size(); k++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                        constraint3.addTerm(1, f[j][i][k]);
                        constraint3.addTerm(-1, f[i][j][k]);
                    }//k
                }//i
                model.addConstr(constraint3, GRB.EQUAL, q[i], "q" + i);
            }//j


            //Constraint 4
            for (int i = 0; i < this.allLocations.size(); i++) {
                for (int j = 0; j < this.allLocations.size(); j++) {
                    if(i != j){
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
                    if(i != j) {
                        for (int k = 0; k < this.vehicleList.size(); k++) {
                            GRBLinExpr lhs = new GRBLinExpr();
                            lhs.addTerm((this.vehicleList.get(k).getCapacity() - q[i]), x[i][j][k]);
                            model.addConstr(lhs, GRB.GREATER_EQUAL, f[i][j][k], "constraint5_" + i + j + k);
                        }//k
                    }
                }//i
            }//j




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
        }//catch
    }//solution
}