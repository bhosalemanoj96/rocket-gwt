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
package rocket.widget.client.tabpanel;

import rocket.util.client.Checker;
import rocket.widget.client.Html;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

/**
 * The HorizontalTabPanel class is the base class for both the TopTabPanel and
 * BottomTabPanel classes.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract class HorizonalTabPanel extends TabPanel {

	protected HorizonalTabPanel() {
		super();
	}

	protected TabBarPanel createTabBarPanel(final VerticalAlignmentConstant alignment) {
		Checker.notNull("parameter:alignment", alignment);

		final HorizontalTabBarPanel panel = new HorizontalTabBarPanel();

		panel.setStyleName(this.getTabBarStyleName());
		panel.setVerticalAlignment(alignment);

		final Widget first = this.createTabBarBeforeSpacer();
		final Widget rest = this.createTabBarAfterSpacer();
		panel.add(first);
		panel.add(rest);
		panel.setCellHeight(first, "100%");
		panel.setCellWidth(rest, "100%");
		panel.setCellHorizontalAlignment(rest, HasHorizontalAlignment.ALIGN_RIGHT);
		return panel;
	}

	protected abstract String getTabBarStyleName();

	protected Widget createTabBarBeforeSpacer() {
		final Html widget = new Html("&nbsp;");
		widget.setStyleName(this.getTabBarBeforeSpacerStyleName());
		widget.setHeight("100%");
		return widget;
	}

	protected abstract String getTabBarBeforeSpacerStyleName();

	protected Widget createTabBarAfterSpacer() {
		final Html widget = new Html("&nbsp;");
		widget.setStyleName(getTabBarAfterSpacerStyleName());
		widget.setHeight("100%");
		return widget;
	}

	protected abstract String getTabBarAfterSpacerStyleName();
}
