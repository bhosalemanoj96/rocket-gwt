/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget.menu;

import java.util.Iterator;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface MenuList extends HasWidgets {
    // PANEL ::::::::::::::::::::::::::::::::::::::::::::::

    boolean add(final Widget widget);

    boolean insert(final Widget widget, final int beforeIndex);

    boolean remove(final Widget widget);

    int getCount();

    Iterator iterator();

    void clear();

    void open();

    void close();

    AbstractMenu getMenu();
    
    void setMenu(AbstractMenu menu);

    MenuList getParentMenuList();

    boolean hasParentMenuList();

    void setParentMenuList(MenuList parentMenuList);

    Element getElement();

    boolean isHorizontalLayout();

    void onBrowserEvent( Event event );
}
