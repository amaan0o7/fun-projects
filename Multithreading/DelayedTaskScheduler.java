import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DelayedTaskScheduler {
    private final PriorityBlockingQueue<DelayedTask> queue;
    private    final ReentrantLock lock;
     private   final Condition available;

     public DelayedTaskScheduler() {
        this.queue = new PriorityBlockingQueue();
        this.lock = new ReentrantLock();
        this.available = this.lock.newCondition();
    }

    public class DelayedTask extends FutureTask implements Delayed {
        private final long startTime;
        private final Runnable task;
    
        public DelayedTask(Runnable task, long delayTime) {
            super(task, null);  // null is the return value for the runnable tasks
            this.task = task;
            this.startTime = System.currentTimeMillis() + delayTime;
        }
    
        @Override
        public long getDelay(TimeUnit unit) {
            long diff = this.startTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }
    
        @Override
        public int compareTo(DelayedTask o) {
            return Long.compare(this.getDelay(), ((DelayedTask) o).getDelay());
        }
    }

    public boolean put(DelayedTask task)throws InterruptedException {
        lock.lock();
        try{
            this.queue.offer(task);
            if(queue.peek()==task){
                available.signal();
            }
            return true;
        }
        finally{
            lock.unlock();
        }
    }

    public DelayedTask get() throws InterruptedException {
        this.lock.lock();
        try {
            while (true) {
                DelayedTask peekTask = this.queue.peek();
                if (peekTask == null) {
                    // no elemnets; wait!
                    available.await();
                } else {
                    long delay = peekTask.getDelay(TimeUnit.NANOSECONDS);
                    if (delay <= 0) {
                        System.out.println("executed in::"+System.currentTimeMillis());
                        
                        return this.queue.poll();}
                    available.awaitNanos(delay);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        DelayedTaskScheduler scheduler = new DelayedTaskScheduler();

        // Create and add a DelayedTask
        DelayedTaskScheduler.DelayedTask task = scheduler.new DelayedTask(() -> {
            System.out.println("Task executed -- 1");
        }, 1000); 
        try {
            System.out.println("current time:: "+System.currentTimeMillis());
            // Put the task in the scheduler
            scheduler.put(task);

            // Sleep for a while to simulate some delay
            Thread.sleep(2000);

            // Get the task from the scheduler
            DelayedTaskScheduler.DelayedTask retrievedTask = scheduler.get();

            // Execute the retrieved task
            if (retrievedTask != null) {
                // Execute the task
                new Thread(retrievedTask).start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
