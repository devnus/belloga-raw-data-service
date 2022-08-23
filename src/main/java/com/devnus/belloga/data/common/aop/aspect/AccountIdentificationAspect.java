package com.devnus.belloga.data.common.aop.aspect;

import com.devnus.belloga.data.common.aop.annotation.GetAccountIdentification;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


/**
 * @GetAccountIdentification 어노테이션에 대한 AOP 이다.
 * @GetAccountIdentification 파라미터에 따라 HttpServletRequest 헤더에서 식별자를 꺼내 파라미터 변수에 담아준다.
 * @author suhongkim
 */
@Aspect
@Component
public class AccountIdentificationAspect {
    @Around("execution(* *(.., @com.devnus.belloga.data.common.aop.annotation.GetAccountIdentification (*), ..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        // httpServletRequest 를 가져온다.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Class<?> clazz = methodSignature.getDeclaringType();

        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        // 파라미터의 개수만큼 반복문을 돌린다.
        for(int i = 0 ; i < method.getParameterCount(); i++) {
            if(method.getParameterAnnotations()[i].length < 1) continue; // 해당 파라미터에는 어노테이션이 없음

            // method.getParameterAnnotations()[i]를 통해 해당 파라미터에 붙은 모든 어노테이션을 가져온다.
            // 해당 파라미터의 첫번째 파라미터를 불러온다.
            // 즉 @GetAccountIdentification 는 다른 어노테이션과 같이 쓰면 안되고 단독으로 쓰여야함 // 해결하려면 2차원 반복문 돌려야
            Annotation annotation = method.getParameterAnnotations()[i][0];

            // @GetAccountIdentification과 일치할 경우 해당 arg index에 식별자를 담아주어야 한다.
            if(GetAccountIdentification.class.isInstance(annotation)) {
                // annotation을 GetAccountIdentification로 형변환해서 key를 꺼내온다.
                GetAccountIdentification getAccountIdentification = (GetAccountIdentification) annotation;
                args[i] = request.getHeader(getAccountIdentification.role().getKey());
            }
        }
        // 메서드 실행 전
        Object result = pjp.proceed(args);
        // 메서드 실행 후
        return result;
    }
}
