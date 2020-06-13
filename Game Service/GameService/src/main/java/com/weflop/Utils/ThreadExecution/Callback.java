package com.weflop.Utils.ThreadExecution;

/**
 * Wrapper for a callback. Useful for wrapping methods
 * that we want to be called after executing a Runnable
 * in a thread.
 * 
 * @author abrevnov
 *
 */
public interface Callback {
    void callback();
}