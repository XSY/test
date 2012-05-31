package wonders.t;

import java.util.Random;
import org.zeromq.ZMQ;

//
//   Task ventilator in Java
//   Binds PUSH socket to tcp://localhost:5557
//   Sends batch of tasks to workers via that socket
//
//   Nicola Peduzzi <thenikso@gmail.com>
//

public class taskvent {

	public static void main(String[] args) throws Exception {
		ZMQ.Context context=ZMQ.context(1);
		ZMQ.Socket sender=context.socket(ZMQ.PUSH);
		sender.bind("tcp://*:5557");
		
		ZMQ.Socket sink=context.socket(ZMQ.PUSH);
		sink.connect("tcp://localhost:5558");
		
		System.out.println("Please Enter when workers are ready: ");
		System.in.read();
		System.out.println("Sending tasks to workers!\n ");
		
		sink.send("0\u0000".getBytes(), 0);
		
		Random srandom=new Random(System.currentTimeMillis());
		
		
		int task_nbr;
		int total_msec=0;
		for (task_nbr = 0; task_nbr < 100; task_nbr++) {
			int workload;
			workload=srandom.nextInt(100)+1;
			total_msec+=workload;
			System.out.println(workload+".");
			String string=String.format("%d\u0000", workload);
			
			//while running here, cannot continue but keep here all the time
			//sender's socket address is not right 
			//sender.send(string.getBytes(),0); 
			sink.send(string.getBytes(),0);
			
		}
		System.out.println("Total expected costs:"+total_msec+"msec");
		
		sink.close();
		sender.close();
		context.term();
		Thread.sleep(1000);
		
	}

}


