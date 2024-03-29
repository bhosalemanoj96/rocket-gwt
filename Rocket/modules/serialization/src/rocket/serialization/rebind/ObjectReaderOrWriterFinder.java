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
package rocket.serialization.rebind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.ConcreteTypesImplementingInterfaceVisitor;
import rocket.util.client.Checker;

/**
 * This class is responsible for attempting to find the best ObjectReader/Writer
 * for each of the given types.
 */
abstract public class ObjectReaderOrWriterFinder {

	/**
	 * Builds up a map that contains every type along with its corresponding
	 * Reader/Writer. Types that are not matched by a Reader/Writer will not
	 * appear in the map.
	 * 
	 * @param types
	 * @return
	 */
	public Map<Type, Type> build(final Set<Type> types) {
		Checker.notNull("parameter:types", types);

		final Map<Type, List<Match>> accumulator = new HashMap<Type, List<Match>>();

		final ConcreteTypesImplementingInterfaceVisitor visitor = new ConcreteTypesImplementingInterfaceVisitor() {

			@Override
			protected boolean visit(final Type readerOrWriter) {
				final Map<Type, Match> matches = ObjectReaderOrWriterFinder.this.findMatchingType(readerOrWriter);
				while (true) {
					// nothing to do...
					if (matches.isEmpty()) {
						break;
					}
					// check if any of the matched types overlap any of the
					// serializedtypes.. if result is none do nothing...
					if (false == ObjectReaderOrWriterFinder.this.findSingleMatch(matches, types)) {
						break;
					}

					ObjectReaderOrWriterFinder.this.mergeMatches(matches, accumulator);
					break;
				}
				return false;
			}

			@Override
			protected boolean skipAbstractTypes() {
				return true;
			}
		};
		final Type readerOrWriterInterface = this.getImplementingInterface();
		visitor.start(readerOrWriterInterface);

		return this.finalizeBindings(accumulator);
	}

	/**
	 * Sub classes should return either
	 * {@link rocket.serialization.client.ObjectReader} or
	 * {@link rocket.serialization.client.ObjectWriter} type.
	 * 
	 * @return
	 */
	abstract protected Type getImplementingInterface();

	/**
	 * Given a reader/writer builds a map of all the types that it should apply
	 * too.
	 */
	protected Map<Type, Match> findMatchingType(final Type readerOrWriter) {
		Checker.notNull("parameter:readerOrWriter", readerOrWriter);

		Map<Type, Match> matches = null;

		while (true) {
			final Type type = this.getTypeFromAnnotation(readerOrWriter);
			if (null == type) {
				matches = Collections.<Type,Match>emptyMap();
				break;
			}
			if (false == this.shouldBeSerialized(type)) {
				matches = Collections.<Type,Match>emptyMap();
				break;
			}

			// interface find all implementing interfaces...
			if (type.isInterface()) {
				matches = this.findTypesImplementingInterface(type, readerOrWriter);
				break;
			}
			// find all sub classes...
			matches = new HashMap<Type, Match>();
			matches.put(type, new Match(readerOrWriter, ObjectReaderOrWriterFinder.CLASS_MATCH));
			break;
		}

		return matches;
	}

	/**
	 * Builds up a map that contains the concrete types that implement the given
	 * interface.
	 * 
	 * @param interfacee
	 * @param readerOrWriter
	 * @return
	 */
	protected Map<Type, Match> findTypesImplementingInterface(final Type interfacee, final Type readerOrWriter) {
		Checker.notNull("parameter:interfacee", interfacee);
		Checker.notNull("parameter:readerOrWriter", readerOrWriter);

		final Map<Type, Match> matches = new HashMap<Type, Match>();

		final ConcreteTypesImplementingInterfaceVisitor visitor = new ConcreteTypesImplementingInterfaceVisitor() {

			@Override
			protected boolean skip(final Type interfacee, final Type type) {
				boolean skip = super.skip(interfacee, type);

				if (false == skip) {
					skip = !ObjectReaderOrWriterFinder.this.shouldBeSerialized(type);
				}

				return skip;
			}

			@Override
			protected boolean visit(final Type type) {
				final boolean previouslyVisited = matches.containsKey(type);
				if (false == previouslyVisited) {
					matches.put(type, new Match(readerOrWriter, ObjectReaderOrWriterFinder.INTERFACE_MATCH));
				}
				return previouslyVisited;
			}

			protected boolean skipAbstractTypes() {
				return false;
			}
		};
		visitor.start(interfacee);
		return matches;
	}

	/**
	 * Retrieves the type after reading the type name from an annotation
	 * belonging to the given type.
	 * 
	 * @param type
	 *            The type being checked
	 * @return The located type if an annotation exists on the incoming type.
	 */
	protected Type getTypeFromAnnotation(final Type type) {
		Checker.notNull("parameter:type", type);

		Type typeFromAnnotation = null;
		while (true) {
			// annotation not found...
			final List<String> values = type.getMetadataValues(SerializationConstants.SERIALIZABLE_TYPE);
			if (values.size() == 0) {
				break;
			}
			if (values.size() != 1) {
				throw new SerializationFactoryGeneratorException("Type contains more than one "
						+ SerializationConstants.SERIALIZABLE_TYPE + " annotation.");
			}

			// type must be found if annotation exists...
			final String typeName = (String) values.get(0);
			if (null != typeName) {
				typeFromAnnotation = type.getGeneratorContext().getType(typeName);
				Checker.notNull("Unable to find annotated type \"" + typeName + "\".", typeFromAnnotation);
			}
			break;
		}

		return typeFromAnnotation;
	}

