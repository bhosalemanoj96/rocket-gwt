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
package rocket.json.rebind.setfield;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.util.client.Checker;

/**
 * An abstraction for the set-field templated file
 * 
 * @author Miroslav Pokorny
 */
public class SetFieldTemplatedFile extends TemplatedFileCodeBlock {

	public SetFieldTemplatedFile() {
		super();
	}

	@Override
	public boolean isNative() {
		return true;
	}

	private Field field;

	protected Field getField() {
		Checker.notNull("field:field", field);
		return this.field;
	}

	public void setField(final Field field) {
		Checker.notNull("parameter:field", field);
		this.field = field;
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.FIELD.equals(name)) {
				value = this.getField();
				break;
			}
			break;
		}
		return value;
	}
}
