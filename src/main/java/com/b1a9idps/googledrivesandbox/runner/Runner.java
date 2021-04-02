package com.b1a9idps.googledrivesandbox.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("start");
    }
}
