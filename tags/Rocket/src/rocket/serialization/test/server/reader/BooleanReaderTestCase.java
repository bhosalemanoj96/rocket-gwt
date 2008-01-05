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
package rocket.serialization.test.server.reader;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.reader.BooleanReader;
import rocket.serialization.test.server.ServerTestCase;

public class BooleanReaderTestCase extends ServerTestCase {

	final static String BOOLEAN = Boolean.class.getName();

	final static boolean BOOLEAN_VALUE = true;

	public void testReadBoolean() {
		final String stream = "[1,\"" + BOOLEAN + "\",1,2," + BOOLEAN_VALUE + "]";
		final ObjectInputStream input = createObjectInputStream(stream, BooleanReader.instance);

		final Boolean object = (Boolean) input.readObject();
		assertNotNull(stream, object);

		assertEquals(BOOLEAN_VALUE, object.booleanValue());

		this.verifyFurtherReadsFail(input);
	}
}
