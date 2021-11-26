
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
public class RaceConditions
{
    static int n;
    static int[] buffer;
    static int k;
    static int t;

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
                try
                {
                    consumer();
                }
                catch (InterruptedException error)
                {
                    error.printStackTrace();
                }
            }
        });
            p.start();      //start producer
            c.start();      //start consumer
            p.join();       //join producer
            c.join();       //join consumer

    }
    public static void producer() throws InterruptedException {
        //producer

        while(true)
        {
            int k1 = ThreadLocalRandom.current().nextInt(0, k);
            for(int i = 0; i < k1;i++)
            {
                buffer[(next_in + k1) % n] = 1;
            }
            next_in += (k1 % n);
            int t1 = ThreadLocalRandom.current().nextInt(0, t);
           System.out.println("Producer: Sleep for " + t1 + " seconds");
            TimeUnit.SECONDS.sleep(t1);
        }
    }

    public static void consumer() throws InterruptedException {
        //consumer
        while(true){
            int t2 = ThreadLocalRandom.current().nextInt(0, t );
            System.out.println("Consumer: Sleep for " + t2 + " seconds");
            TimeUnit.SECONDS.sleep(t2);
            int k2 = ThreadLocalRandom.current().nextInt(0, k);
            for(int i = 0; i < k2;i++)
            {
                int data = buffer[(next_out + k2) % n];
                //System.out.println("Data: " + data);
                if(data >= 1){
                    System.out.println("RACE CONDITION");
                    //System.out.println(Arrays.toString(buffer));
                    System.exit(1);
                }

                next_out += (k2 % n);
                System.out.println("No race condition");
            }

        }
    }



}
