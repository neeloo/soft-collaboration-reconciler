package com.Neeloo.soft_collaboration_reconciler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class SoftCollaborationReconcilerApplication {

	public static void main(String[] args) {

        SpringApplication.run(SoftCollaborationReconcilerApplication.class, args);
        //System.out.println("Hello");
	}

}
