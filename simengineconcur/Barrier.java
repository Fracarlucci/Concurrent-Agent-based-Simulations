package pcd.ass01.simengineconcur;

import pcd.ass01.simengineseq.AbstractSimulation;

public interface Barrier {
  public void waitBefore(AbstractSimulation isStopped);

  void signalAll();
}