	/**
	 * Loops thru all the types belonging to matches attempting to find at least
	 * one type in serializableTypes set.
	 * 
	 * @param matches
	 * @param serializableTypes
	 * @return
	 */
	protected boolean findSingleMatch(final Map<Type,Match> matches, final Set<Type> serializableTypes) {
		boolean found = false;

		final Iterator<Type> iterator = matches.keySet().iterator();
		while (iterator.hasNext()) {
			final Type type = iterator.next();
			if (serializableTypes.contains(type)) {
				found = true;
				break;
			}
		}

		return found;
	}

	/**
	 * Adds to the list of matches for every type in old matches. If a type
	 * doesnt exist in oldMatch a new entry is created.
	 * 
	 * @param newMatches
	 * @param oldMatches
	 */
	protected void mergeMatches(final Map<Type, Match> newMatches, final Map<Type, List<Match>> oldMatches) {
		// need to check if any of the types in $newMatch exist in $oldMatches

		final Iterator<Map.Entry<Type, Match>> newMatchesIterator = newMatches.entrySet().iterator();
		while (newMatchesIterator.hasNext()) {
			final Map.Entry<Type, Match> entry = newMatchesIterator.next();
			final Type newType = entry.getKey();
			final Match newMatch = entry.getValue();

			List<Match> listOfMatchesForType = (List<Match>) oldMatches.get(newType);
			if (null == listOfMatchesForType) {
				listOfMatchesForType = new ArrayList<Match>();

				oldMatches.put(newType, listOfMatchesForType);
			}
			// only add if newMatch is not the same type as one of oldMatch's
			final Type newMatchReaderOrWriter = newMatch.getReaderWriter();
			final Iterator<Match> iterator = listOfMatchesForType.iterator();
			boolean duplicate = false;
			while (iterator.hasNext()) {
				final Match otherMatch = iterator.next();
				if (newMatchReaderOrWriter.equals(otherMatch.getReaderWriter())) {
					duplicate = true;
					break;
				}
			}

			if (false == duplicate) {
				listOfMatchesForType.add(newMatch);
			}
		}
	}

	private final static int CLASS_MATCH = 2;

	private final static int INTERFACE_MATCH = 1;

	/**
	 * Instances of this class hold just how good a match an annotation is for a
	 * type. The score property is used to allow reader/writers with an exact
	 * class to take priority over a reader/writer that says it handles
	 * interfaces.
	 * 
	 * eg an exact class match will have a score one higher than a subclass of
	 * the very same class.
	 */
	static private class Match {

		Match(final Type readerWriter, final int score) {
			super();
			this.setReaderWriter(readerWriter);
			this.setScore(score);
		}

		Type readerWriter;

		Type getReaderWriter() {
			Checker.notNull("field:readerWriter", readerWriter);
			return this.readerWriter;
		}

		void setReaderWriter(final Type readerWriter) {
			Checker.notNull("parameter:readerWriter", readerWriter);
			this.readerWriter = readerWriter;
		}

		int score;

		int getScore() {
			Checker.notZero("field:score", score);
			return score;
		}

		void setScore(final int score) {
			Checker.notZero("parameter:score", score);
			this.score = score;
		}

		public String toString() {
			return super.toString() + ", type: " + readerWriter + ", score: " + score;
		}
	}

	/**
	 * Comparator which sorts all matches from highest to lowest score.
	 */
	static final Comparator DESCENDING_SCORE_COMPARATOR = new Comparator<Match>() {
		public boolean equals(final Match match, final Match otherMatch) {
			return 0 == this.compare(match, otherMatch);
		}

		public int compare(final Match match, final Match otherMatch) {
			return otherMatch.getScore() - match.getScore();
		}
	};

	/**
	 * Loops thru all types in the incoming map and then attempts to select the
	 * best match, ie assign a reader/writer for a type.
	 * 
	 * @param matches
	 * @return A map with the type as the key and reader/writer as the value.
	 */
	protected Map<Type, Type> finalizeBindings(final Map<Type, List<Match>> matches) {
		Checker.notNull("parameter:matches", matches);

		final Map<Type, Type> finalized = new HashMap<Type, Type>();

		final Iterator<Map.Entry<Type, List<Match>>> entries = matches.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry<Type, List<Match>> entry = entries.next();
			final Type type = entry.getKey();
			final List<Match> matchList = entry.getValue();

			// sort $matchList so the highest score appears first...
			Collections.sort(matchList, ObjectReaderOrWriterFinder.DESCENDING_SCORE_COMPARATOR);

			Match match = (Match) matchList.get(0);
			Type readerWriter = match.getReaderWriter();

			if (matchList.size() > 1) {
				final Match secondMatch = (Match) matchList.get(1);

				final int firstScore = match.getScore();
				final int secondScore = secondMatch.getScore();
				if (firstScore == secondScore) {
					this.throwAmbiguousMatches(type, readerWriter, secondMatch.getReaderWriter());
				}
			}

			finalized.put(type, readerWriter);
		}

		return finalized;
	}

	abstract protected void throwAmbiguousMatches(final Type type, final Type readerOrWriter, final Type secondReaderOrWriter);

	/**
	 * Tests if the given type should be ignored when attempting to match a
	 * ObjectReader/ObjectWriter
	 * 
	 * @return
	 */
	abstract protected boolean shouldBeSerialized(Type type);
}
