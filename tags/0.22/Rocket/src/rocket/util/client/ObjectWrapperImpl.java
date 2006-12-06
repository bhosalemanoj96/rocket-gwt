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
package rocket.util.client;

import rocket.dom.client.Destroyable;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Convenient base class for any native object wrapper. It provides typed methods that make it easy to read or write to object properties.
 * properties.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class ObjectWrapperImpl implements ObjectWrapper, Destroyable {

    protected ObjectWrapperImpl() {
        super();
    }

    public boolean equals(final Object otherObject) {
        return otherObject instanceof ObjectWrapper ? this.equals((ObjectWrapper) otherObject) : false;
    }

    public boolean equals(final ObjectWrapper otherObjectWrapper) {
        ObjectHelper.checkNotNull("parameter:otherObjectWrapper", otherObjectWrapper);

        boolean same = false;
        while (true) {
            // if nativeObjectWrapper is missing cant be equal to anything...
            if (false == this.hasObject()) {
                break;
            }

            // if other rule hasnt got a native rule object it cant be equal...
            if (false == otherObjectWrapper.hasObject()) {
                break;
            }

            same = this.getObject().equals(otherObjectWrapper.getObject());
            break;
        }
        return same;
    }

    /**
     * Releases reference to the dom object being wrapped.
     */
    public void destroy() {
        this.clearObject();
    }

    /**
     * The native object being wrapped
     */
    private JavaScriptObject object;

    public JavaScriptObject getObject() {
        ObjectHelper.checkNotNull("field:object", object);
        return object;
    }

    public boolean hasObject() {
        return null != this.object;
    }

    public void setObject(final JavaScriptObject object) {
        ObjectHelper.checkNotNull("parameter:object", object);
        this.object = object;
    }

    public void clearObject() {
        this.object = null;
    }

    /**
     * Returns the string form of the object being wrapped.
     * 
     * @return
     */
    protected native String toStringObject()/*-{
     var object = this.@rocket.util.client.ObjectWrapperImpl::object;
     return object ? object : "null";
     }-*/;

    // A VARIETY OF CONVENIENT TYPED PROPERTY METHODS.

    protected boolean hasProperty(final String propertyName) {
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return ObjectHelper.hasProperty(this.getObject(), propertyName);
    }

    // BOOLEAN :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected boolean getBoolean(final String propertyName) {
        return ObjectHelper.getBoolean(this.getObject(), propertyName);
    }

    protected void setBoolean(final String propertyName, final boolean value) {
        ObjectHelper.setBoolean(this.getObject(), propertyName, value);
    }

    // DOUBLE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected double getDouble(final String propertyName) {
        return ObjectHelper.getDouble(this.getObject(), propertyName);
    }

    protected void setDouble(final String propertyName, final double value) {
        ObjectHelper.setDouble(this.getObject(), propertyName, value);
    }

    // INT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected int getInteger(final String propertyName) {
        return ObjectHelper.getInteger(this.getObject(), propertyName);
    }

    protected void setInteger(final String propertyName, final int value) {
        ObjectHelper.setInteger(this.getObject(), propertyName, value);
    }

    // STRING :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected String getString(final String propertyName) {
        return ObjectHelper.getString(this.getObject(), propertyName);
    }

    protected void setString(final String propertyName, final String value) {
        ObjectHelper.setString(this.getObject(), propertyName, value);
    }

    protected void removeProperty(final String propertyName) {
        ObjectHelper.removeProperty(this.getObject(), propertyName);
    }

    public String toString() {
        return super.toString() + ", object[" + this.toStringObject() + "]";
    }
}
