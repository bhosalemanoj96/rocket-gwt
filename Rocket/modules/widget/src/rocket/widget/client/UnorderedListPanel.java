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
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * An UnorderedListPanel is a panel that creates a list as the primary container
 * widget. Each widget that is added is again wrapped in their own list item.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class UnorderedListPanel extends Panel implements HasWidgets {

	public UnorderedListPanel() {
		super();
	}

	public UnorderedListPanel(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(final Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.UNORDERED_LIST_TAG);
	}

	/**
	 * Factory method which creates the parent UL element for this entire panel
	 * 
	 * @return
	 */
	@Override
	protected Element createPanelElement() {
		return DOM.createElement(WidgetConstants.UNORDERED_LIST_TAG);
	}

	protected String getInitialStyleName() {
		return WidgetConstants.UNORDERED_LIST_PANEL_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	/**
	 * Returns the element which will house each of the new widget's elements.
	 * 
	 * @return
	 */
	public Element getParentElement() {
		return this.getElement();
	}

	@Override
	protected void insert0(final Element element, final int indexBefore) {
		Checker.notNull("parameter:element", element);

		final Element child = this.createElement();
		DOM.insertChild(this.getParentElement(), child, indexBefore);
		child.appendChild(element);
	}

	protected Element createElement() {
		return DOM.createElement(WidgetConstants.UNORDERED_LIST_ITEM_TAG);
	}

	@Override
	protected void remove0(final Element element, final int index) {
		Checker.notNull("parameter:element", element);
		Dom.removeFromParent(element.getParentElement());
	}
}
