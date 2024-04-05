package pcd.ass01.simtrafficbase;

import java.util.Optional;
import java.util.concurrent.locks.Lock;

import pcd.ass01.simengineseq.*;

/**
 * Base class modeling the skeleton of an agent modeling a car in the traffic environment
 */
public abstract class CarAgent extends AbstractAgent {

  /* car model */
  protected double maxSpeed;
  private final Lock envLock;
  protected double currentSpeed;
  protected double acceleration;
  protected double deceleration;

  /* percept and action retrieved and submitted at each step */
  protected CarPercept currentPercept;
  protected Optional<Action> selectedAction;


  public CarAgent(String id, RoadsEnv env, Road road, Lock envLock,
                  double initialPos,
                  double acc,
                  double dec,
                  double vmax) {
    super(id);
    this.envLock = envLock;
    this.acceleration = acc;
    this.deceleration = dec;
    this.maxSpeed = vmax;
    env.registerNewCar(this, road, initialPos);
  }

  @Override
  public synchronized void run() {
    synchronized (envLock) {
    while (true) {
      try {
        // TODO aggiungere il while
        System.out.println("I'm waiting.. ID: " + envLock.toString());
        envLock.wait();

        System.out.println("I'm running.. ID: " + super.getAgentId());

//        step(super.getDt());


      } catch (InterruptedException e) {
        System.out.println("Tirata eccezione");
      }
    }
    }
  }

  /**
   * Basic behaviour of a car agent structured into a sense/decide/act structure
   */
  public synchronized void step(int dt) {

    /* sense */

    AbstractEnvironment env = this.getEnv();
    currentPercept = (CarPercept) env.getCurrentPercepts(getAgentId());

    /* decide */

    selectedAction = Optional.empty();

    decide(dt);

    /* act */

    if (selectedAction.isPresent()) {
      env.doAction(getAgentId(), selectedAction.get());
    }
  }

  /**
   * Base method to define the behaviour strategy of the car
   *
   * @param dt
   */
  protected abstract void decide(int dt);

  public double getCurrentSpeed() {
    return currentSpeed;
  }

  protected void log(String msg) {
    System.out.println("[CAR " + this.getAgentId() + "] " + msg);
  }


}
