package com.reactnativenavigation.parse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LayoutNode {
	public enum Type {
		Container,
		ContainerStack,
		BottomTabs,
		SideMenuRoot,
		SideMenuCenter,
		SideMenuLeft,
		SideMenuRight,
		CustomDialog,
        TopTabsContainer,
        TopTab
	}

	public final String id;
	public final Type type;
	public final JSONObject data;

	public final List<LayoutNode> children;

	public LayoutNode(String id, Type type) {
		this(id, type, new JSONObject(), new ArrayList<LayoutNode>());
	}

	public LayoutNode(String id, Type type, JSONObject data, List<LayoutNode> children) {
		this.id = id;
		this.type = type;
		this.data = data;
		this.children = children;
	}
}
