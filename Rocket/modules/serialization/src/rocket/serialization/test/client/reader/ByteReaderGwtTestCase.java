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
package rocket.serialization.test.client.reader;

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.reader.ByteReader;
import rocket.serialization.test.client.ClientGwtTestCase;

public class ByteReaderGwtTestCase extends ClientGwtTestCase {

	final static String BYTE = "java.lang.Byte";

	final static byte BYTE_VALUE = 123;

	public void testReadByte() {
		final String stream = "[1,\"" + BYTE + "\",1,2," + BYTE_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, BYTE, ByteReader.instance);

		final Byte object = (Byte) input.readObject();
		assertNotNull(stream, object);

		assertEquals(BYTE_VALUE, object.byteValue());

		this.verifyFurtherReadsFail(input);
	}
}
