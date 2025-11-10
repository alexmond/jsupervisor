package org.alexmond.jsupervisor.utility;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.SuppressWarnings;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
//
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LogbackLogger {

    /**
     * Creates a dynamic RollingFileAppender for a process (Java 17+ compatible).
     * @param processName Unique ID (e.g., "testapp1") for logger/appender name.
     * @param logDir Base directory for logs.
     * @param maxSize e.g., "10MB".
     * @param maxHistory Days to keep backups.
     * @return Attached Logger for this appender.
     */
    @SuppressWarnings("unchecked")  // For generics in RollingFileAppender<Object>
    public Logger createDynamicAppender(String processName, Path logDir, String maxSize, int maxHistory) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Create appender
        RollingFileAppender<Object> appender = new RollingFileAppender<>();
        appender.setContext(context);
        appender.setName(processName + "-rolling");  // Unique name to avoid conflicts

        // Set file path (e.g., logs/testapp1-stderr.log)
        String fileName = logDir.resolve(processName + "-stderr.log").toString();
        appender.setFile(fileName);

        // Rolling policy: Time + Size based (daily rollover or at maxSize)
        SizeAndTimeBasedRollingPolicy<Object> policy = new SizeAndTimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setParent(appender);
        policy.setFileNamePattern(logDir.resolve(processName + "-stderr.%d{yyyy-MM-dd}.%i.log.gz").toString());
        policy.setMaxFileSize(FileSize.valueOf(maxSize));  // e.g., "10MB"
        policy.setMaxHistory(maxHistory);  // Keep 7 days
        policy.setTotalSizeCap("1GB");  // Fixed cap (pure Java 17; adjust as needed)
        policy.start();  // Start policy first

        // Triggering policy (built into SizeAndTimeBasedRollingPolicy)
        appender.setRollingPolicy(policy);

        // Encoder: Pattern for output
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %processName - %msg%n");  // Add processName
        encoder.start();  // Start encoder
        appender.setEncoder(encoder);

        // Start appender (after sub-components)
        appender.start();

        // Create/attach logger
        Logger logger = context.getLogger(processName + "-stderr");  // Dynamic logger name
        logger.addAppender(appender);
        logger.setLevel(Level.ERROR);  // Only errors for stderr

        // Optional: Detach on shutdown (call in @PreDestroy)
        // context.detachAppender(logger, appender);

        return logger;
    }

    public void startProcessWithDynamicLogging(ProcessBuilder pb, String processName, Path logDir) {
        Process process = pb.start();

        // Create dynamic appender/logger
        Logger stderrLogger = createDynamicAppender(processName, logDir, "10MB", 7);

        // Consume stderr and log to dynamic appender
        CompletableFuture<Void> stderrFuture = CompletableFuture.runAsync(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stderrLogger.error(line);  // Routes to rolling appender
                }
            } catch (Exception e) {
                stderrLogger.error("Stream read error: " + e.getMessage());
            }
        }).exceptionally(throwable -> {
            if (throwable instanceof CompletionException) {
                throwable = throwable.getCause();
            }
            stderrLogger.error("Async stderr consumption failed: " + throwable.getMessage(), throwable);
            return null;
        });

        // Optional: Wait for completion on process end
        // stderrFuture.join();  // But don't block supervisor thread

        // Similar for stdout...
    }
}
