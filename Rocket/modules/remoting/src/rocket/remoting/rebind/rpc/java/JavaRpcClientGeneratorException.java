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
package rocket.remoting.rebind.rpc.java;

import rocket.generator.rebind.GeneratorException;

/**
 * A common base class for all exceptions that may be thrown during the code
 * generation of a remote json service client.
 * 
 * @author Miroslav Pokorny
 */
public class JavaRpcClientGeneratorException extends GeneratorException {

	JavaRpcClientGeneratorException() {
		super();
	}

	JavaRpcClientGeneratorException(String message) {
		super(message);
	}

	JavaRpcClientGeneratorException(Throwable cause) {
		super(cause);
	}

	JavaRpcClientGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}
}
