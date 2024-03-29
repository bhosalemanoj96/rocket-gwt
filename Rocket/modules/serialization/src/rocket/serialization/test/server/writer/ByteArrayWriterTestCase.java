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
package rocket.serialization.test.server.writer;

import rocket.serialization.server.writer.ByteArrayWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class ByteArrayWriterTestCase extends ServerTestCase {

	final static String BYTE_ARRAY = byte[].class.getName();

	final static byte BYTE_VALUE = 34;

	public void testWriteByteArray() {
		final TestServerObjectOutputStream output = createObjectOutputStream(ByteArrayWriter.instance);

		final byte[] array = new byte[1];
		array[0] = BYTE_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1," + BYTE_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + BYTE_ARRAY + "\"," + expectedValues + "]", text);
	}
}
