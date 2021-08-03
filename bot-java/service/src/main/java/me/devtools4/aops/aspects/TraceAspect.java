package me.devtools4.aops.aspects;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import me.devtools4.aops.annotations.Trace;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.event.Level;

@Aspect
public class TraceAspect {

  @Around(Trace.TRACE_ANNOTATION)
  public Object around(ProceedingJoinPoint point) throws Throwable {
    MethodSignature sig = (MethodSignature) point.getSignature();
    Trace trace = sig.getMethod().getAnnotation(Trace.class);
    Level level = Level.valueOf(trace.level());
    Logger targetLogger = getLogger(point.getTarget().getClass());
    if (!isEnabled(targetLogger, level)) {
      return point.proceed();
    }

    log(targetLogger, level, "{} with args: {}", sig.toShortString(), (Object) point.getArgs());

    long start = System.currentTimeMillis();
    Object proceed = point.proceed();
    long executionTime = System.currentTimeMillis() - start;

    log(targetLogger, level, "{} executed in {} ms, result: [{}]", sig.toShortString(), executionTime, result(proceed));

    return proceed;
  }

  private static boolean isEnabled(Logger logger, Level level) {
    switch (level) {
      case TRACE:
        return logger.isTraceEnabled();
      case DEBUG:
        return logger.isDebugEnabled();
      case INFO:
        return logger.isInfoEnabled();
      case WARN:
        return logger.isWarnEnabled();
      case ERROR:
        return logger.isErrorEnabled();
      default:
        return false;
    }
  }

  private static void log(Logger logger, Level level, String format, Object... args) {
    switch (level) {
      case TRACE: {
        logger.trace(format, args);
        break;
      }
      case DEBUG: {
        logger.debug(format, args);
        break;
      }
      case INFO: {
        logger.info(format, args);
        break;
      }
      case WARN: {
        logger.warn(format, args);
        break;
      }
      case ERROR: {
        logger.error(format, args);
        break;
      }
      default:
    }
  }

  private static Object result(Object obj) {
    if (obj == null) {
      return "null";
    } else if (obj instanceof byte[]) {
      return "byte array length: " + ((byte[]) obj).length;
    } else if (obj instanceof Collection) {
      return "collection size: " + ((Collection) obj).size();
    } else if (obj.getClass().isArray()) {
      return "array length: " + ((Object[]) obj).length;
    } else {
      return obj;
    }
  }
}