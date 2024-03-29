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
package rocket.generator.rebind.type;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.comments.HasComments;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.metadata.HasMetadata;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.generics.GenericType;

/**
 * A NewType represents a mutable Type that is also able to generate code to
 * represent itself.
 * 
 * @author Miroslav Pokorny
 */
public interface NewType extends Type, CodeGenerator, HasComments, HasMetadata {

	void addInterface(Type interfacee);

	Initializer newInitializer();

	void addInitializer(Initializer initializer);

	boolean hasName();

	void setSuperType(Type superType);

	NewNestedType newNestedType();

	void addNestedType(NewNestedType type);

	void addNestedInterfaceType(NewNestedInterfaceType type);

	NewAnonymousNestedType newAnonymousNestedType();

	NewNestedInterfaceType newNestedInterfaceType();

	void addParameterisedType( GenericType genericType );
	
	//TODO DELETE Set getParameterisedTypes();
	
	NewField newField();

	void addField(NewField field);

	NewMethod newMethod();

	void addMethod(NewMethod method);
	
	public void addMetaData(String name, String value);
}
