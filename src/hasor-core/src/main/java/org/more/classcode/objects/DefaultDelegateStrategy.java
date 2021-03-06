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
package org.more.classcode.objects;
import org.more.classcode.ClassEngine;
import org.more.classcode.DelegateStrategy;
/**
 * 接口{@link DelegateStrategy}的默认实现，该策略实现不会忽略任何委托。
 * @version 2010-9-3
 * @author 赵永春 (zyc@hasor.net)
 */
public class DefaultDelegateStrategy implements DelegateStrategy {
    public void initStrategy(ClassEngine classEngine) {}
    public boolean isIgnore(Class<?> delegateType) {
        return false;
    }
}