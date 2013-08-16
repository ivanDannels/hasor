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
package org.more.util;
public class MatchUtils {
    /**将通配符转换成正则表达式。*/
    public static String wildToRegex(String wild) {
        if (wild == null)
            throw new NullPointerException("wild param is null");
        StringBuffer result = new StringBuffer("");
        char metachar[] = { '$', '^', '[', ']', '(', ')', '{', '|', '+', '.', '\\' };
        for (int i = 0; i < wild.length(); i++) {
            char ch = wild.charAt(i);
            for (int j = 0; j < metachar.length; j++)
                if (ch == metachar[j])
                    result.append("\\");
            if (ch == '*')
                result.append(".*");
            else if (ch == '?')
                result.append(".");
            else
                result.append(ch);
        }
        result.append("$");
        return result.toString();
    }
    /**将字符串转换成成正则表达式。*/
    public static String stringToRegex(String wild) {
        if (wild == null)
            throw new NullPointerException("wild param is null");
        StringBuffer result = new StringBuffer("");
        char metachar[] = { '$', '^', '[', ']', '(', ')', '{', '|', '+', '.', '\\' };
        for (int i = 0; i < wild.length(); i++) {
            char ch = wild.charAt(i);
            for (int j = 0; j < metachar.length; j++)
                if (ch == metachar[j])
                    result.append("\\");
            result.append(ch);
        }
        return result.toString();
    }
    /**使用通配符匹配字符串。*/
    public static boolean matchWild(String pattern, String str) {
        if (str == null)
            return false;
        return str.matches(wildToRegex(pattern));
    }
}