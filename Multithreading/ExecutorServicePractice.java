import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServicePractice {
    public static void main(String[] args) {
        ExecutorService servicePool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 5; i++) {
                servicePool.execute(new InnerExecutorServicePractice(i));
        }
    }

    static class InnerExecutorServicePractice implements Runnable {
    
        private int count;
        @Override
        public void run() {
            System.out.println("async work"+count);
        }
        InnerExecutorServicePractice(int i){
            this.count=i;
        }
    }
}
