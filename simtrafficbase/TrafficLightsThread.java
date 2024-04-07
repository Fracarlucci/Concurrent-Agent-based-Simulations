package pcd.ass01.simtrafficbase;

import pcd.ass01.simengineconcur.Barrier;
import pcd.ass01.simengineseq.AbstractEnvironment;
import pcd.ass01.simengineseq.AbstractSimulation;
import java.util.List;

public class TrafficLightsThread extends Thread {

    private final Barrier actBarrier;
    private final Barrier stepBarrier;
    private final List<TrafficLight> trafficLights;
    private final AbstractSimulation sim;
    private final int dt;


    public TrafficLightsThread(Barrier actBarrier, Barrier stepBarrier, List<TrafficLight> trafficLights, AbstractSimulation sim, int dt) {
        this.actBarrier = actBarrier;
        this.stepBarrier = stepBarrier;
        this.trafficLights = trafficLights;
        this.sim = sim;
        this.dt = dt;
    }

    public void addTrafficLight(TrafficLight tl) {
        this.trafficLights.add(tl);
    }

    public void initTrafficLights() {
        this.trafficLights.forEach(TrafficLight::init);
    }
    
    public void run() {
        while(true) {
            stepBarrier.waitBefore(); 
//            if(sim.isStopped())
//                break;
            this.step();
        }
    }

    public void step() {
        this.trafficLights.forEach(tl -> tl.step(this.dt));
        stepBarrier.waitBefore();
        actBarrier.waitBefore();
    }
}
