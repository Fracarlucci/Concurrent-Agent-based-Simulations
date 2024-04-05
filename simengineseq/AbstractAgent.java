package pcd.ass01.simengineseq;

import java.util.concurrent.locks.Lock;

/**
 * 
 * Base  class for defining types of agents taking part to the simulation
 * 
 */
public abstract class AbstractAgent extends Thread {
	
	private String myId;
	private AbstractEnvironment env;
	private int dt;
	
	/**
	 * Each agent has an identifier
	 * 
	 * @param id
	 */
	protected AbstractAgent(String id) {
		this.myId = id;
		this.dt = 0;
	}
	
	/**
	 * This method is called at the beginning of the simulation
	 * 
	 * @param env
	 */
	public synchronized void init(AbstractEnvironment env) {
		this.env = env;
		this.run();
	}
	
	/**
	 * This method is called at each step of the simulation
	 * 
	 * @param dt - logical time step
	 */
	abstract public void step(int dt);
	

	public synchronized String getAgentId() {
		return myId;
	}
	
	protected synchronized AbstractEnvironment getEnv() {
		return this.env;
	}

	protected synchronized void setDt(int dt) { this.dt = dt; }
	protected synchronized int getDt() { return this.dt; }
}
