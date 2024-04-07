package pcd.ass01.simengineconcur;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierImpl implements Barrier {

  private final int nThreads;
  private ReentrantLock mutex;
  private Condition cond;
  private int nWait = 0; 
  private int nPassed = 0;


  public BarrierImpl(int nThreads) {
    this.nThreads = nThreads;
    this.mutex = new ReentrantLock();
    this.cond = mutex.newCondition();
  }

  @Override
  public void waitBefore() {
    try {
      mutex.lock();
      nWait++;
      if (nWait < nThreads) {
        do {
          cond.await();
        } while (nPassed == 0);
      } else {
        nWait = 0; // Reset of the barrier.
        cond.signalAll();
      }
      nPassed = (nPassed + 1) % nThreads;

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      mutex.unlock();
    }
  }
}
