/*
 * Copyright (c) 2012, Stefan Lange
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Stefan Lange nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.radicalfish.debug;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.AnimationState;
import de.matthiasmann.twl.BorderLayout;
import de.matthiasmann.twl.BorderLayout.Location;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.InfoWindow;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ProgressBar;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.SplitPane;
import de.matthiasmann.twl.SplitPane.Direction;
import de.matthiasmann.twl.TableBase.Callback;
import de.matthiasmann.twl.TableBase.KeyboardSearchHandler;
import de.matthiasmann.twl.TableBase.StringCellRenderer;
import de.matthiasmann.twl.TableRowSelectionManager;
import de.matthiasmann.twl.Timer;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.AbstractTreeTableModel;
import de.matthiasmann.twl.model.AbstractTreeTableNode;
import de.matthiasmann.twl.model.SortOrder;
import de.matthiasmann.twl.model.TableSelectionModel;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import de.matthiasmann.twl.model.TreeTableModel;
import de.matthiasmann.twl.model.TreeTableNode;
import de.matthiasmann.twl.renderer.AnimationState.StateKey;
import de.radicalfish.assets.Assets;

/**
 * {@link AssetsViewer} lets you visualize and analyze all assets mapped in the given {@link Assets} instance.
 * <p>
 * The widget does not display the assets in a particular order. On creation it will load all by getting the key array
 * via {@link AssetManager#getAssetNames()}. When you unload or reload a texture it will be added to the bottom of the
 * list.
 * <p>
 * The details display all methods that start with "get", have a return type and do not require a parameter. The
 * {@link Object#getClass()} method will be ignored. As a neat feature the details view supports multi line for the
 * key-value pairs. so if you have a multi line getter it will display the string in a nice box
 * <p>
 * The {@link AssetsViewer} will try to inform you about losing dependencies if it can. If you unload a dependency it
 * will still be in the {@link Assets} instance. In this case the viewer will tell you that we have a missing
 * dependency. It will not fix the issue for you! (how should it eh?) However if you reload a asset with the same name
 * as the missing dependency the viewer will inform you that there is an asset that matches the dependency but it is not
 * assigned to the actual object which needs the asset.
 * <p>
 * Also you can search for assets by just typing in a character. Use the arrow keys to move along assets that match the
 * typed string. Press escape to leave the serach window.
 * <p>
 * Press the column header to sort them by name (or number for the reference count column).
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 25.09.2012
 */
public class AssetsViewer extends ResizableFrame {
	
	public enum SortColumn {
		NAME(NameComparator.INSTANCE), //
		TYPE(TypeComparator.INSTANCE), //
		STATUS(StatusComparator.INSTANCE), //
		REFCOUNT(RefComparator.INSTANCE);
		
		final Comparator<AssetsNode> comparator;
		
		SortColumn(Comparator<AssetsNode> comparator) {
			this.comparator = comparator;
		}
	}
	
	private final Assets assets;
	
	private TreeTable table;
	private AssetsTree model;
	
	private ProgressBar totalbar;
	private Label label_totalbar;
	private ScrollPane detailPane;
	private DetailsWidget details;
	private Spoiler spoiler;
	private SplitPane pane;
	
	private SortColumn sortColumn = SortColumn.NAME;
	private SortOrder sortOrder = SortOrder.ASCENDING;
	
	private ArrayList<String> temp = new ArrayList<String>();
	private ArrayList<String> temp2 = new ArrayList<String>();
	
	private int assetsCount;
	private float progress;
	
