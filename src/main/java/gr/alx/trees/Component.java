package gr.alx.trees;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alx on 12/3/2016.
 */
class Component {
	private final int id;

	private final Set<Component> dependencies;

	public Component(int id, Component... components) {
		this.id = id;
		this.dependencies = new HashSet<>(Arrays.asList(components));
	}

	public Component(int id, Set<Component> dependencies) {
		this.id = id;
		this.dependencies = dependencies;
	}

	public int getId() {
		return this.id;
	}

	public Set<Component> getDependencies() {
		return this.dependencies;
	}

	@Override
	public String toString() {
		return "Component{" + "id=" + id + '}';
	}
}
