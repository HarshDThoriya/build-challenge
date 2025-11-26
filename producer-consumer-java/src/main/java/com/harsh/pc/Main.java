package com.harsh.pc;

import com.harsh.pc.worker.RunnerBlockingQueue;
import com.harsh.pc.worker.RunnerWaitNotify;

/**
 * Entry point for Assignment 1.
 * Usage:
 *   --mode=queue  → run BlockingQueue version
 *   --mode=wait   → run wait/notify version
 */
public class Main {
    public static void main(String[] args) {
        String mode = "--mode=queue";
        if (args != null) {
            for (String a : args) if (a.startsWith("--mode=")) mode = a;
        }
        switch (mode) {
            case "--mode=wait" -> RunnerWaitNotify.runDemo();
            case "--mode=queue" -> RunnerBlockingQueue.runDemo();
            default -> throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}