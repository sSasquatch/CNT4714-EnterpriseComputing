/* Name: 
 * Course: CNT 4714 Summer 2020
 * Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 * Due Date: June 14, 2020
 */

package Project2;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private static int balance = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService application = Executors.newFixedThreadPool(14);
		Random generator = new Random();
		int depositID = 1, withdrawID = 1;
		
		// create Buffer
		Buffer sharedLocation = new Buffer();
		System.out.println("    Deposit Threads    \t\t   Withdrawal Threads  \t\t        Balance");
		System.out.println("-----------------------\t\t-----------------------\t\t-----------------------");
		
		// try to start 5 depositors and 9 withdrawals
		try {
			// loop for the total number of threads
			// generator a random number between 0 - 1
			// if it is 0, start a deposit thread
			// if it is 1, start a withdraw thread
			// if either are the result but the number of threads for that type are max
			// start the opposite instead (deposit -> withdraw and reverse
			for (int i = 0; i < 14; i++) {
				if (generator.nextInt(1) == 0) {
					if (depositID < 6) {
						// start deposit thread
						application.execute(new Depositor(sharedLocation, depositID));
						depositID++;
					}
					else {
						// start withdraw thread
						application.execute(new Withdrawal(sharedLocation, withdrawID));
						withdrawID++;
					}
				}
				else {
					if (withdrawID < 10) {
						// start withdraw thread
						application.execute(new Withdrawal(sharedLocation, withdrawID));
						withdrawID++;
					}
					else {
						// start deposit thread
						application.execute(new Depositor(sharedLocation, depositID));
						depositID++;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// shutdown application
		application.shutdown();
	}
	
	public static int updateBalance(int amount, int operationType, int threadNum) {
		// check if the withdraw is greater than the balance
		if ((operationType == 0) && (-amount > balance)) {
			System.out.println("                       \t\tThread W" + threadNum + " withdraws $" + -amount + "\t\t(***) Withdrawal - Blocked - Insufficient Funds!!!");			
			return -1;
		}
		
		// change the balance for after the operation
		balance += amount;
		
		// print out the operation
		if (operationType == 1) {
			System.out.println("Thread D" + threadNum + " deposits $" + amount + "\t\t                        \t(+) Balance is $" + balance);
			return 1;
		}
		else if (operationType == 0) {
			System.out.println("                       \t\tThread W" + threadNum + " withdraws $" + -amount + "\t\t(-) Balance is $" + balance);
			return 0;
		}
		
		return 2;
	}
}
