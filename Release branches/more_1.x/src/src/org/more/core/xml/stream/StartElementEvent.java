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
package org.more.core.xml.stream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/**
 * ������һ����ʼ��ǩʱ��
 * @version 2010-9-8
 * @author ������ (zyc@byshell.org)
 */
public class StartElementEvent extends XmlStreamEvent {
    public StartElementEvent(String xpath, XmlReader xmlReader, XMLStreamReader reader) {
        super(xpath, xmlReader, reader);
    }
    /**��ȡԪ������{@link QName}����*/
    public QName getName() {
        return this.getReader().getName();
    }
    /**��ȡԪ����(�����������ռ�ǰ׺)��*/
    public String getElementName() {
        return this.getName().getLocalPart();
    }
    /**��ȡԪ�������ռ�ǰ׺��*/
    public String getPrefix() {
        return this.getName().getPrefix();
    }
    /**��ȡԪ�������ռ䡣*/
    public String getNamespaceURI() {
        return this.getName().getNamespaceURI();
    }
    /**��ȡ�����Ԫ���϶���������ռ�������*/
    public int getNamespaceCount() {
        return this.getReader().getNamespaceCount();
    }
    /**��ȡ�����Ԫ���϶����ָ�������������ռ�ǰ׺��*/
    public String getNamespacePrefix(int index) {
        return this.getReader().getNamespacePrefix(index);
    }
    /**��ȡ�����Ԫ���϶����ָ�������������ռ�URI��*/
    public String getNamespaceURI(int index) {
        return this.getReader().getNamespaceURI(index);
    }
    /**ʹ��ָ���������ռ�ǰ׺��ȡ�����ռ�URI��*/
    public String getNamespaceURI(String prefix) {
        return this.getReader().getNamespaceURI(prefix);
    }
    /**��ȡ�ڸ�Ԫ���϶��������������*/
    public int getAttributeCount() {
        return this.getReader().getAttributeCount();
    }
    /**��ȡ��Ԫ���϶����ָ����������*/
    public QName getAttributeName(int index) {
        return this.getReader().getAttributeName(index);
    }
    /**��ȡ��Ԫ���϶����ָ������ֵ��*/
    public String getAttributeValue(int index) {
        return this.getReader().getAttributeValue(index);
    }
    /**��ȡ���ı�Ԫ�ص����ݣ�������Ǵ��ı�Ԫ�أ����׳��쳣��*/
    public String getElementText() throws XMLStreamException {
        return this.getReader().getElementText();
    }
}