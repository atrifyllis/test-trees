package gr.alx.trees;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newHashSet;

/**
 * Created by alx on 12/3/2016.
 */
public class ComponentHandlerTest {

	private final ComponentHandler cut = new ComponentHandler();

	// @formatter:off
	/**
	 * Testing the following:
	 *
	 *    1        2       3   4   5
	 *  /   \    /   \     |
	 * 2     3  4     5    4
	 *
	 * Should give:
	 *
	 * 4 5
	 * ----
	 * 2 3
	 * ---
	 * 1
	 *
	 */
	// @formatter:on
	@Test
	public void testHandler1() {
		Component c4 = new Component(4);
		Component c5 = new Component(5);

		Component c3 = new Component(3, c4);
		Component c2 = new Component(2, c4, c5);
		Component c1 = new Component(1, c2, c3);

		Set<Component> allComponents = new HashSet<>(Arrays.asList(c1, c2, c3, c4, c5));

		List<Set<Component>> allComponentsByLevel = cut.getComponentsByLevel(allComponents);

		assertThat(allComponentsByLevel).containsExactly(
				newHashSet(newArrayList(c4, c5)),
				newHashSet(newArrayList(c2, c3)),
				newHashSet(newArrayList(c1))
		);

	}
	// @formatter:off
	/**
	 * Testing the following:
	 *
	 *    1        2         3         4        5   6    7
	 *  / | \    /   \     /   \     / | \      |
	 * 5  4  7  3     6   4     7   5  6  7     6
	 *
	 * Should give:
	 *
	 * 6 7
	 * ----
	 * 5
	 * ---
	 * 4
	 * ---
	 * 1 3
	 * ---
	 * 2
	 */
	// @formatter:on
	@Test

	public void testHandler2() {
		Component c7 = new Component(7);
		Component c6 = new Component(6);
		Component c5 = new Component(5, c6);
		Component c4 = new Component(4, c5, c6, c7);
		Component c3 = new Component(3, c4, c7);
		Component c2 = new Component(2, c3, c6);
		Component c1 = new Component(1, c5, c4, c7);

		Set<Component> allComponents = new HashSet<>(Arrays.asList(c1, c2, c3, c4, c5, c6, c7));

		List<Set<Component>> allComponentsByLevel = cut.getComponentsByLevel(allComponents);

		assertThat(allComponentsByLevel).containsExactly(
				newHashSet(newArrayList(c6, c7)),
				newHashSet(newArrayList(c5)),
				newHashSet(newArrayList(c4)),
				newHashSet(newArrayList(c1, c3)),
				newHashSet(newArrayList(c2))
		);
		//cut.printComponentsByLevel(allComponentsByLevel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHandlerCycleException() {
		Component c1 = null;
		Component c4 = new Component(4, c1);
		Component c3 = new Component(3);
		Component c2 = new Component(2, c4);
		c1 = new Component(1, c2, c3);

		Set<Component> allComponents = new HashSet<>(Arrays.asList(c1, c2, c3, c4));

		cut.getComponentsByLevel(allComponents);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHandlerNoZeroDependenciesComponentException() {
		Component c1 = null;
		Component c2 = new Component(2, c1);
		c1 = new Component(1, c2);

		Set<Component> allComponents = new HashSet<>(Arrays.asList(c1, c2));

		cut.getComponentsByLevel(allComponents);
	}

}
