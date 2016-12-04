package gr.alx.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by alx on 12/4/2016.
 */
class ComponentHandler {

	/**
	 * Finds the components that have no dependencies and then calls the recursive method {@lin
	 * #findNextLevelComponents} which does the rest of the processing.
	 * <p/>
	 * throws {@link IllegalArgumentException} if a cycle is found from the beginning, i.e.: <br/>
	 *
	 * 1&nbsp;&nbsp;&nbsp;2<br/> |&nbsp;&nbsp;&nbsp;|<br/> 2&nbsp;&nbsp;1
	 */
	public List<Set<Component>> getComponentsByLevel(Set<Component> allComponents) {
		List<Set<Component>> componentsByLevel = new ArrayList<>();

		Set<Component> zeroDependenciesComponents = getZeroDependenciesComponents(allComponents);
		if (zeroDependenciesComponents.isEmpty()) {
			throw new IllegalArgumentException("The input components contain cyclic dependencies");
		}

		componentsByLevel.add(zeroDependenciesComponents);
		allComponents.removeAll(zeroDependenciesComponents);

		return findNextLevelComponents(allComponents, componentsByLevel);
	}

	/**
	 * Retrieve components which have no dependencies
	 */
	private Set<Component> getZeroDependenciesComponents(Set<Component> components) {
		return components.stream()
				.filter(c -> c.getDependencies().isEmpty())
				.collect(Collectors.toSet());
	}

	/**
	 * Recursive method which, given that components have no been assigned to a "build level" yet:<br/>
	 *
	 * 1) checks which of them can be built on the next level,<br/>
	 * 2) updates the list of levels and the set of remaining components and<br/>
	 * 3) calls itself with the updated parameters.
	 * <p/>
	 * throws {@link IllegalArgumentException} if a cycle is found, i.e.: <br/>
	 *
	 * 1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2&nbsp;&nbsp;&nbsp;3&nbsp;&nbsp;&nbsp;4 <br/>
	 * |&nbsp;\&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br/>
	 * 2&nbsp;3&nbsp;&nbsp;4&nbsp;&nbsp;&nbsp;1<br/>
	 * <br/>
	 * the result levels will be:<br/>
	 *
	 * 3<br/>
	 * ---<br/>
	 * no elements found, exception thrown<br/>
	 * ---
	 */
	private List<Set<Component>> findNextLevelComponents(Set<Component> remainingComponents, List<Set<Component>> componentsByLevel) {
		if (remainingComponents.isEmpty()) {
			printComponentsByLevel(componentsByLevel);
			return componentsByLevel;
		}

		Set<Component> nextLevelComponents = remainingComponents.stream()
				.filter(byAllDependenciesOnPreviousLevels(componentsByLevel))
				.collect(Collectors.toSet());
		if (nextLevelComponents.isEmpty()) {
			throw new IllegalArgumentException("The input components contain cyclic dependencies");
		}

		componentsByLevel.add(nextLevelComponents);
		remainingComponents.removeAll(nextLevelComponents);

		return findNextLevelComponents(remainingComponents, componentsByLevel);
	}

	/**
	 * Checks if the dependencies of a component are ALL included in previous levels components. If
	 * all of them are found this means the component can be built so it is included in next level
	 * components.
	 */
	private Predicate<Component> byAllDependenciesOnPreviousLevels(List<Set<Component>> componentsByLevel) {
		Set<Component> previousLevelsComponents = flattenLevelComponents(componentsByLevel);
		return c -> previousLevelsComponents.containsAll(c.getDependencies());
	}

	/**
	 * Flattens the list of sets of components to a set of components
	 */
	private Set<Component> flattenLevelComponents(List<Set<Component>> componentsByLevel) {
		return componentsByLevel.stream()
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}


	void printComponentsByLevel(List<Set<Component>> allComponentsByLevel) {
		allComponentsByLevel.forEach(lc -> {
			String level = lc.stream()
					.map(c -> Integer.toString(c.getId()))
					.collect(Collectors.joining(","));
			System.out.println(level);
		});
	}
}
