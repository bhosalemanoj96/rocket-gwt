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
package rocket.beans.rebind.set;

import java.util.Collection;
import java.util.HashSet;

import rocket.beans.rebind.collection.CollectionTemplatedFile;
import rocket.beans.rebind.collection.CollectionValue;

/**
 * Eadch instance represents a set of values before they are realised within a
 * FactoryBean
 * 
 * @author Miroslav Pokorny
 */
public class SetValue extends CollectionValue {

	protected Collection createElements() {
		return new HashSet();
	}

	protected CollectionTemplatedFile createTemplate() {
		return new SetTemplatedFile();
	}

	protected String getCollectionTypeName() {
		return Constants.SET_TYPE;
	}
}
