package pcd.ass01.simtrafficexamples;

import pcd.ass01.simengineconcur.Barrier;
import pcd.ass01.simengineseq.AbstractSimulation;
import pcd.ass01.simtrafficbase.*;

import java.util.List;

/**
 *
 * Traffic Simulation about 2 cars moving on a single road, no traffic lights
 *
 */
public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

	private final ThreadManager threadManager;
	private final RoadsEnv env;

	public TrafficSimulationSingleRoadTwoCars(int nThreads) {
		super();
		this.env = new RoadsEnv();
        this.threadManager = new ThreadManager(nThreads, this, env);
    }

	public void setup() {

		final Barrier stepBarrier = threadManager.getStepBarrier();
		final Barrier actBarrier = threadManager.getStepBarrier();

		int t0 = 0;
		int dt = 1;

		this.setupTimings(t0, dt);

		this.setupEnvironment(env);
		Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));
		CarAgent car1 = new CarAgentBasic("car-1", env, r,0, 0.1, 0.2, 8, dt, actBarrier, stepBarrier);
		this.addAgent(car1);
		CarAgent car2 = new CarAgentBasic("car-2", env, r,100, 0.1, 0.1, 7, dt, actBarrier, stepBarrier);
		this.addAgent(car2);

		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(25);
		threadManager.generateCars(List.of(car1,car2));
	}

	@Override
	public void run(int nSteps) {
		this.threadManager.setSteps(nSteps);
//		super.run(nSteps);
		this.threadManager.startThreads(1);
	}

}
