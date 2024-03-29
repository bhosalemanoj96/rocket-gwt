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
package rocket.generator.rebind.visitor;

import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * This visitor may be used to discover all the concrete types that implement a
 * given interface.
 * 
 * @author Miroslav Pokorny
 */
abstract public class ConcreteTypesImplementingInterfaceVisitor {

	public void start(final Type interfacee) {
		Checker.trueValue("The type " + interfacee + " is not an interface.", interfacee.isInterface());

		final SubTypesVisitor types = new SubTypesVisitor() {

			@Override
			protected boolean visit(final Type type) {
				boolean skipRemaining = false;
				if (false == ConcreteTypesImplementingInterfaceVisitor.this.skip(interfacee, type)) {
					skipRemaining = ConcreteTypesImplementingInterfaceVisitor.this.visit(type);
				}
				return skipRemaining;
			}

			@Override
			protected boolean skipInitialType() {
				return false;
			}
		};
		types.start(interfacee.getGeneratorContext().getObject());
	}

	protected boolean skip(final Type interfacee, final Type type) {
		boolean skip = false;

		while (true) {
			// skip interfaces...
			if (type.isInterface()) {
				skip = true;
				break;
			}

			// skip abstract classes...
			if (type.isAbstract() && this.skipAbstractTypes()) {
				skip = true;
				break;
			}

			// does type implement interfacee ?
			if (false == type.isAssignableTo(interfacee)) {
				skip = true;
			}
			break;
		}

		return skip;
	}

	/**
	 * Each type that implements the given interface is passed by to this
	 * method.
	 * 
	 * @param type
	 * @return return true to skip remaining types.
	 */
	abstract protected boolean visit(final Type type);

	/**
	 * Return true to skip all abstract types and only visit concrete types.
	 * 
	 * @return
	 */
	abstract protected boolean skipAbstractTypes();

}