	/**
	 * Do not use this C'Tor. It's only there for testing the widget in the ThemeEditor. This will create an
	 * {@link Assets} instance with no content.
	 */
	public AssetsViewer() {
		this(new Assets());
		// for testing
		// model.insert("Bla", "Test", 2).insert("Bla 2", "Test Deep", 5);
		// model.insert("FBla", "Test", 2).insert("Bla 2", "Test Deep", 5);
		// model.insert("eeeeBla", "Test", 2).insert("Bla 2", "Test Deep", 5);
		// model.insert("jjkBla", "Test", 2).insert("Bla 2", "Test Deep", 5);
	}
	public AssetsViewer(Assets assets) {
		this.assets = assets;
		createPanel();
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	protected void paintOverlay(GUI gui) {
		super.paintOverlay(gui);
		if (progress != assets.getProgress()) {
			totalbar.setValue(assets.getProgress());
			progress = assets.getProgress();
		}
		if (assetsCount > assets.getLoadedAssets()) {
			removeAssets();
			// reloadAssets();
			updateAssetsCount();
		} else if (assetsCount < assets.getLoadedAssets()) {
			addAssets();
			updateAssetsCount();
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("resizableframe-title");
		setResizableAxis(ResizableAxis.BOTH);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		setTitle("Assets");
		setSize(350, 400);
		setPosition(5, 5);
		
		details = new DetailsWidget();
		
		pane = new SplitPane();
		pane.setDirection(Direction.VERTICAL);
		pane.setRespectMinSizes(true);
		
		BorderLayout bl = new BorderLayout();
		
		pane.add(createTreeTable());
		pane.add(createDetailSpoiler());
		
		pane.setSplitPosition(5000);
		
		bl.add(pane);
		bl.add(createProgress(), Location.NORTH);
		add(bl);
		
		initAssetsTree();
		updateAssetsCount();
		
		assetsCount = assets.getLoadedAssets();
	}
	private Widget createTreeTable() {
		final TableSingleSelectionModel select = new TableSingleSelectionModel();
		model = new AssetsTree();
		table = new TreeTable(model);
		table.setTheme("table-assets");
		table.setDefaultSelectionManager();
		table.setSelectionManager(new TableRowSelectionManager(select));
		table.registerCellRenderer(AssetText.class, new AssetsTextRenderer());
		table.setVaribleRowHeight(true);
		table.addCallback(new Callback() {
			public void mouseDoubleClicked(int row, int column) {
				if (row >= 0 && row < table.getNumRows()) {
					AssetsNode node = (AssetsNode) table.getNodeFromRow(row);
					if (node.flag == AssetsNode.FLAG_OKAY) {
						details.createPairs(assets.get(node.name));
					} else if (node.flag == AssetsNode.FLAG_MISS_DEP) {
						details.createError(node.info);
					} else if (node.flag == AssetsNode.FLAG_DEP_INSTANCE_MISMATCH) {
						details.createWarning(node.info);
					}
				} else {
					details.createPairs(null);
				}
				spoiler.invalidateLayout();
			}
			public void mouseRightClick(int row, int column, Event evt) {}
			public void columnHeaderClicked(int column) {
				SortColumn selected = SortColumn.values()[column];
				if (selected == sortColumn) {
					setSortOrder(sortOrder.invert());
				} else {
					setSortColumns(selected);
				}
			}
		});
		
		TopLevelTreeTableSearchWindow search = new TopLevelTreeTableSearchWindow(table, select);
		search.setModel(model, 0);
		table.setKeyboardSearchHandler(search);
		
		ScrollPane scrollPane = new ScrollPane(table);
		scrollPane.setFixed(Fixed.HORIZONTAL);
		scrollPane.setTheme("tableScrollPane");
		
		return scrollPane;
	}
	private Widget createProgress() {
		DialogLayout dl = new DialogLayout();
		dl.setTheme("");
		
		label_totalbar = new Label("");
		totalbar = new ProgressBar();
		
		Label l2 = new Label("Loaded: ");
		Group h3 = dl.createSequentialGroup().addWidget(l2, Alignment.LEFT).addWidget(label_totalbar, Alignment.FILL);
		Group v3 = dl.createParallelGroup().addWidget(l2, Alignment.LEFT).addWidget(label_totalbar, Alignment.FILL);
		
		Group h1 = dl.createParallelGroup().addGroup(h3).addWidgets(totalbar);
		Group v1 = dl.createSequentialGroup().addGroup(v3).addGap(2, 2, 2).addWidget(totalbar, Alignment.FILL);
		
		dl.setHorizontalGroup(h1);
		dl.setVerticalGroup(v1);
		return dl;
	}
	private Widget createDetailSpoiler() {
		detailPane = new ScrollPane(details);
		detailPane.setTheme("scrollpane-assets");
		spoiler = new Spoiler(detailPane, "Details");
		spoiler.setTheme("spoiler-assets");
		spoiler.setCallback(new Spoiler.Callback() {
			public void viewChanged(boolean visible, boolean afterLayout) {
				if (!afterLayout) {
					if (!visible) {
						pane.setSplitPosition(5000);
					}
				}
			}
		});
		return spoiler;
	}
	
	private void updateAssetsCount() {
		assetsCount = assets.getLoadedAssets();
		label_totalbar.setText(assets.getLoadedAssets() + " / " + assets.getTotalAssets());
	}
	
	private void initAssetsTree() {
		Array<String> keys = assets.getAssetNames();
		for (String key : keys) {
			AssetsNode node = model.insert(key, assets.getAssetType(key).getSimpleName(), assets.getReferenceCount(key));
			int size = assets.getDependencies(key) == null ? 0 : assets.getDependencies(key).size;
			if (size > 0) {
				initDependecies(node, assets.getDependencies(key));
			}
		}
	}
	private void initDependecies(AssetsNode parent, Array<String> deps) {
		for (String key : deps) {
			AssetsNode node = null;
			if (assets.isLoaded(key)) {
				node = parent.insert(key, assets.getAssetType(key).getSimpleName(), assets.getReferenceCount(key));
			} else {
				node = parent.insert(key, "UNKOWN", "Missing\nDependency", 0);
				node.info = "The dependeny was removed!\nThis can lead to errors in rendering!";
				node.flag = AssetsNode.FLAG_MISS_DEP;
				continue;
			}
			int size = assets.getDependencies(key) == null ? 0 : assets.getDependencies(key).size;
			if (size > 0) {
				initDependecies(node, assets.getDependencies(key));
			}
		}
	}
	
	private void addAssets() {
		Array<String> keys = assets.getAssetNames();
		temp.clear();
		for (String key : keys) {
			boolean isIn = false;
			for (int i = 0; i < model.getNumChildren(); i++) {
				AssetsNode node = (AssetsNode) model.getChild(i);
				if (node.name.equals(key)) {
					isIn = true;
				}
			}
			if (!isIn) {
				temp.add(key);
			}
		}
		
		// add the new assets
		for (String toAdd : temp) {
			AssetsNode node = model.insert(toAdd, assets.getAssetType(toAdd).getSimpleName(), assets.getReferenceCount(toAdd));
			int size = assets.getDependencies(toAdd) == null ? 0 : assets.getDependencies(toAdd).size;
			if (size > 0) {
				initDependecies(node, assets.getDependencies(toAdd));
			}
			// walk through all dependencies and check if a dependency was marked with the FLAG_MISS_DEP flag
			// if so, mark it with the FLAG_DEP_INSTANCE_MISMATCH
			for (int i = 0; i < model.getNumChildren(); i++) {
				AssetsNode an = (AssetsNode) model.getChild(i);
				if (an.getNumChildren() > 0) {
					checkDependenciesOnAdd(an, toAdd);
				}
			}
		}
	}
	private void checkDependenciesOnAdd(AssetsNode parent, String dep) {
		for (int i = 0; i < parent.getNumChildren(); i++) {
			AssetsNode node = (AssetsNode) parent.getChild(i);
			if (node.flag == AssetsNode.FLAG_MISS_DEP && node.name.equals(dep)) {
				// match, the node equals this means the
				node.type = assets.getAssetType(dep).getSimpleName();
				node.status = "Reload Asset\nMismatch";
				node.refCount = assets.getReferenceCount(dep);
				node.info = "The dependency was reloaded\nbut the instances are not equal!";
				node.flag = AssetsNode.FLAG_DEP_INSTANCE_MISMATCH;
				node.invokeUpdate();
			}
			if (node.getNumChildren() > 0) {
				checkDependenciesOnAdd(node, dep);
			}
		}
	}
	
	private void removeAssets() {
		// remove from the root and save the name to make the actual removing happen
		temp2.clear();
		for (int i = model.getNumChildren() - 1; i >= 0; i--) {
			AssetsNode node = (AssetsNode) model.getChild(i);
			if (!assets.isLoaded(node.name)) { // remove the node!
				temp2.add(node.name);
				model.remove(i);
			}
			
		}
		
		// walk trough the remaining assets and check all assets on missing dependencies
		for (String toRemove : temp2) {
			for (int i = 0; i < model.getNumChildren(); i++) {
				AssetsNode node = (AssetsNode) model.getChild(i);
				if (node.getNumChildren() > 0) {
					updateDependenciesOnRemove(toRemove, node);
				}
			}
		}
	}
	private void updateDependenciesOnRemove(String toRemove, AssetsNode parent) {
		for (int i = 0; i < parent.getNumChildren(); i++) {
			AssetsNode node = (AssetsNode) parent.getChild(i);
			if (node.name.equals(toRemove)) {
				// Mark the node
				node.type = "UNKOWN";
				node.status = "Missing\nDependency";
				node.refCount = 0;
				node.info = "The dependeny was removed!\nThis can lead to errors in rendering!";
				node.flag = AssetsNode.FLAG_MISS_DEP;
				node.invokeUpdate();
				node.removeLeafs();
			}
			if (node.getNumChildren() > 0) {
				updateDependenciesOnRemove(toRemove, node);
			}
		}
	}
	
	private void setSortOrder(SortOrder order) {
		if (sortOrder != order) {
			sortOrder = order;
			sort();
		}
	}
	private void setSortColumns(SortColumn column) {
		if (sortColumn != column) {
			sortColumn = column;
			sort();
		}
	}
	
	private void sort() {
		table.setColumnSortOrderAnimationState(sortColumn.ordinal(), sortOrder);
		TableSelectionModel sel = table.getSelectionManager().getSelectionModel();
		AssetsNode lead = model.getNode(sel.getLeadIndex());
		AssetsNode anchor = model.getNode(sel.getAnchorIndex());
		AssetsNode selection = model.getNode(sel.getFirstSelected());
		
		AssetsNode[] nodes = model.getAll();
		Arrays.sort(nodes, 0, nodes.length, sortOrder.map(sortColumn.comparator));
		model.setAll(nodes);
		
		int idx = model.findNode(selection);
		sel.setSelection(idx, idx);
		sel.setLeadIndex(model.findNode(lead));
		idx = model.findNode(anchor);
		sel.setAnchorIndex(idx);
		table.scrollToRow(Math.max(0, idx));
		
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static class DetailsWidget extends ColumnLayout {
		private Columns col;
		
		public DetailsWidget() {
			col = getColumns("Name", "Values");
		}
		
		public void createPairs(Object asset) {
			removeAllChildren();
			getRootPanel().clearPanel();
			if (asset == null) {
				return;
			}
			for (Method m : asset.getClass().getMethods()) {
				if (m.getName().startsWith("get") && !m.getName().equals("getClass")) {
					if (m.getParameterTypes().length == 0) {
						Row row = addRow(col);
						row.add(new Label(m.getName().substring(3) + ":  "), Alignment.TOPLEFT);
						try {
							row.add(new Label(m.invoke(asset).toString()), Alignment.TOPLEFT);
						} catch (Exception e) {}
					}
				}
			}
		}
		public void createError(String error) {
			removeAllChildren();
			getRootPanel().clearPanel();
			Row row = addRow(col);
			
			Label l1 = new Label("ERROR: ");
			l1.getAnimationState().setAnimationState(AssetsTextRenderer.STATE_ERROR, true);
			Label l2 = new Label(error);
			
			row.add(l1, Alignment.TOPLEFT);
			row.add(l2, Alignment.TOPLEFT);
		}
		public void createWarning(String warn) {
			removeAllChildren();
			getRootPanel().clearPanel();
			Row row = addRow(col);
			
			Label l1 = new Label("WARNING: ");
			l1.getAnimationState().setAnimationState(AssetsTextRenderer.STATE_WARN, true);
			Label l2 = new Label(warn);
			
			row.add(l1, Alignment.TOPLEFT);
			row.add(l2, Alignment.TOPLEFT);
			
			row = addRow(col);
		}
		
	}
	
	private static class AssetsTree extends AbstractTreeTableModel {
		final String[] columns = new String[] { "Name", "Type", "Status", "Ref" };
		
		public AssetsNode insert(String name, String type, int refCount) {
			return insert(name, type, refCount, getNumChildren());
		}
		public AssetsNode insert(String name, String type, int refCount, int index) {
			AssetsNode n = new AssetsNode(this, name, type, refCount);
			insertChild(n, index);
			return n;
		}
		
		public void setAll(AssetsNode[] all) {
			removeAllChildren();
			for (int i = 0; i < all.length; i++) {
				insertChild(all[i], getNumChildren());
			}
		}
		public AssetsNode[] getAll() {
			AssetsNode[] a = new AssetsNode[getNumChildren()];
			for (int i = 0; i < getNumChildren(); i++) {
				a[i] = (AssetsNode) getChild(i);
			}
			return a;
		}
		
		public AssetsNode getNode(int row) {
			if (row >= 0 && row < getNumChildren()) {
				return (AssetsNode) getChild(row);
			} else {
				return null;
			}
		}
		public int findNode(AssetsNode node) {
			for (int i = 0; i < getNumChildren(); i++) {
				if (getChild(i).equals(node)) {
					return i;
				}
			}
			return -1;
		}
		
		public void remove(int idx) {
			removeChild(idx);
		}
		
		public int getNumColumns() {
			return columns.length;
		}
		public String getColumnHeaderText(int column) {
			return columns[column];
		}
		
	}
	private static class AssetsNode extends AbstractTreeTableNode {
		
		public static final int FLAG_OKAY = 0;
		public static final int FLAG_MISS_DEP = 1;
		public static final int FLAG_DEP_INSTANCE_MISMATCH = 2;
		
		String name, type, status;
		String info = "";
		int refCount;
		int flag = FLAG_OKAY;
		
		public AssetsNode(TreeTableNode parent, String name, String type, int refCount) {
			this(parent, name, type, "Loaded", refCount);
		}
		public AssetsNode(TreeTableNode parent, String name, String type, String status, int refCount) {
			super(parent);
			this.name = name;
			this.type = type;
			this.status = status;
			this.refCount = refCount;
			setLeaf(true);
		}
		
		public AssetsNode insert(String name, String type, int refCount) {
			return insert(name, type, "Loaded", refCount);
		}
		public AssetsNode insert(String name, String type, String status, int refCount) {
			return insert(name, type, status, refCount, getNumChildren());
		}
		public AssetsNode insert(String name, String type, String status, int refCount, int index) {
			AssetsNode n = new AssetsNode(this, name, type, status, refCount);
			insertChild(n, index);
			setLeaf(false);
			return n;
		}
		
		public void removeLeafs() {
			removeAllChildren();
		}
		public void invokeUpdate() {
			fireNodeChanged();
		}
		public Object getData(int column) {
			switch (column) {
				case 0:
					return name;
				case 1:
					return type;
				case 2:
					return getStatusCell();
				case 3:
					return refCount;
			}
			return "???";
		}
		
		private Object getStatusCell() {
			switch (flag) {
				case FLAG_OKAY:
					return AssetText.apply(status, AssetText.OKAY);
				case FLAG_MISS_DEP:
					return AssetText.apply(status, AssetText.ERROR);
				case FLAG_DEP_INSTANCE_MISMATCH:
					return AssetText.apply(status, AssetText.WARN);
			}
			return "???";
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			final AssetsNode o = (AssetsNode) obj;
			
			return this.name.equals(o.name) && this.type.equals(o.type) && this.status.equals(o.status) && (this.refCount == o.refCount)
					&& this.info.equals(o.info) && (this.flag == o.flag);
		}
	}
	
	private static class AssetsTextRenderer extends StringCellRenderer {
		public static final StateKey STATE_ERROR = StateKey.get("error");
		public static final StateKey STATE_WARN = StateKey.get("warning");
		public static final StateKey STATE_OKAY = StateKey.get("okay");
		
		public AssetsTextRenderer() {
			setTheme("stringcellrenderer");
		}
		
		public void setCellData(int row, int column, Object data) {
			int flags = 0;
			if (data instanceof AssetText) {
				flags = ((AssetText) data).getFlags();
			}
			setAnimationState(getAnimationState(), flags);
			super.setCellData(row, column, data);
		}
		private void setAnimationState(AnimationState as, int flags) {
			as.setAnimationState(STATE_ERROR, (flags & AssetText.ERROR) != 0);
			as.setAnimationState(STATE_WARN, (flags & AssetText.WARN) != 0);
			as.setAnimationState(STATE_OKAY, (flags & AssetText.OKAY) != 0);
		}
		
	}
	private static class AssetText {
		public static final int ERROR = 1;
		public static final int WARN = 2;
		public static final int OKAY = 4;
		
		final String text;
		final int flags;
		
		public AssetText(String text, int flags) {
			this.text = text;
			this.flags = flags;
		}
		public int getFlags() {
			return flags;
		}
		
		public String toString() {
			return text;
		}
		
		public static Object apply(String text, int flags) {
			return (flags == 0) ? text : new AssetText(text, flags);
		}
	}
	
	private static class RefComparator implements Comparator<AssetsNode> {
		static final RefComparator INSTANCE = new RefComparator();
		
		public int compare(AssetsNode o1, AssetsNode o2) {
			return Integer.signum(o1.refCount - o2.refCount);
		}
	}
	private static class NameComparator implements Comparator<AssetsNode> {
		static final NameComparator INSTANCE = new NameComparator();
		
		public int compare(AssetsNode o1, AssetsNode o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.name, o2.name);
		}
	}
	private static class TypeComparator implements Comparator<AssetsNode> {
		static final TypeComparator INSTANCE = new TypeComparator();
		
		public int compare(AssetsNode o1, AssetsNode o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.type, o2.type);
		}
	}
	private static class StatusComparator implements Comparator<AssetsNode> {
		static final StatusComparator INSTANCE = new StatusComparator();
		
		public int compare(AssetsNode o1, AssetsNode o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.status, o2.status);
		}
	}
	
