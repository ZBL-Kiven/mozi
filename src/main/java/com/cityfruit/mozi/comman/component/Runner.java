package com.cityfruit.mozi.comman.component;

import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * @author tianyuheng
 * @date 2020/02/11
 */
@Slf4j
@Component
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        init();
    }

    private void init() {
        ScoreUtil.setListener(ScoreUtil::createScore);
        ScoreUtil.getMembers(true, memberMap -> false);
    }

}
