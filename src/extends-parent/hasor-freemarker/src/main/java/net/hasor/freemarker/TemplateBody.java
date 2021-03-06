/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.freemarker;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
/**
 * 
 * @version : 2012-6-14
 * @author ������ (zyc@byshell.org)
 */
public interface TemplateBody {
    /**��ǩ����*/
    public Map<String, Object> tagProperty();
    /**��ȡ��ǩִ�л���*/
    public Environment getEnvironment();
    /**��Ⱦ�����ǩ����*/
    public void render(Writer arg0) throws TemplateException, IOException;
}