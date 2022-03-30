/* Name: Anthony Pionessa
 * Course: CNT 4714 Summer 2020
 * Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 * Due Date: June 14, 2020
 */

package Project2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
	private ReentrantLock lock = new ReentrantLock();
	private Condition canWithdraw = lock.newCondition();
	private int withdrawResult, depositResult, currentBalance = 0;

	public void deposit(int depositAmount, int threadNum) {
		// lock the buffer
		lock.lock();
		try {
			// update the balance for deposit
			depositResult = Main.updateBalance(depositAmount, 1, threadNum);
			// signal all of the awaiting withdraw threads
			canWithdraw.signalAll();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			// unlock the buffer
			lock.unlock();
		}
	}
	
	public void withdraw(int withdrawAmount, int threadNum) {
		// lock the buffer
		lock.lock();
		try {
			// update the balance for withdraw
			withdrawResult = Main.updateBalance(-withdrawAmount, 0, threadNum);
			// if the withdraw failed, make the thread wait until a deposit
			if (withdrawResult == -1)
				canWithdraw.awaitUninterruptibly();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//unlock the buffer
			lock.unlock();
		}
	}
}
