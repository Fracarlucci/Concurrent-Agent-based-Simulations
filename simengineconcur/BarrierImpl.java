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
    // Waiting implementation without runnable.
    try {
      mutex.lock();
      nWait++;
      if (nWait < nThreads) {
        do {
          cond.await();
        } while (nPassed == 0); // Do-while useful for avoiding a double check on the two conditions.
      } else {
        nWait = 0; // Reset of the barrier.
        cond.signalAll();
      }
      nPassed = (nPassed + 1) % nThreads; // Reset of the numer of passed threads after all threads have passed the barrier.

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      mutex.unlock();
    }
  }
}
