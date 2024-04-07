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
    private List<TrafficLight> trafficLights;
    private final Barrier stepBarrier;


    private final Barrier actBarrier;
    private final AbstractSimulation sim;
    private int nSteps = 0;
    private long currentWallTime;
    private int nCyclesPerSec = 0;
    private long totalTime=0;

    private int t;





    public ThreadManager(int nTreads, AbstractSimulation sim, RoadsEnv env) {
        this.nThreads = nTreads;
        this.stepBarrier = new BarrierImpl(nTreads + 1);
        this.actBarrier = new BarrierImpl(nTreads);
        this.sim = sim;
        this.carAgents = new LinkedList<>();
        this.env = env;
    }

    public void generateCars(List<AbstractAgent> carAgents) {
        this.carAgents = carAgents;
    }

    public void generateTrafficLight(TrafficLight trafficLight) {
        this.trafficLights.add(trafficLight);
    }
    public void setupStartTiming(final int t) {
        this.t = t;
    }

    public void startThreads(int dt) {
        carAgents.forEach(ca -> {
            ca.init(env);
            ca.start();
        });

        if(trafficLights != null) {
            trafficLights.forEach(tl -> {
                tl.init(this.env);
                tl.start();
            });
        }


        new Thread(() -> {
            int actualSteps = 0;
            long startWallTime = System.currentTimeMillis();
            int t = 0;
            long timePerStep = 0;
            long startStepTime = 0;

            System.out.println(nSteps);
            while (actualSteps < this.nSteps) {
                this.stepBarrier.waitBefore();

                if(startStepTime!=0){
                    timePerStep += System.currentTimeMillis() - startStepTime;
                }
                t += dt;
                currentWallTime = System.currentTimeMillis();
                this.sim.notifyNewStep(t, carAgents, env);

                if(nCyclesPerSec >0){
                    sim.syncWithWallTime(currentWallTime);
                }
                actualSteps++;
                startStepTime = System.currentTimeMillis();
                System.out.println("Steps: " + actualSteps);
            }
//          this.stepBarrier.waitBefore(() -> this.sim.stop());
            System.out.println("Finish: " + actualSteps);
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

    public void setnCyclesPerSec(int nCyclesPerSec) {
        this.nCyclesPerSec = nCyclesPerSec;
    }
}
