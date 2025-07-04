package com.equitrack.dao;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * MyLock provides a shared ReentrantReadWriteLock used to synchronize access to
 * the database across all DAO classes
 */
public class MyLock {
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * The read lock for safely performing concurrent read operations on the
	 * database
	 */
	public static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

	/**
	 * The write lock for performing exclusive write operations on the database
	 */
	public static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
}