	public static class TopLevelTreeTableSearchWindow extends InfoWindow implements KeyboardSearchHandler {
		private final TableSelectionModel selectionModel;
		private final EditField searchTextField;
		private final StringBuilder searchTextBuffer;
		
		private String searchText;
		private String searchTextLowercase;
		private Timer timer;
		private TreeTableModel model;
		private int column;
		private boolean searchStartOnly;
		
		public TopLevelTreeTableSearchWindow(TreeTable table, TableSelectionModel selectionModel) {
			super(table);
			this.selectionModel = selectionModel;
			this.searchTextField = new EditField();
			this.searchTextBuffer = new StringBuilder();
			this.searchText = "";
			
			Label label = new Label("Search");
			label.setLabelFor(searchTextField);
			
			searchTextField.setReadOnly(true);
			
			DialogLayout l = new DialogLayout();
			l.setHorizontalGroup(l.createSequentialGroup().addWidget(label).addWidget(searchTextField));
			l.setVerticalGroup(l.createParallelGroup().addWidget(label).addWidget(searchTextField));
			
			add(l);
		}
		
		public TreeTable getTreeTable() {
			return (TreeTable) getOwner();
		}
		
		public TreeTableModel getModel() {
			return model;
		}
		
		public void setModel(TreeTableModel model, int column) {
			if (column < 0) {
				throw new IllegalArgumentException("column");
			}
			if (model != null && column >= model.getNumColumns()) {
				throw new IllegalArgumentException("column");
			}
			this.model = model;
			this.column = column;
			cancelSearch();
			
		}
		
