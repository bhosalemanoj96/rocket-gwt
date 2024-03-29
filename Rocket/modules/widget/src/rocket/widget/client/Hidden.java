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
package rocket.widget.client;

import rocket.dom.client.Dom;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWT Hidden widget
 * but also adds the ability to hijack any elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * Hidden widget.
 * 
 * ROCKET When upgrading from GWT 1.5.2 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class Hidden extends Widget {

	public Hidden() {
		super();
	}

	public Hidden(final String value) {
		super();

		this.setValue(value);
	}

	public Hidden(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(Element element) {
		Dom.checkInput("parameter:element", element, WidgetConstants.HIDDEN_INPUT_TYPE);
	}

	protected Element createElement() {
		final Element element = DOM.createElement("input");
		element.setPropertyString("type", WidgetConstants.HIDDEN_INPUT_TYPE);
		return element;
	}

	protected void applyStyleName() {
	}

	@Override
	protected String getInitialStyleName() {
		throw new UnsupportedOperationException("getWidgetStyleName");
	}

	@Override
	protected int getSunkEventsBitMask() {
		return 0;
	}

	public String getName() {
		return DOM.getElementProperty(getElement(), "name");
	}

	public void setName(String name) {
		DOM.setElementProperty(getElement(), "name", name);
	}

	/**
	 * Gets the default value of the hidden field.
	 * 
	 * @return the default value
	 */
	public String getDefaultValue() {
		return this.getElement().getPropertyString("defaultValue");
	}

	/**
	 * Sets the default value of the hidden field.
	 * 
	 * @param defaultValue
	 *            default value to set
	 */
	public void setDefaultValue(final String defaultValue) {
		this.getElement().setPropertyString("defaultValue", defaultValue);
	}

	/**
	 * Gets the value of the hidden field.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return this.getElement().getPropertyString("value");
	}

	public void setValue(String value) {
		this.getElement().setPropertyString("value", value);
	}
}
