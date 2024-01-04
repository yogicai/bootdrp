package com.bootdo.listenner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

/**
* @Author: Created by yogiCai on 2019/4/7/007
*/
@Slf4j
@Component
@Order(value = 2)
public class StartWebListener implements CommandLineRunner {

    @Value("${web.login.cmd}")
    private String loginCmd;

	@Override
	public void run(String... arg0) throws Exception {
		try {
		    if (loginCmd != null && StringUtils.contains(System.getProperty("os.name").toLowerCase(), "windows")) {
                Runtime run = Runtime.getRuntime();
                run.exec(loginCmd);
            }
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}