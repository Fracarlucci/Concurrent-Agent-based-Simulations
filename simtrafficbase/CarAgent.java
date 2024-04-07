package pcd.ass01.simtrafficbase;

import java.util.Optional;

import pcd.ass01.simengineconcur.Barrier;
import pcd.ass01.simengineseq.*;

/**
 * Base class modeling the skeleton of an agent modeling a car in the traffic environment
 */
public abstract class CarAgent extends AbstractAgent {

  /* car model */
  protected double maxSpeed;
  protected double currentSpeed;
  protected double acceleration;
  protected double deceleration;

  /* percept and action retrieved and submitted at each step */
  protected CarPercept currentPercept;
  protected Optional<Action> selectedAction;
  private final int dt;
  private final Barrier actBarrier;
  private final Barrier stepBarrier;


  public CarAgent(String id, RoadsEnv env, Road road,
                  double initialPos,
                  double acc,
                  double dec,
                  double vmax,
                  int dt,
                  Barrier actBarrier,
                  Barrier stepBarrier) {
    super(id);
    this.acceleration = acc;
    this.deceleration = dec;
    this.maxSpeed = vmax;
    this.dt = dt;
    this.actBarrier = actBarrier;
    this.stepBarrier = stepBarrier;
    env.registerNewCar(this, road, initialPos);
  }

  /**
   * Basic behaviour of a car agent structured into a sense/decide/act structure
   */
//  public void step() {
//
//  }

  public void senseAndDecide(int dt) {
    AbstractEnvironment env = this.getEnv();
    currentPercept = (CarPercept) env.getCurrentPercepts(getAgentId());

    /* decide */
    selectedAction = Optional.empty();
    decide(dt);
  }

  public void act() {
      selectedAction.ifPresent(action -> this.getEnv().doAction(super.getAgentId(), action));
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
