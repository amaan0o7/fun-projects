public class thread {
//     ThreadLocal in Java provides thread-local variables. 
// It allows you to store data that is accessible only within the thread it's associated with. 
// Each thread accessing a ThreadLocal has its own independently initialized copy of the variable. 
// This means that changes made by one thread to its copy of the variable don’t affect the copies of the same variable in other threads.
// Here's a basic overview of ThreadLocal:

// Thread Scope: ThreadLocal provides a way to create variables that are local to a thread. It's often used to maintain context or state information specific to a thread, such as user sessions or database connections.

// Independently Managed Copies: Each thread has its own copy of the ThreadLocal variable. When you access the ThreadLocal variable from different threads, you'll get different instances of the variable, each specific to the thread that's accessing it.

// Initialization: The initial value for a ThreadLocal variable is set using initialValue() or by overriding the withInitial() method with a Supplier.

// Avoiding Thread Interference: Since each thread has its own copy of the variable, it helps prevent thread interference or synchronization issues when multiple threads are accessing the same ThreadLocal.

// Memory Management: ThreadLocal variables are associated with threads and are subject to garbage collection when the thread itself is garbage collected.
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        
                threadLocal.set("Hello, Main ThreadLocal!");
                
                Runnable runnable = () -> {
                    threadLocal.set("Modified in another thread");
                    System.out.println(threadLocal.get());
                };
        
                Thread thread = new Thread(runnable);
                thread.start();
        
                System.out.println(threadLocal.get()); // Output: Hello, ThreadLocal!
           
        
    }
}