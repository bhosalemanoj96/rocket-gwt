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
package rocket.logging.rebind;

class Logger {
	Logger(final String level, final String typeName) {
		super();

		this.setLevel(level);
		this.setTypeName(typeName);
	}

	String level;

	String getLevel() {
		return this.level;
	}

	void setLevel(final String level) {
		this.level = level;
	}

	String typeName;

	String getTypeName() {
		return this.typeName;
	}

	void setTypeName(final String typeName) {
		this.typeName = typeName;
	}
}
