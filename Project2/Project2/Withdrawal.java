/* Name: Anthony Pionessa
 * Course: CNT 4714 Summer 2020
 * Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 * Due Date: June 14, 2020
 */

package Project2;

import java.util.Random;

public class Withdrawal extends Thread {
	private int threadNum;
	private Random generator = new Random();
	private Buffer sharedLocation;

	public Withdrawal(Buffer shared, int num) {
		sharedLocation = shared;
		threadNum = num;
	}
	
	public void run() {
		try {
			// sleep for a few milliseconds after being started
			Thread.sleep(generator.nextInt(30) + 1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			// withdraw a random amount from 1 - 50
			sharedLocation.withdraw(generator.nextInt(50) + 1, threadNum);
			try {
				// sleep after withdraw for a few milliseconds
				Thread.sleep(generator.nextInt(3) + 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
