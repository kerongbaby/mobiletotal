package pygmy.core;

import java.util.Vector;

public class ThreadPool 
{
    private Vector threads = new Vector();
    private Vector queue = new Vector();

    public ThreadPool( int numberOfThreads ){
        for( int i = 0; i < numberOfThreads; i++ ){
            PooledThread thread = new PooledThread( "Pooled Thread " + i );
            thread.start();
            threads.addElement( thread );
        }
    }

    public void execute( Runnable runnable ){
        synchronized( queue ){
            queue.addElement( runnable );
            queue.notify();
        }
    }

    public void shutdown(){
        for( int i = 0; i < threads.size(); i++ ) 
        {
            PooledThread thread = (PooledThread) threads.elementAt( i );
            thread.interrupt();
            thread.close();
        }
        /*把所有租塞的线程激活*/
        synchronized(queue){
        	queue.notifyAll();
       }
        
        while(true){
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	for(int i = 0; i < threads.size(); i++){
        		PooledThread thread = (PooledThread) threads.elementAt( i );
        		if(!thread.isExited()){
        			continue;
        		}
        	}
        	break;
        }
    }

    protected class PooledThread extends Thread 
    {
    	private boolean running;
    	private byte state;
    	public PooledThread(String name){
            super(name);
            running = true;
            state = 0;
        }

        public void run(){
            try{
            	state = 1;
                while( running ){
                    waitForTask();
					if(!running){
						break;
					}
                    Runnable runnable = retrieveTask();
                    if( runnable != null ){
                        try{
                            runnable.run();
                        } catch( Exception e ){
                            //log.warn( "@threadpool@"+e.toString(), e );
                        }
                    }
                }
                state = -1;//Exited from the loop
            } catch( InterruptedException e ){
            }
        }

        private void waitForTask() throws InterruptedException {
            synchronized( queue ) 
            {
                if( queue.isEmpty() ){
                    queue.wait();
                }
            }
        }

        private Runnable retrieveTask(){
            Runnable runnable = null;
            synchronized( queue ){
                if( !queue.isEmpty() ){
                    runnable = (Runnable)queue.firstElement();
                    queue.removeElement(runnable);
                }
            }
            return runnable;
        }
        
        public void close(){
        	running = false;
        }
        
        public boolean isExited(){
        	return state < 0 ? true : false;
        }
    }
}
