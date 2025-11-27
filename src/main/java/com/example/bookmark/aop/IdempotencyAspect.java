package com.example.bookmark.aop;

import com.example.bookmark.entity.IdempotencyKey;
import com.example.bookmark.repository.IdempotencyKeyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.example.bookmark.aop.Idempotent)")
    public Object handleIdempotent(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String idempotencyKey = request.getHeader("Idempotency-Key");

        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return joinPoint.proceed();
        }

        Optional<IdempotencyKey> keyOptional = idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey);

        if (keyOptional.isPresent() && keyOptional.get().getResponseBody() != null) {
            log.info("Idempotent request detected. Returning cached response for key: {}", idempotencyKey);
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Class<?> returnType = signature.getMethod().getReturnType();
            return objectMapper.readValue(keyOptional.get().getResponseBody(), returnType);
        }

        // To prevent concurrent requests
        if (keyOptional.isEmpty()) {
            idempotencyKeyRepository.save(new IdempotencyKey(idempotencyKey));
        }

        Object result = joinPoint.proceed();

        IdempotencyKey keyToUpdate = idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey).orElseThrow();
        keyToUpdate.setResponseBody(objectMapper.writeValueAsString(result));
        idempotencyKeyRepository.save(keyToUpdate);

        return result;
    }
}
