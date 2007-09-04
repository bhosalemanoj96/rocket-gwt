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
package rocket.widget.client.slider;

import rocket.selection.client.Selection;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.widget.client.Composite;
import rocket.widget.client.DivPanel;
import rocket.widget.client.Panel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Common base class for both the Horizontal and Vertical Slider widgets.
 * 
 * It contains both common properties and behaviour with sub-classes required to
 * implement a bare minimum.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class Slider extends Composite {

	/**
	 * Sub classes must return the significant mouse coordinate.
	 * 
	 * @param event
	 * @return
	 */
	abstract protected int getMousePageCoordinate(final Event event);

	/**
	 * Sub classes must return the significant widget coordinate
	 * 
	 * @return
	 */
	abstract protected int getAbsoluteWidgetCoordinate();

	/**
	 * Sub classes must return the name of the style property coordinate
	 * 
	 * @return StyleConstants.LEFT or StyleConstants.TOP
	 */
	abstract protected String getHandleCoordinateStylePropertyName();

	/**
	 * Sub classes must return the slider widget length of the significant
	 * dimension
	 * 
	 * @return
	 */
	abstract protected int getSliderLength();

	/**
	 * Sub classes must return the length of the handle of the significant
	 * dimension
	 * 
	 * @return
	 */
	abstract protected int getHandleLength();

	protected Widget createWidget() {
		final Panel panel = this.createPanel();
		this.setPanel(panel);
		return panel;
	}

	protected void afterCreateWidget() {
		this.setChangeListeners(this.createChangeListeners());
		this.setFocusListeners(this.createFocusListeners());
	}

	protected int getSunkEventsBitMask() {
		return Event.FOCUSEVENTS | Event.ONCHANGE | Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOUT | Event.ONMOUSEMOVE;
	}

	public void onAttach() {
		final int value = this.getValue();
		super.onAttach();
		this.setValue(value);
	}

	// EVENT HANDLING
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * Dispatches to the appropriate method depending on the event type.
	 */
	public void onBrowserEvent(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final int eventType = DOM.eventGetType(event);
			if (eventType == Event.ONMOUSEDOWN) {
				handleMouseDown(event);
				break;
			}

			if (eventType == Event.ONMOUSEOUT) {
				this.handleMouseOut(event);
				break;
			}
			if (eventType == Event.ONMOUSEUP) {
				this.handleMouseUp(event);
			}
			break;
		}

		super.onBrowserEvent(event);
	}

	/**
	 * Dispatches the event to the respective handler depending on whether the
	 * handle or the slider background was clicked.
	 * 
	 * @param event
	 */
	protected void handleMouseDown(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final Element target = DOM.eventGetTarget(event);

			// check if the handle widget has been clicked...
			if (DOM.isOrHasChild(this.getHandle().getElement(), target)) {
				this.handleHandleMouseDown(event);
				break;
			}

			// was the slider background itself clicked ?
			if (DOM.isOrHasChild(this.getElement(), target)) {
				this.handleBackgroundMouseDown(event);
				break;
			}

			// unknown mouseTarget do nothing...
			break;
		}
	}

	/**
	 * Initiates the dragging of the handle until it is released.
	 * 
	 * @param event
	 */
	protected void handleHandleMouseDown(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		if (false == this.hasDraggingEventPreview()) {
			Selection.clearAnySelectedText();
			Selection.disableTextSelection();

			final EventPreview eventPreview = this.createDraggingEventPreview();
			this.setDraggingEventPreview(eventPreview);
			DOM.addEventPreview(eventPreview);

			this.getHandle().addStyleName(this.getSliderDraggingStyleName());
		}
	}

	/**
	 * If the mouseTarget has moved away from the slider cancel any active
	 * timer.
	 * 
	 * @param event
	 */
	protected void handleMouseOut(final Event event) {
		this.clearTimer();
	}

	/**
	 * This method handles any mouseTarget up events.
	 * 
	 * @param event
	 */
	protected void handleMouseUp(final Event event) {
		this.clearTimer();
	}

	/**
	 * The EventPreview object that is following the handle whilst it is being
	 * dragged.
	 */
	private EventPreview draggingEventPreview;

	protected EventPreview getDraggingEventPreview() {
		ObjectHelper.checkNotNull("field:draggingEventPreview", draggingEventPreview);
		return this.draggingEventPreview;
	}

	protected boolean hasDraggingEventPreview() {
		return null != this.draggingEventPreview;
	}

	protected void setDraggingEventPreview(final EventPreview draggingEventPreview) {
		ObjectHelper.checkNotNull("parameter:draggingEventPreview", draggingEventPreview);
		this.draggingEventPreview = draggingEventPreview;
	}

	protected void clearDraggingEventPreview() {
		this.draggingEventPreview = null;
	}

	/**
	 * This EventPreview anonymous class merely delegates to
	 * {@link #handleDraggingEventPreview(Event)}
	 * 
	 * @return
	 */
	protected EventPreview createDraggingEventPreview() {
		final EventPreview draggingEventPreview = new EventPreview() {
			public boolean onEventPreview(final Event event) {
				return handleDraggingEventPreview(event);
			}
		};
		return draggingEventPreview;
	}

	/**
	 * Manages the event type removing the EventPreview when the mouseTarget
	 * button is released and updating the handle via
	 * {@link #handleHandleMouseMove(Event)}
	 * 
	 * @param event
	 * @return When true stops the event from being dispatched to the
	 *         mouseTarget element.
	 */
	protected boolean handleDraggingEventPreview(final Event event) {
		boolean cancelEvent = true;

		while (true) {
			final int type = DOM.eventGetType(event);
			if (type == Event.ONMOUSEMOVE) {
				this.handleMouseMove(event);
				cancelEvent = true;
				break;
			}
			if (type == Event.ONMOUSEUP) {
				this.handleHandleMouseUp(event);
				cancelEvent = false;
				break;
			}
			cancelEvent = true;
			break;
		}
		return !cancelEvent;
	}

	protected void handleMouseMove(final Event event) {
		final int range = this.getSliderLength() - this.getHandleLength();

		int value = this.getMousePageCoordinate(event) - this.getAbsoluteWidgetCoordinate();
		if (value < 0) {
			value = 0;
		}
		if (value > range) {
			value = range;
		}

		final float newValue = (float) value / range * this.getMaximumValue() + 0.5f;
		this.setValue((int) newValue);

		this.clearTimer();
	}

	protected void handleBackgroundMouseDown(final Event event) {
		final int mouse = this.getMousePageCoordinate(event) - this.getAbsoluteWidgetCoordinate() - this.getHandleLength() / 2;
		final HandleSlidingTimer timer = this.getTimer();
		timer.setMouse(mouse);

		handleBackgroundMouseDown(mouse);
	}

	protected void handleBackgroundMouseDown(final int mouse) {
		while (true) {
			final int handle = this.getRelativeHandleCoordinate();
			if (mouse == handle) {
				this.clearTimer();
				break;
			}
			if (mouse < handle) {
				this.handleBeforeHandleMouseDown();
				break;
			}
			this.handleAfterHandleMouseDown();
			break;
		}
	}

	/**
	 * Decreases the value of this slider ensuring that it does not underflow
	 * the minimum value of this slider.
	 */
	protected void handleBeforeHandleMouseDown() {
		int newValue = this.getValue() - this.getDelta();
		if (newValue < 0) {
			newValue = 0;
		}
		final int coordinate = this.getRelativeHandleCoordinate() - 1;
		this.setValue(newValue);
		this.setRelativeHandleCoordinate(coordinate);
	}

	/**
	 * Increases the value of this slider ensuring that it does not exceed the
	 * maximum value of this slider.
	 */
	protected void handleAfterHandleMouseDown() {
		int newValue = this.getValue() + this.getDelta();
		final int maximumValue = this.getMaximumValue();
		if (newValue > maximumValue) {
			newValue = maximumValue;
		}
		final int coordinate = this.getRelativeHandleCoordinate() + 1;
		this.setValue(newValue);
		this.setRelativeHandleCoordinate(coordinate);
	}

	/**
	 * This method is called when the mouseTarget button is let go whilst
	 * dragging the slider handle.
	 * 
	 * @param event
	 */
	protected void handleHandleMouseUp(final Event event) {
		this.getHandle().removeStyleName(this.getSliderDraggingStyleName());

		DOM.removeEventPreview(this.getDraggingEventPreview());
		this.clearDraggingEventPreview();
		Selection.enableTextSelection();
	}

	/**
	 * Sub-classes need to return the style that is added to the handle widget
	 * when it is being dragged or removed when the dragging is stopped.
	 * 
	 * @return
	 */
	protected abstract String getSliderDraggingStyleName();

	/**
	 * A timer is used to simulate multiple clicks when holding down the
	 * mouseTarget button
	 */
	private HandleSlidingTimer timer;

	protected HandleSlidingTimer getTimer() {
		if (false == this.hasTimer()) {
			final HandleSlidingTimer timer = createTimer();
			timer.scheduleRepeating(this.getMouseDownRepeatRate());
			this.setTimer(timer);
		}

		ObjectHelper.checkNotNull("field:timer", timer);
		return timer;
	}

	protected boolean hasTimer() {
		return null != this.timer;
	}

	protected void setTimer(final HandleSlidingTimer timer) {
		ObjectHelper.checkNotNull("parameter:timer", timer);
		this.timer = timer;
	}

	/**
	 * Clears any active timer.
	 */
	protected void clearTimer() {
		if (this.hasTimer()) {
			this.timer.cancel();
		}
		this.timer = null;
	}

	protected HandleSlidingTimer createTimer() {
		return new HandleSlidingTimer();
	}

	protected class HandleSlidingTimer extends Timer {

		public void run() {
			Slider.this.handleBackgroundMouseDown(this.getMouse());
		}

		private int mouse;

		int getMouse() {
			return this.mouse;
		}

		void setMouse(final int target) {
			this.mouse = target;
		}
	}

	/**
	 * This value in milliseconds controls the repetition of mouseTarget down
	 * events within the background area of the slider.
	 * 
	 * Smaller values result in a faster glide of the handle towards the mouse,
	 * whilst larger values result in a slow movement.
	 */
	private int mouseDownRepeatRate;

	public int getMouseDownRepeatRate() {
		PrimitiveHelper.checkGreaterThan("field:mouseDownRepeatRate", mouseDownRepeatRate, 0);
		return this.mouseDownRepeatRate;
	}

	public void setMouseDownRepeatRate(final int mouseDownRepeatRate) {
		PrimitiveHelper.checkGreaterThan("parameter:mouseDownRepeatRate", mouseDownRepeatRate, 0);
		this.mouseDownRepeatRate = mouseDownRepeatRate;
	}

	// WIDGET :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * A panel is used to hold both the sliders element and house the handle
	 * widget.
	 */
	private Panel panel;

	protected Panel getPanel() {
		ObjectHelper.checkNotNull("field:panel", panel);
		return panel;
	}

	protected boolean hasPanel() {
		return null != this.panel;
	}

	protected void setPanel(final Panel panel) {
		ObjectHelper.checkNotNull("parameter:panel", panel);

		this.panel = panel;
	}

	protected Panel createPanel() {
		final Panel panel = new DivPanel();
		final Element element = panel.getElement();
		InlineStyle.setString(element, StyleConstants.POSITION, "relative");
		InlineStyle.setInteger(element, StyleConstants.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(element, StyleConstants.TOP, 0, CssUnit.PX);

		panel.add(new HTML());
		return panel;
	}

	public Widget getHandle() {
		return this.getPanel().get(0);
	}

	public void setHandle(final Widget handle) {
		final Panel panel = this.getPanel();
		final String handleStyle = this.getHandleStyleName();

		final Widget previous = panel.get(0);
		previous.removeStyleName(handleStyle);
		panel.remove(0);

		panel.insert(handle, 0);
		handle.addStyleName(handleStyle);
	}

	abstract String getHandleStyleName();

	// SLIDER ::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * When attached the handle coordinates within the slider is used to
	 * calculate the actual value of the slider. This field is constantly
	 * updated and is used when the widget is not attached.
	 */
	private int value;

	public int getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		final int maximumValue = this.getMaximumValue();
		PrimitiveHelper.checkBetween("parameter:value", value, 0, maximumValue + 1);

		if (this.isAttached()) {
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					Slider.this.setValue0();
				}
			});
		}
		this.value = value;

		this.getChangeListeners().fireChange(this);
	}

	protected void setValue0() {
		final int sliderLength = this.getSliderLength() - this.getHandleLength();
		final int coordinate = this.getValue() * sliderLength / this.getMaximumValue();
		this.setRelativeHandleCoordinate(coordinate);
	}

	/**
	 * The maximum value of the slider. The minimum value is defaulted to 0.
	 * Clients must adjust this value if they wish to use a different range of
	 * values.
	 */
	private int maximumValue;

	public int getMaximumValue() {
		PrimitiveHelper.checkGreaterThan("field:maximumValue", maximumValue, 0);
		return this.maximumValue;
	}

	public void setMaximumValue(final int maximumValue) {
		PrimitiveHelper.checkGreaterThan("parameter:maximumValue", maximumValue, 0);
		this.maximumValue = maximumValue;
	}

	/**
	 * The amount the value jumps. The value must be 1 or more.
	 */
	private int delta;

	public int getDelta() {
		PrimitiveHelper.checkGreaterThan("field:delta", delta, 0);
		return this.delta;
	}

	public void setDelta(final int delta) {
		PrimitiveHelper.checkGreaterThan("parameter:delta", delta, 0);
		this.delta = delta;
	}

	public void addChangeListener(final ChangeListener changeListener) {
		super.addChangeListener(changeListener);
	}

	public void removeChangeListener(final ChangeListener changeListener) {
		super.removeChangeListener(changeListener);
	}

	public void addFocusListener(final FocusListener focusListener) {
		super.addFocusListener(focusListener);
	}

	public void removeFocusListener(final FocusListener focusListener) {
		super.removeFocusListener(focusListener);
	}

	protected int getRelativeHandleCoordinate() {
		final Element element = this.getHandle().getElement();
		return InlineStyle.getInteger(element, this.getHandleCoordinateStylePropertyName(), CssUnit.PX, 0);
	}

	protected void setRelativeHandleCoordinate(final int coordinate) {
		final Element element = this.getHandle().getElement();
		InlineStyle.setString(element, StyleConstants.POSITION, "absolute");
		InlineStyle.setInteger(element, this.getHandleCoordinateStylePropertyName(), coordinate, CssUnit.PX);
	}

	public String toString() {
		return super.toString() + ", value: " + this.value;
	}
}
