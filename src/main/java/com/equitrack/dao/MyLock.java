package com.equitrack.dao;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyLock {
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	public static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
	public static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
}
