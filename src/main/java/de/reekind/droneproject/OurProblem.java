package de.reekind.droneproject;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;

import java.util.Collection;

/**
 * Created by timbe on 05.08.2017.
 */
public class OurProblem {
    private VehicleRoutingProblem problem;

    public static void main(String[] args) {
        		/*
         * get a vehicle type-builder and build a type with the typeId "vehicleType" and one capacity dimension, i.e. weight, and capacity dimension value of 2
		 */
        final int WEIGHT_INDEX = 0;
        final int RANGE_INDEX = 1;
        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType");
        vehicleTypeBuilder.addCapacityDimension(WEIGHT_INDEX, 4);
        vehicleTypeBuilder.addCapacityDimension(RANGE_INDEX, 50);
        VehicleType vehicleType = vehicleTypeBuilder.build();

		/*
         * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
		 */
        VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle1");
        vehicleBuilder.setStartLocation(Location.newInstance(10, 10));
        vehicleBuilder.setType(vehicleType);
        VehicleImpl vehicle = vehicleBuilder.build();





		/*
         * build services at the required locations, each with a capacity-demand of 1.
		 */

        Service service1 = Service.Builder.newInstance("1").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(5, 7)).build();
        Service service2 = Service.Builder.newInstance("2").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(5, 13)).build();

        Service service3 = Service.Builder.newInstance("3").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(15, 7)).build();
        Service service4 = Service.Builder.newInstance("4").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(15, 13)).build();


        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.addVehicle(vehicle);
        vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4);
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);
        VehicleRoutingProblem problem = vrpBuilder.build();

		/*
         * get the algorithm out-of-the-box.
		 */
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

		/*
         * and search a solution
		 */
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

		/*
         * get the best
		 */
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
    }
}
