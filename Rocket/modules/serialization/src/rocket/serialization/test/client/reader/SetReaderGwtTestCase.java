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

import java.util.Iterator;
import java.util.Set;

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.reader.SetReader;
import rocket.serialization.test.client.ClientGwtTestCase;

public class SetReaderGwtTestCase extends ClientGwtTestCase {

	final String HASHSET = "java.util.HashSet";

	public void testReadEmptySet() {
		final String stream = "[1,\"" + HASHSET + "\",1,2,0]";
		final ClientObjectInputStream reader = createObjectInputStream(stream, HASHSET, SetReader.instance);
		final Set set = (Set) reader.readObject();
		assertNotNull(set);
		assertEquals("" + set, 0, set.size());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadSetWithNullElement() {
		final String stream = "[1,\"" + HASHSET + "\",1,2,1,0]";
		final ClientObjectInputStream reader = createObjectInputStream(stream, HASHSET, SetReader.instance);
		final Set set = (Set) reader.readObject();
		assertNotNull(set);
		assertEquals("" + set, 1, set.size());
		assertNull("" + set, set.iterator().next());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadSetSingleElement() {
		final String stream = "[2,\"" + HASHSET + "\",\"" + APPLE + "\",1,2,1,3]";
		final ClientObjectInputStream reader = createObjectInputStream(stream, HASHSET, SetReader.instance);
		final Set set = (Set) reader.readObject();
		assertNotNull(set);
		assertEquals("" + set, 1, set.size());
		assertEquals("" + set, APPLE, set.iterator().next());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadSetWithElements() {
		final String stream = "[3,\"" + HASHSET + "\",\"" + APPLE + "\",\"" + BANANA + "\",1,2,2,3,4]";
		final ClientObjectInputStream reader = createObjectInputStream(stream, HASHSET, SetReader.instance);
		final Set set = (Set) reader.readObject();
		assertNotNull(set);
		assertEquals("" + set, 2, set.size());

		final Iterator iterator = set.iterator();
		assertEquals("" + set, APPLE, iterator.next());
		assertEquals("" + set, BANANA, iterator.next());

		this.verifyFurtherReadsFail(reader);
	}
}