		public boolean isActive() {
			return isOpen();
		}
		
		public void updateInfoWindowPosition() {
			adjustSize();
			setPosition(getOwner().getX(), getOwner().getBottom());
		}
		
		public boolean handleKeyEvent(Event evt) {
			if (model == null) {
				return false;
			}
			
			if (evt.isKeyPressedEvent()) {
				switch (evt.getKeyCode()) {
					case Event.KEY_ESCAPE:
						if (isOpen()) {
							cancelSearch();
							return true;
						}
						break;
					case Event.KEY_RETURN:
						if (isOpen()) {
							cancelSearch();
							return true;
						}
						return false;
					case Event.KEY_BACK:
						if (isOpen()) {
							int length = searchTextBuffer.length();
							if (length > 0) {
								searchTextBuffer.setLength(length - 1);
								updateText();
							}
							restartTimer();
							return true;
						}
						break;
					case Event.KEY_UP:
						if (isOpen()) {
							searchDir(-1);
							restartTimer();
							return true;
						}
						break;
					case Event.KEY_DOWN:
						if (isOpen()) {
							searchDir(+1);
							restartTimer();
							return true;
						}
						break;
					default:
						if (evt.hasKeyCharNoModifiers() && !Character.isISOControl(evt.getKeyChar())) {
							if (searchTextBuffer.length() == 0) {
								searchStartOnly = true;
							}
							searchTextBuffer.append(evt.getKeyChar());
							updateText();
							restartTimer();
							return true;
						}
						break;
				}
			}
			
			return false;
		}
		
