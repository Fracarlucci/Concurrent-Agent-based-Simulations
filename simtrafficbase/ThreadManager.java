package pcd.ass01.simtrafficbase;

import pcd.ass01.simengineconcur.Barrier;
import pcd.ass01.simengineconcur.BarrierImpl;
import pcd.ass01.simengineseq.AbstractAgent;
import pcd.ass01.simengineseq.AbstractSimulation;

import java.util.LinkedList;
import java.util.List;

public class ThreadManager {

    private final int nThreads;
    private RoadsEnv env;
    private List<AbstractAgent> carAgents;
    private final Barrier stepBarrier;


    private final Barrier actBarrier;
    private final AbstractSimulation sim;
    private int nSteps = 0;
    private long currentWallTime;
    private int nStepsPerSec = 0;
    private long totalTime=0;





    public ThreadManager(int nTreads, AbstractSimulation sim, RoadsEnv env) {
        this.nThreads = nTreads;
        this.stepBarrier = new BarrierImpl(nTreads);
        this.actBarrier = new BarrierImpl(nTreads);
        this.sim = sim;
        this.carAgents = new LinkedList<>();
        this.env = env;
    }

    public void generateCars(List<AbstractAgent> carAgents) {
        this.carAgents = carAgents;
    }

    public void startThreads(int dt) {
        carAgents.forEach(ca -> {
            ca.init(env);
            ca.start();
        });

        new Thread(() -> {
            int actualSteps = 0;
            long startWallTime = System.currentTimeMillis();
            int t = 0;
            long timePerStep = 0;
            long startStepTime = 0;

            while (actualSteps < this.nSteps) {
                this.stepBarrier.waitBefore();

                if(startStepTime!=0){
                    timePerStep += System.currentTimeMillis() - startStepTime;
                }
                t += dt;
                currentWallTime = System.currentTimeMillis();
                this.sim.notifyNewStep(t, carAgents, env);

                if(nStepsPerSec>0){
                    sim.syncWithWallTime();
                }
                actualSteps++;
                startStepTime = System.currentTimeMillis();
            }

            // Exited the loop, the simulation must stop, so the barrier waits that all cars and lights have stopped and after execute the runnable.
//            this.stepBarrier.waitBefore(() -> this.sim.stop());

            timePerStep += System.currentTimeMillis() - startStepTime;
            totalTime = System.currentTimeMillis() - startWallTime;
        }).start();
    }

    public void setSteps(int nSteps) {
        this.nSteps = nSteps;
    }
    public Barrier getStepBarrier() {
        return stepBarrier;
    }

    public Barrier getActBarrier() {
        return actBarrier;
    }
}
