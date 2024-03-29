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
package rocket.remoting.client;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import rocket.collection.client.MultiValueMap;
import rocket.util.client.Checker;
import rocket.util.client.Utilities;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A simple implementation container for request parameters
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RequestParameters implements IsSerializable, Serializable {

	public RequestParameters() {
		this.setMultiValueMap(new MultiValueMap<String,String>());
	}

	/**
	 * A MultiValueMap is used to hold parameter names and their associated list
	 * of values.
	 */
	private MultiValueMap<String,String> multiValueMap;

	protected MultiValueMap<String,String> getMultiValueMap() {
		Checker.notNull("field:map", multiValueMap);

		return multiValueMap;
	}

	protected void setMultiValueMap(final MultiValueMap<String,String> multiValueMap) {
		Checker.notNull("parameter:multiValueMap", multiValueMap);

		this.multiValueMap = multiValueMap;
	}

	/**
	 * Tests if a particular parameter has been added.
	 * 
	 * @param name
	 * @return True if the parameter exists false otherwise
	 */
	public boolean contains(final String name) {
		return this.getMultiValueMap().contains(name);
	}

	public String getValue(final String name) {
		return (String)this.getMultiValueMap().getFirstValue(name);
	}

	protected List<String> getValueAsList(final String name) {
		Checker.notEmpty("parameter:name", name);
		return this.getMultiValueMap().getValuesList(name);
	}

	public String[] getValues(final String name) {
		final Object[] values = this.getMultiValueMap().getValues(name);
		String[] array = null;
		if (null != values) {
			final int size = values.length;
			array = new String[size];
			for (int i = 0; i < size; i++) {
				array[i] = (String) values[i];
			}
		}
		return array;
	}

	public void buildFromUrl(final String url) {
		Checker.notNull("parameter:url", url);
		final int queryStringIndex = url.indexOf("?");
		if (queryStringIndex != -1) {
			final String queryString = url.substring(queryStringIndex);
			this.buildFromQueryString(queryString);
		}
	}

	public void buildFromQueryString(final String string) {
		Checker.notNull("parameter:string", string);

		final String[] parameters = Utilities.split(string, "&", true);
		for (int i = 0; i < parameters.length; i++) {
			final String parameter = parameters[i];
			final int equals = parameter.indexOf('=');
			final String name = equals == -1 ? parameter : parameter.substring(0, equals);
			final String value = equals == -1 ? null : parameter.substring(equals + 1);

			this.add(name, value);
		}
	}

	public void add(final String name, final String value) {
		Checker.notEmpty("parameter:name", name);

		this.getMultiValueMap().add(name, value);
	}

	public Iterator<String> names() {
		return this.getMultiValueMap().keys();
	}

	public void clear() {
		this.getMultiValueMap().clear();
	}

	/**
	 * Converts the parameters within this object into a post data equivalent ie
	 * array of bytes.
	 * 
	 * @return The built string.
	 */
	public String asString() {
		final StringBuffer data = new StringBuffer();

		final Iterator<String> names = this.names();
		boolean addSeparator = false;

		while (names.hasNext()) {
			final String name = (String) names.next();
			final List<String> valuesList = this.getValueAsList(name);
			if (null == valuesList) {
				continue;
			}

			final Iterator<String> values = valuesList.iterator();
			while (values.hasNext()) {
				if (addSeparator) {
					data.append("&");
				}
				addSeparator = true;

				data.append(name);
				data.append('=');
				data.append(URL.encodeComponent(values.next()));
			}
		}

		return data.toString();
	}

	public String toString() {
		return super.toString() + ", parameters:" + multiValueMap;
	}
}