		public void cancelSearch() {
			searchTextBuffer.setLength(0);
			// updateText();
			closeInfo();
			if (timer != null) {
				timer.stop();
			}
		}
		
		@Override
		protected void afterAddToGUI(GUI gui) {
			super.afterAddToGUI(gui);
			timer = gui.createTimer();
			timer.setDelay(3000);
			timer.setCallback(new Runnable() {
				public void run() {
					cancelSearch();
				}
			});
		}
		
		@Override
		protected void beforeRemoveFromGUI(GUI gui) {
			timer.stop();
			timer = null;
			
			super.beforeRemoveFromGUI(gui);
		}
		
		private void updateText() {
			searchText = searchTextBuffer.toString();
			searchTextLowercase = null;
			searchTextField.setText(searchText);
			if (searchText.length() >= 0 && model != null) {
				if (!isOpen() && openInfo()) {
					updateInfoWindowPosition();
				}
				updateSearch();
			}
		}
		
		private void restartTimer() {
			timer.stop();
			timer.start();
		}
		
		private void updateSearch() {
			int numRows = model.getNumChildren();
			if (numRows == 0) {
				return;
			}
			for (int row = 0; row < numRows; row++) {
				if (checkRow(row)) {
					setRow(row);
					return;
				}
			}
			searchTextField.setErrorMessage("'" + searchText + "' not found");
		}
		
