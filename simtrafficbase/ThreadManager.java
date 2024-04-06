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
    private List<CarAgent> carAgents;
    private final Barrier stepBarrier;


    private final Barrier actBarrier;
    private final AbstractSimulation sim;
    private int nSteps = 0;


    public ThreadManager(int nTreads, AbstractSimulation sim) {
        this.nThreads = nTreads;
        this.stepBarrier = new BarrierImpl(nTreads);
        this.actBarrier = new BarrierImpl(nTreads);
        this.sim = sim;
        this.carAgents = new LinkedList<>();
    }

    public void generateCars(List<CarAgent> carAgents) {
        this.carAgents = carAgents;
    }

    public void startThreads() {
        carAgents.forEach(Thread::start);

        new Thread(() -> {
            int actualSteps = 0;

            while (actualSteps < this.nSteps) {
                this.stepBarrier.waitBefore();

//                this.sim.notifyNewStep(actualSteps, (AbstractAgent)carAgents, env);

//                t += this.dt;

                actualSteps++;
            }
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
