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
package org.hasor.icache.key;
import org.hasor.context.AppContext;
import org.hasor.icache.KeyBuilder;
/**
 * 
 * @version : 2013-4-23
 * @author ������ (zyc@byshell.org)
 */
abstract class AbstractKeyBuilder implements KeyBuilder {
    @Override
    public void initKeyBuilder(AppContext appContext) {}
    @Override
    public void destroy(AppContext appContext) {}
}
