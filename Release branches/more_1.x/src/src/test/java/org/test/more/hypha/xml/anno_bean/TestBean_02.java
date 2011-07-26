/*
 * Copyright 2008-2009 the original author or authors.
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
package org.test.more.hypha.xml.anno_bean;
import org.more.hypha.anno.define.Aop;
import org.more.hypha.anno.define.AopInformed;
import org.more.hypha.anno.define.Bean;
import org.more.hypha.aop.define.PointcutType;
/**
 * 
 * @version 2010-9-21
 * @author ������ (zyc@byshell.org)
 */
//@Bean
@Aop(informeds = { @AopInformed(type = PointcutType.Before, refBean = "") })
public class TestBean_02 {}