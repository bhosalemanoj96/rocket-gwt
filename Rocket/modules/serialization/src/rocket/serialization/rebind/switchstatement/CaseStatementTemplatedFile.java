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
package rocket.serialization.rebind.switchstatement;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the case-statement.txt template
 * 
 * @author Miroslav Pokorny
 */
class CaseStatementTemplatedFile extends TemplatedFileCodeBlock {

	public CaseStatementTemplatedFile() {
		super();
	}

	@Override
	public boolean isNative() {
		return true;
	}

	@Override
	public void setNative(final boolean ignored) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The serializedType whose object writer is being registered
	 */
	private Type serializedType;

	protected Type getSerializedType() {
		Checker.notNull("field:serializedType", serializedType);
		return this.serializedType;
	}

	public void setSerializedType(final Type serializedType) {
		Checker.notNull("parameter:serializedType", serializedType);
		this.serializedType = serializedType;
	}

	/**
	 * The singleton field holding the object writer
	 */
	private Field singleton;

	protected Field getSingleton() {
		Checker.notNull("field:singleton", singleton);
		return this.singleton;
	}

	public void setSingleton(final Field singleton) {
		Checker.notNull("parameter:singleton", singleton);
		this.singleton = singleton;
	}

	@Override
	protected String getResourceName() {
		return Constants.CASE_TEMPLATE;
	}

	@Override
	public InputStream getInputStream() {
		return super.getInputStream();
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.CASE_SERIALIZED_TYPE.equals(name)) {
				value = new StringLiteral(this.getSerializedType().getRuntimeName());
				break;
			}
			if (Constants.CASE_STATEMENT_SINGLETON.equals(name)) {
				value = this.getSingleton();
				break;
			}
			break;
		}
		return value;
	}
}
