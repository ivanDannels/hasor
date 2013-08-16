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
package org.hasor.context.event;
import org.hasor.context.TimerCallBackHook;
/**
 * Timer�쳣�ص��ӿڡ�
 * @version : 2013-4-12
 * @author ������ (zyc@hasor.net)
 */
class EmptyTimerCallBackHook implements TimerCallBackHook {
    public void handleException(String timerName, Throwable e) {}
}