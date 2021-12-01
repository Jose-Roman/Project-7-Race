//Jose Roman
//Project 7 Race Conditions
//Sec 11.3
//Dec 16 2021

import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PVoperations
{
    static int n;
    static int[] buffer = new int[n];
    static int k;
    static int t;
    //semaphores used to recreate P and V operations
    static Semaphore P = new Semaphore(n);
    static Semaphore V = new Semaphore(0);
    //e and f shown in zybooks 11.3
    static int e = n;
    static int f = 0;
    static int next_in = 0;
    static int next_out = 0;

    public static void main(String[] args) throws InterruptedException
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter Buffer Size: ");
        n = keyboard.nextInt();
        buffer = new int[n];
        System.out.println("Enter K Range: ");
        k = keyboard.nextInt();
        System.out.println("Enter T (Sleep Time) Range: ");
        t = keyboard.nextInt();


        //threads so producer and consumer can run at same time
        Thread p = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //producer thread
                try
                {
                    producer();
                }
                catch (InterruptedException error)
                {
                    error.printStackTrace();
                }
            }
        });
        Thread c = new Thread(new Runnable()
        {
            //consumer thread
            @Override
            public void run()
            {
                try {
                    consumer();
                } catch (InterruptedException error) {
                    error.printStackTrace();
                }
            }
        });
        p.start();      //start producer
        c.start();      //start consumer
        p.join();       //join producer
        c.join();       //join consumer

    }

    //producer
    public static void producer() throws InterruptedException
    {
        while(true)
        {
            int k1 = ThreadLocalRandom.current().nextInt(0,k);
            for(int i = 0; i < k1;i++)
            {
                P.acquire(e);                    //P operation
                buffer[(next_in + i) % n] += 1;
                V.release(f);                    //V operation

            }
            next_in += (k1 % n);

            int t1 = ThreadLocalRandom.current().nextInt(0, t);
            System.out.println("Producer: Sleep for " + t1 + " seconds");
            TimeUnit.SECONDS.sleep(t1);     //sleep

        }
    }


    //consumer
    public static void consumer() throws InterruptedException {
        while(true){
        int t2 = ThreadLocalRandom.current().nextInt(0, t );
        System.out.println("Consumer: Sleep for " + t2 + " seconds");
        TimeUnit.SECONDS.sleep(t2);     //sleep
        int k2 = ThreadLocalRandom.current().nextInt(0, k);
        for(int i = 0; i < k2;i++)
        {

          P.acquire(f);             //P operation
            int data = buffer[(next_out + i) % n];
            if(data > 1){
                System.out.println("Race Condition");
                System.exit(1);
            }

          V.release(e);            //V operation

          next_out += (k2 % n);
          System.out.println("No race condition");
        }
       
    }
    }
}