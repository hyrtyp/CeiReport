package com.jingdong.app.mall.utils.thread;

import com.jingdong.app.mall.utils.IPriority;
import com.jingdong.app.mall.utils.PriorityCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

public class ThreadPool {

	protected boolean hasIdleThread;
	protected int initPoolSize;
	protected boolean initialized;
	protected int maxPoolSize;
	// 优先级任务
	protected PriorityQueue<IPriority> queue = new PriorityQueue<IPriority>();
	// 线程容器
	protected Vector<PooledThread> threads = new Vector<PooledThread>();

	public ThreadPool(int maxPoolSize, int initPoolSize) {
		this.maxPoolSize = maxPoolSize;
		this.initPoolSize = initPoolSize;
	}

	/**
	 * 弹出第一个优先级任务
	 * 
	 * @return
	 */
	private IPriority pollTasks() {
			IPriority localIPriority = this.queue.poll();
			return localIPriority;
	}

	/**
	 * 获取空闲的线程
	 * 
	 * @return
	 */
	public PooledThread getIdleThread() {
		while (true) {
			Iterator<PooledThread> iterator = threads.iterator();
			while (iterator.hasNext()) {
				PooledThread pooledthread = (PooledThread) iterator.next();
				if (!pooledthread.isRunning()) {
					return pooledthread;
				}
			}
			if (getPoolSize() < maxPoolSize) {
				PooledThread pooledthread = new PooledThread(this);
				pooledthread.start();
				threads.add(pooledthread);
				return pooledthread;
			}
			if (waitForIdleThread()) {
				continue;
			}
		}
	}

	/**
	 * 获取线程池的大小
	 * 
	 * @return
	 */
	public int getPoolSize() {
		return this.threads.size();
	}

	/**
	 * 初始化线程池内的线程，并且监听队列内时候有任务，随时通知给子线程
	 */
	public void init() {
		this.initialized = true;
		for (int i = 0;; ++i) {
			if (i >= this.initPoolSize) {
				new Thread(new Runnable() {

					public void run() {
						android.os.Process
								.setThreadPriority(android.os.Process.THREAD_PRIORITY_LOWEST);
						do {
							Collection collection = (Collection) pollTasks();
							if (collection != null) {
								PooledThread pooledThread = getIdleThread();
								pooledThread.putTasks(collection);
								pooledThread.startTasks();
							} else {
								try {
									synchronized (queue) {
										queue.wait();
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						} while (true);
					}
				}).start();
				return;
			} else {
				PooledThread localPooledThread = new PooledThread(this);
				localPooledThread.start();
				this.threads.add(localPooledThread);
			}
		}
	}

	/**
	 * 通知线程池有空余线程
	 */
	protected void notifyForIdleThread() {
		synchronized (this) {
			this.hasIdleThread = true;
			notify();
		}
	}

	/**
	 * 向队列内增加任务
	 * 
	 * @param paramRunnable
	 * @param paramInt
	 */
	public void offerTask(Runnable paramRunnable, int paramInt) {
		PriorityCollection localPriorityCollection = new PriorityCollection(
				paramInt);
		localPriorityCollection.add(paramRunnable);
		offerTasks(localPriorityCollection);
	}

	/**
	 * 提供任务优先队列
	 * 
	 * @param paramIPriority
	 */
	public void offerTasks(IPriority paramIPriority) {
		synchronized (queue) {
			queue.offer(paramIPriority);
			queue.notify();
		}
	}

	/**
	 * 把线程池扩展到最大
	 * 
	 * @param paramInt
	 */
	public void setMaxPoolSize(int paramInt) {
		this.maxPoolSize = paramInt;
		if (paramInt >= getPoolSize())
			return;
		setPoolSize(paramInt);
	}

	/**
	 * 设置线程池的大小
	 * 
	 * @param paramInt
	 */
	public void setPoolSize(int paramInt) {
		if (!this.initialized) {
			this.initPoolSize = paramInt;
		} else {
			if (paramInt > getPoolSize()) {
				for (int i = getPoolSize(); (i < paramInt)
						&& (i < this.maxPoolSize); ++i) {
					PooledThread localPooledThread = new PooledThread(this);
					localPooledThread.start();
					this.threads.add(localPooledThread);
				}
			}
			while (paramInt != getPoolSize()) {
				((PooledThread) this.threads.remove(0)).kill();
			}
		}
	}

	/**
	 * 等待获取空闲的线程
	 * 
	 * @return
	 */
	protected boolean waitForIdleThread() {
		boolean flag = false;
		this.hasIdleThread = false;
		while (true) {
			if (!hasIdleThread) {
				if (this.getPoolSize() >= maxPoolSize) {
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					flag = true;
					return flag;
				}
			} else {
				flag = true;
				return flag;
			}
		}

	}
}