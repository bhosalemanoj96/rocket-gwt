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
package rocket.generator.rebind;

import java.io.OutputStream;
import java.util.Set;

import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;

/**
 * Defines a generator context.
 * 
 * @author Miroslav Pokorny
 */
public interface GeneratorContext {

	String getGeneratedTypeName(final String name, final String suffix);

	Type findType(final String name);

	Type getType(final String name);

	Type getBoolean();

	Type getByte();

	Type getShort();

	Type getInt();

	Type getLong();

	Type getFloat();

	Type getDouble();

	Type getChar();

	Type getVoid();

	Type getObject();

	Type getString();

	void addType(final Type type);

	Set<NewType> getNewTypes();

	NewConcreteType newConcreteType(String name);

	NewInterfaceType newInterfaceType(String name);

	String getPackageName(final String fullyQualifiedClassName);

	String getSimpleClassName(final String fullyQualifiedClassName);

	void trace(final String message);

	void trace(final String message, final Throwable throwable);

	void debug(final String message);

	void debug(final String message, final Throwable throwable);

	void info(final String message);

	void info(final String message, final Throwable throwable);

	void warn(final String message);

	void warn(final String message, final Throwable throwable);

	void error(final String message);

	void error(final String message, final Throwable throwable);

	void branch();

	/**
	 * Causes the message to create a branch. The actual message will be
	 * buffered until a leaf message appears.
	 */
	void delayedBranch();

	void unbranch();

	boolean isDebugEnabled();

	boolean isInfoEnabled();

	boolean isTraceEnabled();

	String getProperty(String propertyName);

	Generator getGenerator();

	/**
	 * Factory which may be used to attempt to create a resource that will
	 * appear onthe public path of a hosted mode session or compilation.
	 * 
	 * @param filename
	 * @return null if the resource already existed, or stream to write the file
	 *         contents.
	 */
	OutputStream createResource(String filename);

	/**
	 * Helper which writes a resource if it doesnt already exist generating a
	 * strong filename to guarantee uniqueness.
	 * 
	 * @param contents
	 * @param suffix
	 *            A suffix which is appended to the hash. Typically this will
	 *            include "nocache." + the file extension.
	 * @return The partial path of the written file.
	 */
	String createResource(byte[] contents, String suffix);
}
