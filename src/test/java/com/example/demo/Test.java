package com.example.demo;

import WebSocket.AccountGenerator;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        AccountGenerator ag = new AccountGenerator();
        String acount = ag.getString();
        System.out.println(acount);

    }
}