		private void searchDir(int dir) {
			int numRows = model.getNumChildren();
			if (numRows == 0) {
				return;
			}
			
			int startRow = wrap(getTreeTable().getSelectionManager().getSelectionModel().getFirstSelected(), numRows);
			int row = startRow;
			
			for (;;) {
				do {
					row = wrap(row + dir, numRows);
					if (checkRow(row)) {
						setRow(row);
						return;
					}
				} while (row != startRow);
				
				if (!searchStartOnly) {
					break;
				}
				searchStartOnly = false;
			}
		}
		
		private void setRow(int row) {
			getTreeTable().scrollToRow(row);
			if (selectionModel != null) {
				selectionModel.setSelection(row, row);
			}
			searchTextField.setErrorMessage(null);
		}
		
		private boolean checkRow(int row) {
			Object data = model.getChild(row).getData(column);
			if (data == null) {
				return false;
			}
			String str = data.toString();
			if (searchStartOnly) {
				return str.regionMatches(true, 0, searchText, 0, searchText.length());
			}
			str = str.toLowerCase();
			if (searchTextLowercase == null) {
				searchTextLowercase = searchText.toLowerCase();
			}
			return str.contains(searchTextLowercase);
		}
		
		private static int wrap(int row, int numRows) {
			if (row < 0) {
				return numRows - 1;
			}
			if (row >= numRows) {
				return 0;
			}
			return row;
		}
		
	}
	
}
