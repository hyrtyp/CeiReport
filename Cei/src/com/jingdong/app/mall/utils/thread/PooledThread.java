package com.jingdong.app.mall.utils.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PooledThread extends Thread {
	private static ThreadPool sFirstPool = new ThreadPool(
			Integer.parseInt("1"), Integer.parseInt("1"));
	private static ThreadPool sSecondPool = new ThreadPool(
			Integer.parseInt("1"), Integer.parseInt("1"));
	public static ThreadPool sThirdPool = new ThreadPool(
			Integer.parseInt("1"), Integer.parseInt("1"));
	protected boolean killed = false;
	protected boolean paused = false;
	private ThreadPool pool;
	protected boolean running = false;
	protected boolean stopped = false;
	protected List<Runnable> tasks = new ArrayList<Runnable>();

	static {
		sThirdPool.init();
		sSecondPool.init();
		sFirstPool.init();
	}

	public PooledThread(ThreadPool paramThreadPool) {
		this.pool = paramThreadPool;
	}

	public static ThreadPool getFirstThreadPool() {
		return sFirstPool;
	}

	public static ThreadPool getSecondThreadPool() {
		return sSecondPool;
	}

	public static ThreadPool getThirdThreadPool() {
		return sThirdPool;
	}

	public boolean isRunning() {
		return this.running;
	}

	/**
	 * 清除掉当前线程
	 */
	public void kill() {
		if (!this.running)
			interrupt();
		else
			this.killed = true;
	}

	/**
	 * 物理上清除当前线程
	 */
	public void killSync() {
		kill();
		while (true) {
			if (!isAlive())
				return;
			try {
				sleep(5L);
			} catch (InterruptedException localInterruptedException) {
			}
		}
	}

	/**
	 * 暂停当前线程
	 */
	public void pauseTasks() {
		this.paused = true;
	}

	/**
	 * 物理上暂停当前线程
	 */
	public void pauseTasksSync() {
		pauseTasks();
		while (true) {
			if (!isRunning())
				return;
			try {
				sleep(5L);
			} catch (InterruptedException localInterruptedException) {
			}
		}
	}

	/**
	 * 弹出第一个任务
	 * 
	 * @return
	 */
	protected Runnable popTask() {
		if (this.tasks.size() > 0)
			return (Runnable) this.tasks.remove(0);
		else
			return null;
	}

	/**
	 * 加入任务
	 * 
	 * @param paramRunnable
	 */
	public void putTask(Runnable paramRunnable) {
		this.tasks.add(paramRunnable);
	}

	/**
	 * 加入任务集合
	 * 
	 * @param paramRunnable
	 */
	public void putTasks(Collection paramCollection) {
			this.tasks.addAll(paramCollection);
	}

	/**
	 * 开始唤醒线程，执行任务
	 */
	public void startTasks() {
		synchronized (this) {
			this.running = true;
			notify();
		}

	}

	/**
	 * 停止任务
	 */
	public void stopTasks() {
		this.stopped = true;
	}

	/**
	 * 从物理上停止任务
	 */
	public void stopTasksSync() {
		stopTasks();
		while (true) {
			if (!isRunning())
				return;
			try {
				sleep(5L);
			} catch (InterruptedException localInterruptedException) {
				
			}
		}
	}

	/**
	 * 执行任务集合
	 */
	public void run() {
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_LOWEST);
		while (true) {
			if (running && tasks.size() != 0) {
				Runnable runnable = popTask();
				if (runnable != null) {
					runnable.run();
					if (!stopped) {
						if (paused)
							paused = false;
						if (tasks.size() <= 0) {
							running = false;
							if (killed) {
								killed = false;
							}
						}
					} else {
						stopped = false;
						tasks.clear();
						if (paused)
							paused = false;
						running = false;
						if (killed) {
							killed = false;
						}
					}
				} else {
					running = false;
					if (killed) {
						killed = false;
					}
				}
			} else {
				pool.notifyForIdleThread();
				try { 
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}