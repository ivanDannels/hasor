package org.hasor.test.mvc.plugin.safety.support;
import java.lang.reflect.Method;
import net.hasor.core.ApiBinder;
import net.hasor.core.ModuleSettings;
import net.hasor.core.context.AnnoModule;
import net.hasor.core.module.AbstractHasorModule;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hasor.test.mvc.plugin.safety.Power;
import org.hasor.test.mvc.plugin.safety.SafetyContext;
import com.google.inject.matcher.AbstractMatcher;
/**
 * 权限服务。
 * @version : 2013-4-8
 * @author 赵永春 (zyc@hasor.net)
 */
@AnnoModule(displayName = "SafetyPlatformListener", description = "org.test.plugin.safety软件包功能支持。")
public class SafetyPlatformListener extends AbstractHasorModule {
    private SafetyContext safetyContext = null;
    public void configuration(ModuleSettings info) {}
    public void init(ApiBinder apiBinder) {
        //1.挂载Aop
        apiBinder.getGuiceBinder().bindInterceptor(new ClassPowerMatcher(), new MethodPowerMatcher(), new PowerInterceptor());
        //
        apiBinder.getGuiceBinder().bind(SafetyContext.class).asEagerSingleton();
    }
    /*-------------------------------------------------------------------------------------*/
    /*负责检测类是否匹配。规则：只要类型或方法上标记了@Power。*/
    private class ClassPowerMatcher extends AbstractMatcher<Class<?>> {
        public boolean matches(Class<?> matcherType) {
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            Method[] m1s = matcherType.getMethods();
            Method[] m2s = matcherType.getDeclaredMethods();
            for (Method m1 : m1s) {
                if (m1.isAnnotationPresent(Power.class) == true)
                    return true;
            }
            for (Method m2 : m2s) {
                if (m2.isAnnotationPresent(Power.class) == true)
                    return true;
            }
            return false;
        }
    }
    /*负责检测方法是否匹配。规则：方法或方法所处类上标记了@Power。*/
    private class MethodPowerMatcher extends AbstractMatcher<Method> {
        public boolean matches(Method matcherType) {
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            if (matcherType.getDeclaringClass().isAnnotationPresent(Power.class) == true)
                return true;
            return false;
        }
    }
    /*拦截器*/
    private class PowerInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            //1.获取权限数据
            Method targetMethod = invocation.getMethod();
            Power cacheAnno = targetMethod.getAnnotation(Power.class);
            if (cacheAnno == null)
                cacheAnno = targetMethod.getDeclaringClass().getAnnotation(Power.class);
            if (cacheAnno == null)
                return invocation.proceed();
            //2.获取Key
            if (safetyContext.checkPower(cacheAnno.value()) == false)
                throw new Exception("no Power");
            return invocation.proceed();
        }
    }
}