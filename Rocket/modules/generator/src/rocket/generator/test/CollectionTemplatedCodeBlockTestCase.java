/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.generator.test;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.EmptyCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.StringBufferSourceWriter;

public class CollectionTemplatedCodeBlockTestCase extends TestCase {
	
	public void testEmptyCollection() {
		final CollectionTemplatedCodeBlock test = this.createCollectionTemplatedCodeBlock(Collections.<Type>emptyList());
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		test.write(writer);

		final String actual = writer.getBuffer();
		final String expected = "";
		TestCase.assertEquals(expected, actual);
	}

	public void testCollectionWithOneElement() {
		final CollectionTemplatedCodeBlock test = this.createCollectionTemplatedCodeBlock(Collections.<CodeBlock>nCopies(1, EmptyCodeBlock.INSTANCE));
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		test.write(writer);

		final String actual = writer.getBuffer();
		final String expected = "value0";
		TestCase.assertEquals(expected, actual);
	}

	public void testCollectionWithTwoElements() {
		final CollectionTemplatedCodeBlock test = this.createCollectionTemplatedCodeBlock(Collections.<CodeBlock>nCopies(2, EmptyCodeBlock.INSTANCE));
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		test.write(writer);

		final String actual = writer.getBuffer();
		final String expected = "value0,value1";
		TestCase.assertEquals(expected, actual);
	}

	public void testCollectionWithThreeElements() {
		final CollectionTemplatedCodeBlock test = this.createCollectionTemplatedCodeBlock(Collections.<CodeBlock>nCopies(3, EmptyCodeBlock.INSTANCE));
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		test.write(writer);

		final String actual = writer.getBuffer();
		final String expected = "value0,value1,value2";
		TestCase.assertEquals(expected, actual);
	}

	CollectionTemplatedCodeBlock createCollectionTemplatedCodeBlock(final Collection collection) {
		final TestCollectionTemplatedCodeBlock test = new TestCollectionTemplatedCodeBlock();
		test.setCollection(collection);
		return test;
	}

	public class TestCollectionTemplatedCodeBlock extends CollectionTemplatedCodeBlock<Object> {

		private Collection<Object> collection;

		protected Collection<Object> getCollection() {
			return this.collection;
		}

		public void setCollection(final Collection<Object> collection) {
			this.collection = collection;
		}

		@Override
		protected void prepareToWrite(final Object element) {
		}

		@Override
		protected void writeBetweenElements(final SourceWriter writer) {
			writer.print(",");
		}

		@Override
		protected InputStream getInputStream() {
			return new StringBufferInputStream("${value}");
		}

		@Override
		protected Object getValue0(final String name) {
			final int index = this.getIndex();
			return new CodeBlock() {
				public boolean isEmpty() {
					return false;
				}

				public void write(final SourceWriter writer) {
					writer.print("value" + index);
				}
			};
		}
	}

}
