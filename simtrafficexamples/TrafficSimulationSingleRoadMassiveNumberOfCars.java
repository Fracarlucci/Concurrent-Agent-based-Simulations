package pcd.ass01.simtrafficexamples;

import pcd.ass01.simengineconcur.Barrier;
import pcd.ass01.simengineseq.AbstractAgent;
import pcd.ass01.simengineseq.AbstractSimulation;
import pcd.ass01.simtrafficbase.*;

import java.util.LinkedList;
import java.util.List;

public class TrafficSimulationSingleRoadMassiveNumberOfCars extends AbstractSimulation {

	private final int numCars;
    private final ThreadManager threadManager;
    private final RoadsEnv env;

	public TrafficSimulationSingleRoadMassiveNumberOfCars(int numCars, int nThreads) {
		super();
		this.numCars = numCars;
        this.env = new RoadsEnv();
        this.threadManager = new ThreadManager(nThreads, 0, this, env);
    }

	public void setup() {

        List<CarAgent> cars = new LinkedList<>();
        int t0 = 0;
        int dt = 1;

		this.setupTimings(0, 1);

		this.setupEnvironment(env);

		Road road = env.createRoad(new P2d(0,300), new P2d(15000,300));

		for (int i = 0; i < numCars; i++) {

			String carId = "car-" + i;
			double initialPos = i*10;
			double carAcceleration = 1; //  + gen.nextDouble()/2;
			double carDeceleration = 0.3; //  + gen.nextDouble()/2;
			double carMaxSpeed = 7; // 4 + gen.nextDouble();

			CarAgent car = new CarAgentBasic(
					carId,
					env,
					road,
					initialPos,
					carAcceleration,
					carDeceleration,
					carMaxSpeed);
			this.addAgent(car);
            cars.add(car);

            /* no sync with wall-time */
		}
        threadManager.generateCars(cars, dt);
    }

    @Override
    public void run(int nSteps) { 
        this.threadManager.setSteps(nSteps);
        this.threadManager.startThreads(1);
    }
}
