package com.olifarhaan.aspect;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Aspect
@Component
public class ExecutionTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeAspect.class);
    private final MeterRegistry meterRegistry;

    public ExecutionTimeAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(* com.olifarhaan.model.*.*(..)) || " +
            "execution(* com.olifarhaan.service.*.*(..)) || " +
            "execution(* com.olifarhaan.controller.*.*(..)) || " +
            "execution(* com.olifarhaan.repository.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Object result = joinPoint.proceed();
            long executionTime = TimeUnit.NANOSECONDS
                    .toMillis(sample.stop(meterRegistry.timer("method.execution.time", "method", methodName)));
            logger.info("{} executed in {} ms", methodName, executionTime);
            return result;
        } catch (Throwable throwable) {
            meterRegistry.counter("method.execution.error", "method", methodName).increment();
            throw throwable;
        }
    }
}
