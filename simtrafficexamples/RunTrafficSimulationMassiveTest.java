package pcd.ass01.simtrafficexamples;

public class RunTrafficSimulationMassiveTest {


	public static void main(String[] args) {

		int numCars = 5000;
		int nSteps = 100;
		int nThreads = Runtime.getRuntime().availableProcessors();

		// For Massive test, set the var
		// 'stop' inside AbstractSimulation to true
		var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars, nThreads);
		simulation.setup();

		log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");

		simulation.run(nSteps);
	}

	private static void log(String msg) {
		System.out.println("[ SIMULATION ] " + msg);
	}
}
