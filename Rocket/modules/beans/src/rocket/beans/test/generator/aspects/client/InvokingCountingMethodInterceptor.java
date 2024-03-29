/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.beans.test.generator.aspects.client;

import rocket.beans.client.aop.MethodInvocation;

/**
 * This method interceptor does nothing but invoke the next interceptor in the
 * chain.
 * 
 * @author Miroslav Pokorny
 */
public class InvokingCountingMethodInterceptor implements rocket.beans.client.aop.MethodInterceptor {

	public InvokingCountingMethodInterceptor() {
		super();
	}

	public Object invoke(final MethodInvocation invocation) throws Throwable {
		this.executedCount++;
		return invocation.proceed();
	}

	public int executedCount = 0;
}
