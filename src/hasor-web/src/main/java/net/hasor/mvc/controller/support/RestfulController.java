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
package net.hasor.mvc.controller.support;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import net.hasor.core.AppContext;
import com.google.inject.Inject;
/**
 * action功能的入口。
 * @version : 2013-5-11
 * @author 赵永春 (zyc@hasor.net)
 */
class RestfulController implements Filter {
    @Inject
    private AppContext         appContext  = null;
    private ActionDefineImpl[] defineArray = null;
    //
    public void init(FilterConfig filterConfig) throws ServletException {
        ActionManager actionManager = appContext.getInstance(ActionManager.class);
        ArrayList<ActionDefineImpl> restfulList = new ArrayList<ActionDefineImpl>();
        //
        for (ActionNameSpace ns : actionManager.getNameSpaceList()) {
            for (ActionDefineImpl define : ns.getActions())
                if (define.getRestfulMapping() != null)
                    restfulList.add(define);
        }
        //
        Collections.sort(restfulList, new Comparator<ActionDefineImpl>() {
            public int compare(ActionDefineImpl o1, ActionDefineImpl o2) {
                return o1.getRestfulMapping().compareToIgnoreCase(o2.getRestfulMapping());
            }
        });
        this.defineArray = restfulList.toArray(new ActionDefineImpl[restfulList.size()]);
    }
    public void destroy() {}
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String actionPath = request.getRequestURI().substring(request.getContextPath().length());
        //1.获取 ActionInvoke
        ActionDefineImpl define = this.getActionDefine(request.getMethod(), actionPath);
        if (define == null) {
            chain.doFilter(request, resp);
            return;
        }
        //3.执行调用
        this.doInvoke(define, request, resp);
    }
    private ActionDefineImpl getActionDefine(String httpMethod, String requestPath) {
        for (ActionDefineImpl restAction : this.defineArray) {
            if (requestPath.matches(restAction.getRestfulMappingMatches()) == true) {
                if (restAction.matchingMethod(httpMethod))
                    return restAction;
            }
        }
        return null;
    }
    //
    private void doInvoke(ActionDefineImpl define, ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        try {
            define.createInvoke(servletRequest, servletResponse).invoke();
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
            if (target instanceof ServletException)
                throw (ServletException) target;
            if (target instanceof IOException)
                throw (IOException) target;
            throw new ServletException(target);
        }
    }
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    /** 为转发提供支持 */
    public RequestDispatcher getRequestDispatcher(final String newRequestUri, final HttpServletRequest request) {
        // TODO 需要检查下面代码是否符合Servlet规范（带request参数情况下也需要检查）
        //1.拆分请求字符串
        final ActionDefineImpl define = getActionDefine(request.getMethod(), newRequestUri);
        if (define == null)
            return null;
        //
        return new RequestDispatcher() {
            public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                servletRequest.setAttribute(REQUEST_DISPATCHER_REQUEST, Boolean.TRUE);
                /*执行servlet*/
                try {
                    doInvoke(define, servletRequest, servletResponse);
                } finally {
                    servletRequest.removeAttribute(REQUEST_DISPATCHER_REQUEST);
                }
            }
            public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                if (servletResponse.isCommitted() == true)
                    throw new ServletException("Response has been committed--you can only call forward before committing the response (hint: don't flush buffers)");
                /*清空缓冲*/
                servletResponse.resetBuffer();
                ServletRequest requestToProcess;
                if (servletRequest instanceof HttpServletRequest) {
                    requestToProcess = new RequestDispatcherRequestWrapper(servletRequest, newRequestUri);
                } else {
                    //正常情况之下不会执行这段代码。
                    requestToProcess = servletRequest;
                }
                /*执行转发*/
                servletRequest.setAttribute(REQUEST_DISPATCHER_REQUEST, Boolean.TRUE);
                try {
                    doInvoke(define, requestToProcess, servletResponse);
                } finally {
                    servletRequest.removeAttribute(REQUEST_DISPATCHER_REQUEST);
                }
            }
        };
    }
    /** 使用RequestDispatcherRequestWrapper类处理request.getRequestURI方法的返回值*/
    public static final String REQUEST_DISPATCHER_REQUEST = "javax.servlet.forward.servlet_path";
    private static class RequestDispatcherRequestWrapper extends HttpServletRequestWrapper {
        private final String newRequestUri;
        public RequestDispatcherRequestWrapper(ServletRequest servletRequest, String newRequestUri) {
            super((HttpServletRequest) servletRequest);
            this.newRequestUri = newRequestUri;
        }
        public String getRequestURI() {
            return newRequestUri;
        }
    }
}