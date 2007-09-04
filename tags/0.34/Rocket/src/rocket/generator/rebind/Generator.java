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

import java.io.InputStream;
import java.io.PrintWriter;

import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.TreeLogger;

/**
 * Convenient base class for any generator. It includes code to setup a context,
 * the initial test if a generated type for the given typeName exists etc.
 * 
 * @author Miroslav Pokorny
 */
abstract public class Generator extends com.google.gwt.core.ext.Generator {

	/**
	 * Starts the code generation process including the construction of a new
	 * Context.
	 */
	public String generate(final TreeLogger logger, final com.google.gwt.core.ext.GeneratorContext generatorContext, final String typeName) {
		final GeneratorContext context = this.createGeneratorContext();
		context.setGeneratorContext(generatorContext);
		context.setLogger(logger);
		context.setGenerator(this);
		this.setGeneratorContext(context);

		return this.createNewTypeIfNecessary(typeName);
	}

	/**
	 * Tests if a Type already exists and if it doesnt invokes
	 * {@link #assembleNewType(Context, String, String)} which will build a
	 * {@link NewConcreteType}
	 * 
	 * @param typeName
	 * @return
	 */
	public String createNewTypeIfNecessary(final String typeName) {
		final GeneratorContext context = this.getGeneratorContext();
		final String newTypeName = context.getGeneratedTypeName(typeName);
		String bindTypeName = newTypeName;

		context.info("Recieved type [" + typeName + "]");
		final long started = System.currentTimeMillis();

		final PrintWriter printWriter = context.tryCreateTypePrintWriter(newTypeName);
		if (null != printWriter) {
			try {
				final NewConcreteType newType = this.assembleNewType(typeName, newTypeName);
				if (null != newType) {
					context.info("Completed assembling new type [" + newTypeName + "]");
					newType.write(printWriter);

					final long now = System.currentTimeMillis();

					context.info("Finished writing new type [" + newTypeName + "], " + (now - started) + " millis taken.");
					bindTypeName = newTypeName;
				} else {
					context.info("No type was assembled for [" + newTypeName + "]");
					bindTypeName = null;
				}

			} catch (final GeneratorException rethrow) {
				context.error("Problem whilst running generator for type [" + typeName + "]", rethrow);
				throw rethrow;

			} catch (final Throwable caught) {
				context.error("Unexpected problem whilst running generator for type [" + typeName + "]", caught);
				throw new GeneratorException(caught);
			}
		} else {
			context.info("Skipping generation step, will use existing type instead.");
		}

		context.info("Will bind [" + typeName + "] to [" + (bindTypeName == null ? typeName : bindTypeName) + "]");
		return bindTypeName;
	}

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		return this.assembleNewType(this.getGeneratorContext().getType(typeName), newTypeName);
	}

	/**
	 * Sub-classes must create a new NewConcreteType, add constructors, methods,
	 * fields etc.
	 * 
	 * @param type
	 * @param newTypeName
	 * @return
	 */
	abstract protected NewConcreteType assembleNewType(final Type type, final String newTypeName);

	private GeneratorContext generatorContext;

	public GeneratorContext getGeneratorContext() {
		ObjectHelper.checkNotNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final GeneratorContext generatorContext) {
		ObjectHelper.checkNotNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	/**
	 * Factory method which must be overridden by sub-classes to create an
	 * appropriate Context.
	 * 
	 * @return
	 */
	abstract protected GeneratorContext createGeneratorContext();

	/**
	 * Helper which converts a type into a filename that may be loaded from the
	 * classpath.
	 * 
	 * @param type
	 * @param fileExtension
	 * @return
	 */
	public String getResourceName(final Class type, final String fileExtension) {
		ObjectHelper.checkNotNull("parameter:type", type);
		StringHelper.checkNotNull("parameter:fileExtension", fileExtension);

		return this.getResourceName(type.getPackage(), type.getSimpleName() + '.' + fileExtension);
	}

	/**
	 * Helper which takes a package and filename within that package returning a
	 * filename that may be loaded from the classpath
	 * 
	 * @param javaLangPackage
	 * @param fileName
	 * @return
	 */
	public String getResourceName(final java.lang.Package javaLangPackage, final String fileName) {
		ObjectHelper.checkNotNull("parameter:javaLangPackage", javaLangPackage);
		StringHelper.checkNotNull("parameter:fileName", fileName);

		final String resourceName = '/' + javaLangPackage.getName().replace('.', '/') + '/' + fileName;
		return resourceName;
	}

	/**
	 * Helper which converts a type into a filename that may be loaded from the
	 * classpath.
	 * 
	 * @param type
	 * @param fileExtension
	 * @return
	 */
	public String getResourceName(final Type type, final String fileExtension) {
		ObjectHelper.checkNotNull("parameter:type", type);
		StringHelper.checkNotNull("parameter:fileExtension", fileExtension);

		return this.getResourceName(type.getPackage(), type.getSimpleName() + '.' + fileExtension);
	}

	/**
	 * Helper which takes a package and filename within that package returning a
	 * filename that may be loaded from the classpath
	 * 
	 * @param packagee
	 * @param fileName
	 * @return
	 */
	public String getResourceName(final Package packagee, final String fileName) {
		ObjectHelper.checkNotNull("parameter:package", packagee);
		StringHelper.checkNotNull("parameter:fileName", fileName);

		final String resourceName = '/' + packagee.getName().replace('.', '/') + '/' + fileName;
		return resourceName;
	}

	public String getResourceNameFromGeneratorPackage(final String fileName) {
		return this.getResourceName(this.getClass().getPackage(), fileName);
	}

	/**
	 * Retrieves a resource by name
	 * 
	 * @param resourceName
	 * @return
	 */
	public InputStream getResource(final String resourceName) {
		StringHelper.checkNotNull("parameter:resourceName", resourceName);

		final InputStream inputStream = Object.class.getResourceAsStream(resourceName);
		if (null == inputStream) {
			this.throwUnableToLoadResource(resourceName);
		}
		return inputStream;
	}

	protected void throwUnableToLoadResource(final String resourceName) {
		throw new GeneratorException("Unable to load resource with a filename [" + resourceName + "]");
	}
}
