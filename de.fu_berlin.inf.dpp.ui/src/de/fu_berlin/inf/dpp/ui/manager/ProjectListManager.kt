package de.fu_berlin.inf.dpp.ui.manager

import de.fu_berlin.inf.dpp.filesystem.IResource.FILE
import de.fu_berlin.inf.dpp.filesystem.IResource.FOLDER
import de.fu_berlin.inf.dpp.filesystem.IResource.PROJECT
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import de.fu_berlin.inf.dpp.HTMLUIContextFactory
import de.fu_berlin.inf.dpp.filesystem.IContainer
import de.fu_berlin.inf.dpp.filesystem.IFile
import de.fu_berlin.inf.dpp.filesystem.IProject
import de.fu_berlin.inf.dpp.filesystem.IResource
import de.fu_berlin.inf.dpp.filesystem.IWorkspaceRoot
import de.fu_berlin.inf.dpp.ui.model.ProjectTree
import de.fu_berlin.inf.dpp.ui.model.ProjectTree.Node
import de.fu_berlin.inf.dpp.ui.model.ProjectTree.NodeType

/**
 * This class is responsible for creating and managing the {@link ProjectTree}
 * models for the HTML UI. It takes care of mapping the {@link IProject} and its
 * {@link IResource}s to with a {@link ProjectTree} with its {@link Node}s.
 * <p>
 * Call {@link #createProjectModels()} to create the models once, and use
 * {@link #getProjectModels()} to retrieve them. Call
 * {@link #getAllResources(ProjectTree[])} to get back the actual resources
 * selected for sharing.
 */
// TODO Make this a core facade
class ProjectListManager
/**
 * Created by PicoContainer
 *
 * @param workspaceRoot
 * the workspace root of the current workspace
 * @see HTMLUIContextFactory
 */
	(workspaceRoot: IWorkspaceRoot?) {
	private val workspaceRoot: IWorkspaceRoot? = workspaceRoot
	/**
	 * This is for caching. In theory {@link #getProjectModels()} could call
	 * {@link #createProjectModels()} directly every time.
	 */
	private var projectModels: ArrayList<ProjectTree>? = ArrayList<ProjectTree>()
	/**
	 * Stores the relationship between the UI models instances and the actual
	 * resources, which would otherwise get lost during the Java-JavaScript-Java
	 * handover.
	 */
	private var resourceMap: HashMap<Node, IResource>? = HashMap<Node, IResource>()

	/**
	 * <p>
	 * Creates the {@link ProjectTree} models representing all available
	 * projects in the current workspace. These models can be retrieved through
	 * {@link #getProjectModels()}. While creating the models, a mapping between
	 * every {@link IResource} and its respective {@link Node} will be created
	 * and stored for later usage.
	 * </p>
	 * <p>
	 * If there are no projects inside the workspace, this will create an empty
	 * list of {@link ProjectTree}s. It's up to the caller to handle this state
	 * and inform the user properly.
	 * </p>
	 * <p>
	 * Note that this will recreate the models every time it's called, and
	 * thereby cause an iteration over all files in the workspace.
	 * </p>
	 *
	 * @throws IOException
	 * if a file couldn't be extracted. This error will not be
	 * logged, so it's up to the caller to handle it
	 */
	@Throws(IOException::class)
	fun createProjectModels() {
		this.projectModels = ArrayList<ProjectTree>()
		this.resourceMap = HashMap<Node, IResource>()
		if (workspaceRoot!!.getProjects().size == 0) {
// No projects inside this workspace | IDE
			return
		}
		for (project in workspaceRoot.getProjects()) {
			val root: Node?
			try {
				root = createModel(project)
				val pTree = ProjectTree(root)
				projectModels!!.add(pTree)
				resourceMap!!.put(root, project)
				
			} catch (e: IOException) {
				throw IOException(
					"Failed to build ProjectModel while extracting resources",
					e
				)
			}
		}
	}

	/**
	 * Creates the {@link Node} for a given container (project or folder), while
	 * creating a mapping to its underlying {@link IResource}.
	 *
	 * @param container
	 * the resource to create the model from.
	 * @return the model for the given resource
	 */
	@Throws(IOException::class)
	private fun createModel(container: IContainer?): Node {
// Go through all members and add them to the model recursively
		val members = ArrayList<Node>()
		for (member in container!!.members()) {
			var memberNode: Node? = null
			when (member!!.getType()) {
				PROJECT, FOLDER -> memberNode = createModel(member as IContainer?)
				FILE -> memberNode = createModel(member as IFile?)
			}
			if(memberNode != null){
				members.add(memberNode)	
			}
		}
// We don't expect any other container types besides projects and folder
// here
		val type = if ((container.getType() == FOLDER))
			NodeType.FOLDER
		else
			NodeType.PROJECT
		val node = Node(
			members, container.getFullPath().lastSegment(),
			type, true
		)
		resourceMap!!.put(node, container)
		return node
	}

	/**
	 * Creates the {@link Node} for a given file, while creating a mapping to
	 * its underlying {@link IResource}.
	 *
	 * @param file
	 * the resource to create the model from.
	 * @return the model for the given resource
	 */
	private fun createModel(file: IFile?): Node? {
		val memberNode = Node.fileNode(file!!.getFullPath().lastSegment(), true)
		resourceMap!!.put(memberNode, file)
		return memberNode
	}

	/**
	 * Retrieve the models created by calling {@link #createProjectModels()}.
	 *
	 * @return the project models of the current workspace, or <code>null</code>
	 * if {@link #createProjectModels()} wasn't called yet.
	 */
	fun getProjectModels(): List<ProjectTree>? {
		return ArrayList<ProjectTree>(projectModels)
	}

	/**
	 * Extracts a list of selected resources from the given {@link ProjectTree}
	 * s.
	 *
	 * @param projectTreeModels
	 * the list of {@link ProjectTree}s to extract the resources list
	 * from
	 * @return A list of all selected resources from the given models. Will be
	 * empty if {@link #createProjectModels()} was not called before.
	 */
	fun getAllResources(projectTreeModels: Array<ProjectTree>?): List<IResource>? {
		val resourcesToShare = ArrayList<IResource>()
		for (pTree in projectTreeModels!!) {
			resourcesToShare.addAll(getResources(pTree))
		}
		return resourcesToShare
	}

	private fun getResources(projectTree: ProjectTree): List<IResource> {
		val resources = ArrayList<IResource>()
		for (node in flatten(projectTree)!!) {
			if (!node.isSelectedForSharing)
				continue
			val resource = resourceMap!!.get(node)
			if (resource!!.exists())
				resources.add(resource)
		}
		return resources
	}

	private fun flatten(projectTree: ProjectTree?): List<Node>? {
		val collector = ArrayList<Node>()
		addMembersRecursively(projectTree!!.getRoot(), collector)
		return collector
	}

	private fun addMembersRecursively(currentNode: Node, collector: ArrayList<Node>?) {
		collector!!.add(currentNode)
		for (projectTreeNode in currentNode.members) {
			addMembersRecursively(projectTreeNode, collector)
		}
	}
}