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
package org.platform.api.dbmapping.meta;
/**
 * һ��һ�����һ��ӳ��ʵ�壨����att��ǩ���������Զ��壩
 * @version : 2013-1-27
 * @author ������ (zyc@byshell.org)
 */
public class EntityAttMeta extends AttMeta {
    /**���ʵ�����ơ�*/
    private String forEntity   = "";
    /**���ʵ��������С�*/
    private String forProperty = "";
    //
    //
    //
    public String getForEntity() {
        return forEntity;
    }
    public void setForEntity(String forEntity) {
        this.forEntity = forEntity;
    }
    public String getForProperty() {
        return forProperty;
    }
    public void setForProperty(String forProperty) {
        this.forProperty = forProperty;
    }
}