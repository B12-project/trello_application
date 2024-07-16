package b12.trello.global.distributedLock;

import jakarta.transaction.Transactional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class AopForTransaction {

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
