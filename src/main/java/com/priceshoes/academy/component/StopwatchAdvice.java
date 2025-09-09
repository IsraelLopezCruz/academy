package com.priceshoes.academy.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.perf4j.LoggingStopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@AllArgsConstructor
public class StopwatchAdvice {
    private final StopwatchConfig stopwatchConfig;

    @Around(value = "@within(stopwatch) || @annotation(stopwatch)", argNames = "joinPoint,stopwatch")
    public Object advice(ProceedingJoinPoint joinPoint, Stopwatch stopwatch) throws Throwable {
        if (stopwatchConfig.isEnabled() && Math.random() <= (double) stopwatchConfig.getPercentage() / 100 ) {
            return joinPoint.proceed();
        }
        LoggingStopWatch timer = new Slf4JStopWatch();

        //if we're not going to end up logging the stop watch, just run the wrapped method
        if ((!timer.isLogging())) {
            return joinPoint.proceed();
        }

        timer.start();

        Throwable exceptionThrown = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            exceptionThrown = t;
            throw exceptionThrown;
        } finally {

            String tag = getStopWatchTag(stopwatch, joinPoint);
            tag = isSuccessRequest(exceptionThrown) ? tag + ".success" : tag + ".failure";

            timer.stop(tag);
        }

    }

    String getStopWatchTag(Stopwatch stopWatch, ProceedingJoinPoint joinPoint) {

        Stopwatch stopwatch = Optional.ofNullable(stopWatch)
                .orElse(joinPoint.getTarget().getClass().getAnnotation(Stopwatch.class));

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String prefix = stopwatch.prefix();

        return prefix+"."+className+"."+methodName;
    }

    boolean isSuccessRequest(Throwable exceptionThrown) {
        return exceptionThrown == null;
    }

    @Configuration
    @Getter
    public static class StopwatchConfig {
        private final boolean enabled;
        private final int percentage;
        public StopwatchConfig(
                @Value("${stopwatch.enabled:false}") boolean enabled,
                @Value("${stopwatch.percentage:50}") int percentage
        ) {
            this.enabled = enabled;
            this.percentage = percentage;
        }

    }
}
