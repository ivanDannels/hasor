/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.servlet.anno.support;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSessionListener;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.core.context.AnnoModule;
import net.hasor.core.gift.GiftSupportModule;
import net.hasor.servlet.AbstractWebModule;
import net.hasor.servlet.WebApiBinder;
import net.hasor.servlet.WebErrorHook;
import net.hasor.servlet.anno.WebContextListener;
import net.hasor.servlet.anno.WebError;
import net.hasor.servlet.anno.WebFilter;
import net.hasor.servlet.anno.WebInitParam;
import net.hasor.servlet.anno.WebServlet;
import net.hasor.servlet.anno.WebSessionListener;
import org.more.util.StringUtils;
/**
 * 支持Bean、WebError、WebFilter、WebServlet注解功能。
 * @version : 2013-4-8
 * @author 赵永春 (zyc@hasor.net)
 */
@AnnoModule(description = "org.hasor.servlet软件包注解版支持。")
public class ServletAnnoSupportModule extends AbstractWebModule {
    /**初始化.*/
    public void init(WebApiBinder apiBinder) {
        apiBinder.moduleSettings().followTarget(GiftSupportModule.class);
        //1.LoadFilter.
        this.loadFilter(apiBinder);
        //2.LoadServlet.
        this.loadServlet(apiBinder);
        //3.loadErrorHook.
        this.loadErrorHook(apiBinder);
        //4.WebSessionListener
        this.loadSessionListener(apiBinder);
        //5.ServletContextListener
        this.loadServletContextListener(apiBinder);
    }
    public void start(AppContext appContext) {}
    public void stop(AppContext appContext) {}
    //
    /**装载Filter*/
    protected void loadFilter(WebApiBinder apiBinder) {
        //1.获取
        Set<Class<?>> webFilterSet = apiBinder.getClassSet(WebFilter.class);
        if (webFilterSet == null)
            return;
        List<Class<? extends Filter>> webFilterList = new ArrayList<Class<? extends Filter>>();
        for (Class<?> cls : webFilterSet) {
            if (Filter.class.isAssignableFrom(cls) == false) {
                Hasor.warning("not implemented Filter :%s", cls);
            } else {
                webFilterList.add((Class<? extends Filter>) cls);
            }
        }
        //2.排序
        Collections.sort(webFilterList, new Comparator<Class<?>>() {
            public int compare(Class<?> o1, Class<?> o2) {
                WebFilter o1Anno = o1.getAnnotation(WebFilter.class);
                WebFilter o2Anno = o2.getAnnotation(WebFilter.class);
                int o1AnnoIndex = o1Anno.sort();
                int o2AnnoIndex = o2Anno.sort();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.注册
        for (Class<? extends Filter> filterType : webFilterList) {
            WebFilter filterAnno = filterType.getAnnotation(WebFilter.class);
            Map<String, String> initMap = this.toMap(filterAnno.initParams());
            apiBinder.filter(null, filterAnno.value()).through(filterType, initMap);
            //
            String filterName = StringUtils.isBlank(filterAnno.filterName()) ? filterType.getSimpleName() : filterAnno.filterName();
            Hasor.info("loadFilter %s[%s] bind %s on %s.", filterName, getIndexStr(filterAnno.sort()), filterType, filterAnno.value());
        }
    }
    //
    /**装载Servlet*/
    protected void loadServlet(WebApiBinder apiBinder) {
        //1.获取
        Set<Class<?>> webServletSet = apiBinder.getClassSet(WebServlet.class);
        if (webServletSet == null)
            return;
        List<Class<? extends HttpServlet>> webServletList = new ArrayList<Class<? extends HttpServlet>>();
        for (Class<?> cls : webServletSet) {
            if (HttpServlet.class.isAssignableFrom(cls) == false) {
                Hasor.warning("not implemented HttpServlet :%s", cls);
            } else {
                webServletList.add((Class<? extends HttpServlet>) cls);
            }
        }
        //2.排序
        Collections.sort(webServletList, new Comparator<Class<?>>() {
            public int compare(Class<?> o1, Class<?> o2) {
                WebServlet o1Anno = o1.getAnnotation(WebServlet.class);
                WebServlet o2Anno = o2.getAnnotation(WebServlet.class);
                int o1AnnoIndex = o1Anno.loadOnStartup();
                int o2AnnoIndex = o2Anno.loadOnStartup();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.注册
        for (Class<? extends HttpServlet> servletType : webServletList) {
            WebServlet servletAnno = servletType.getAnnotation(WebServlet.class);
            Map<String, String> initMap = this.toMap(servletAnno.initParams());
            apiBinder.serve(null, servletAnno.value()).with(servletType, initMap);
            //
            String servletName = StringUtils.isBlank(servletAnno.servletName()) ? servletType.getSimpleName() : servletAnno.servletName();
            int sortInt = servletAnno.loadOnStartup();
            Hasor.info("loadServlet %s[%s] bind %s on %s.", servletName, getIndexStr(sortInt), servletType, servletAnno.value());
        }
    }
    //
    /**装载异常处理程序*/
    protected void loadErrorHook(WebApiBinder apiBinder) {
        //1.获取
        Set<Class<?>> webErrorSet = apiBinder.getClassSet(WebError.class);
        if (webErrorSet == null)
            return;
        List<Class<? extends WebErrorHook>> webErrorList = new ArrayList<Class<? extends WebErrorHook>>();
        for (Class<?> cls : webErrorSet) {
            if (WebErrorHook.class.isAssignableFrom(cls) == false) {
                Hasor.warning("not implemented ErrorHook :%s", cls);
            } else {
                webErrorList.add((Class<? extends WebErrorHook>) cls);
            }
        }
        //2.排序
        Collections.sort(webErrorList, new Comparator<Class<?>>() {
            public int compare(Class<?> o1, Class<?> o2) {
                WebError o1Anno = o1.getAnnotation(WebError.class);
                WebError o2Anno = o2.getAnnotation(WebError.class);
                int o1AnnoIndex = o1Anno.sort();
                int o2AnnoIndex = o2Anno.sort();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.注册
        for (Class<? extends WebErrorHook> errorHookType : webErrorList) {
            WebError errorAnno = errorHookType.getAnnotation(WebError.class);
            Map<String, String> initMap = this.toMap(errorAnno.initParams());
            apiBinder.error(errorAnno.value()).bind(errorHookType, initMap);
            //
            int sortInt = errorAnno.sort();
            Hasor.info("loadErrorHook [%s] of %s.", getIndexStr(sortInt), errorHookType);
        }
    }
    //
    /**装载HttpSessionListener*/
    protected void loadSessionListener(WebApiBinder apiBinder) {
        //1.获取
        Set<Class<?>> sessionListenerSet = apiBinder.getClassSet(WebSessionListener.class);
        if (sessionListenerSet == null)
            return;
        List<Class<? extends HttpSessionListener>> sessionListenerList = new ArrayList<Class<? extends HttpSessionListener>>();
        for (Class<?> cls : sessionListenerSet) {
            if (HttpSessionListener.class.isAssignableFrom(cls) == false) {
                Hasor.warning("not implemented HttpSessionListener :%s", cls);
            } else {
                sessionListenerList.add((Class<? extends HttpSessionListener>) cls);
            }
        }
        //2.排序
        Collections.sort(sessionListenerList, new Comparator<Class<?>>() {
            public int compare(Class<?> o1, Class<?> o2) {
                WebSessionListener o1Anno = o1.getAnnotation(WebSessionListener.class);
                WebSessionListener o2Anno = o2.getAnnotation(WebSessionListener.class);
                int o1AnnoIndex = o1Anno.sort();
                int o2AnnoIndex = o2Anno.sort();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.注册
        for (Class<? extends HttpSessionListener> sessionListener : sessionListenerList) {
            apiBinder.sessionListener().bind(sessionListener);
            //
            WebSessionListener anno = sessionListener.getAnnotation(WebSessionListener.class);
            int sortInt = anno.sort();
            Hasor.info("loadSessionListener [%s] bind %s.", getIndexStr(sortInt), sessionListener);
        }
    }
    //
    /**装载ServletContextListener*/
    protected void loadServletContextListener(WebApiBinder apiBinder) {
        //1.获取
        Set<Class<?>> contextListenerSet = apiBinder.getClassSet(WebContextListener.class);
        if (contextListenerSet == null)
            return;
        List<Class<? extends ServletContextListener>> contextListenerList = new ArrayList<Class<? extends ServletContextListener>>();
        for (Class<?> cls : contextListenerSet) {
            if (ServletContextListener.class.isAssignableFrom(cls) == false) {
                Hasor.warning("not implemented ServletContextListener :%s", cls);
            } else {
                contextListenerList.add((Class<? extends ServletContextListener>) cls);
            }
        }
        //2.排序
        Collections.sort(contextListenerList, new Comparator<Class<?>>() {
            public int compare(Class<?> o1, Class<?> o2) {
                WebContextListener o1Anno = o1.getAnnotation(WebContextListener.class);
                WebContextListener o2Anno = o2.getAnnotation(WebContextListener.class);
                int o1AnnoIndex = o1Anno.sort();
                int o2AnnoIndex = o2Anno.sort();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.注册
        for (Class<? extends ServletContextListener> sessionListener : contextListenerList) {
            apiBinder.contextListener().bind(sessionListener);
            //
            WebContextListener anno = sessionListener.getAnnotation(WebContextListener.class);
            int sortInt = anno.sort();
            Hasor.info("loadServletContextListener [%s] bind %s.", getIndexStr(sortInt), sessionListener);
        }
    }
    //
    /**转换参数*/
    protected Map<String, String> toMap(WebInitParam[] initParams) {
        Map<String, String> initMap = new HashMap<String, String>();
        if (initParams != null)
            for (WebInitParam param : initParams)
                if (StringUtils.isBlank(param.name()) == false)
                    initMap.put(param.name(), param.value());
        return initMap;
    }
    //
    /***/
    private static String getIndexStr(int index) {
        int allRange = 1000;
        /*-----------------------------------------*/
        int minStartIndex = Integer.MIN_VALUE;
        int minStopIndex = Integer.MIN_VALUE + allRange;
        for (int i = minStartIndex; i < minStopIndex; i++) {
            if (index == i)
                return "Min" + ((index == Integer.MIN_VALUE) ? "" : ("+" + String.valueOf(i + Math.abs(Integer.MIN_VALUE))));
        }
        int maxStartIndex = Integer.MAX_VALUE;
        int maxStopIndex = Integer.MAX_VALUE - allRange;
        for (int i = maxStartIndex; i > maxStopIndex; i--) {
            if (index == i)
                return "Max" + ((index == Integer.MAX_VALUE) ? "" : ("-" + Math.abs(Integer.MAX_VALUE - i)));
        }
        return String.valueOf(index);
    }
}