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
package org.hasor.icache;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 声明一个缓存服务提供者，标记了该接口的类必须要求实现{@link Cache}接口。
 * @version : 2013-3-12
 * @author 赵永春 (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface CacheDefine {
    /**对服务的描述信息。*/
    public String description() default "";
    /**在管理控制台显示服务时使用displayName属性。*/
    public String displayName() default "";
    /**缓存名称，同名注册会被覆盖。*/
    public String[] value();
    /**排序顺序（越小越优先）*/
    public int sort() default Integer.MAX_VALUE;
}