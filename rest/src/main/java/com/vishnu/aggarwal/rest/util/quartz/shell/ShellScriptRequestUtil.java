package com.vishnu.aggarwal.rest.util.quartz.shell;

/*
Created by vishnu on 16/7/19 12:48 PM
*/

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.SHELL_SCRIPT_PATH;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.USER_DIR_PATH;
import static org.springframework.util.CollectionUtils.isEmpty;

@CommonsLog
public class ShellScriptRequestUtil implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        if (isEmpty(jobDataMap)) {
            throw new JobExecutionException("");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(jobDataMap.getString(SHELL_SCRIPT_PATH));
            processBuilder.directory(new File(jobDataMap.getString(USER_DIR_PATH)));

            Executors.newSingleThreadExecutor().submit(new StreamGobbler(processBuilder.start().getInputStream(), System.out::println));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @CommonsLog
    @AllArgsConstructor
    @NoArgsConstructor
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }
}
