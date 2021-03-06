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
package net.hasor.servlet.binder.support;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.hasor.core.AppContext;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
/**
 *  
 * @version : 2013-4-12
 * @author 赵永春 (zyc@hasor.net)
 */
@Singleton
public class ManagedErrorPipeline {
    private ErrorDefinition[] errorDefinitions;
    private volatile boolean  initialized    = false;
    private int               errorCaseCount = 5;
    //
    public synchronized void initPipeline(AppContext appContext) throws ServletException {
        if (initialized)
            return;
        this.errorDefinitions = collectErrorDefinitions(appContext.getGuice());
        for (ErrorDefinition errorDefinition : errorDefinitions) {
            errorDefinition.init(appContext);
        }
        //everything was ok...
        this.errorCaseCount = appContext.getSettings().getInteger("hasor.httpServlet.errorCaseCount", 5);
        this.initialized = true;
    }
    private ErrorDefinition[] collectErrorDefinitions(Injector injector) {
        List<ErrorDefinition> errorDefinitions = new ArrayList<ErrorDefinition>();
        TypeLiteral<ErrorDefinition> ERROR_DEFS = TypeLiteral.get(ErrorDefinition.class);
        for (Binding<ErrorDefinition> entry : injector.findBindingsByType(ERROR_DEFS)) {
            errorDefinitions.add(entry.getProvider().get());
        }
        // Convert to a fixed size array for speed.
        return errorDefinitions.toArray(new ErrorDefinition[errorDefinitions.size()]);
    }
    public void dispatch(ServletRequest request, ServletResponse response, Throwable error) throws IOException, ServletException {
        Throwable onError = (error instanceof ServletException) ? ((ServletException) error).getRootCause() : error;
        if (onError == null)
            onError = error;
        //1.进行异常处理
        for (int i = 0; i < errorCaseCount; i++) {
            for (int j = 0; j < errorDefinitions.length; j++) {
                ErrorDefinition errDefine = errorDefinitions[j];
                try {
                    if (errDefine.doError(request, response, onError) == true)
                        return;
                    else
                        continue;
                } catch (Throwable e) {
                    onError = e;
                    break;
                }
                //end !
            }
        }
        //2.异常处理程序无法将最终的异常处理完毕抛出最后处理的异常。
        if (error instanceof IOException)
            throw (IOException) error;
        if (error instanceof ServletException)
            throw (ServletException) error;
        throw new ServletException(error);
    }
    public void destroyPipeline(AppContext appContext) {
        for (ErrorDefinition errorDefinition : errorDefinitions) {
            errorDefinition.destroy(appContext);
        }
    }
}