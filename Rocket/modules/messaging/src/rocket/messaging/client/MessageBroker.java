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
package rocket.messaging.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.util.client.Checker;

/**
 * Takes one or more messages and publishes them to their respective consumers
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MessageBroker {
	/**
	 * Only a single instance needs to exist as it is stateless.
	 */
	public MessageBroker() {
		this.setSubscribers(new HashMap());
	}

	public void publishMessages(final List<Message> messages) {
		Checker.notNull("parameter:messages", messages);

		final Iterator<Message> iterator = messages.iterator();
		while (iterator.hasNext()) {
			final Message message = iterator.next();
			this.publish(message);
		}
	}

	public void publish(final Message message) {
		Checker.notNull("parameter:message", message);

		final String destination = message.getDestination();
		final Object payload = message.hasPayload() ? message.getPayload() : null;
		final List subscribers = (List) this.getSubscribers().get(destination);
		if (null != subscribers) {
			final Iterator iterator = subscribers.iterator();
			while (iterator.hasNext()) {
				final TopicSubscriber subscriber = (TopicSubscriber) iterator.next();
				subscriber.onMessage(payload);
			}
		}
	}

	/**
	 * A map which contains a registry of subscribers, key = commandName value =
	 * Command.
	 */
	private Map<String, List<TopicSubscriber>> subscribers;

	protected Map<String, List<TopicSubscriber>> getSubscribers() {
		Checker.notNull("field:subscribers", subscribers);
		return subscribers;
	}

	protected void setSubscribers(final Map<String, List<TopicSubscriber>> subscribers) {
		Checker.notNull("parameter:subscribers", subscribers);
		this.subscribers = subscribers;
	}

	/**
	 * Registers or adds a new Subsriber to the registry.
	 * 
	 * @param name
	 * @param subscriber
	 */
	public void subscribe(final String name, final TopicSubscriber subscriber) {
		Checker.notEmpty("parameter:name", name);
		Checker.notNull("parameter:subscriber", subscriber);

		final Map<String, List<TopicSubscriber>> subscribers = this.getSubscribers();
		List<TopicSubscriber> listeners = subscribers.get(name);
		if (null == listeners) {
			listeners = new ArrayList<TopicSubscriber>();
			subscribers.put(name, listeners);
		}
		listeners.add(subscriber);
	}

	public void unsubscribe(final String name, final TopicSubscriber subscriber) {
		Checker.notEmpty("parameter:name", name);
		Checker.notNull("parameter:subscriber", subscriber);

		final Map<String, List<TopicSubscriber>> subscribers = this.getSubscribers();
		List<TopicSubscriber> listeners = (List<TopicSubscriber>) subscribers.get(name);
		if (null != listeners) {
			listeners.remove(subscriber);

			if (listeners.isEmpty()) {
				subscribers.remove(name);
			}
		}
	}
}
