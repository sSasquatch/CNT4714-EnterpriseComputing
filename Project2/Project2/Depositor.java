/* Name: Anthony Pionessa
 * Course: CNT 4714 Summer 2020
 * Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 * Due Date: June 14, 2020
 */

package Project2;

import java.util.Random;

public class Depositor extends Thread {
	private int threadNum;
	private Random generator = new Random();
	private Buffer sharedLocation;
	
	public Depositor(Buffer shared, int num) {
		sharedLocation = shared;
		threadNum = num;
	}

	public void run() {
		try {
			// sleep for a few milliseconds after being started
			Thread.sleep(generator.nextInt(30) + 1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			// deposit a random amount from 1 - 250
			sharedLocation.deposit(generator.nextInt(250) + 1, threadNum);
			try {
				// sleep after deposit for a few milliseconds
				Thread.sleep(generator.nextInt(30) + 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
