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
        TopTabs,
        TopTab
	}

	public final String id;
	public final Type type;
	public final JSONObject data;

	final List<LayoutNode> children;

	LayoutNode(String id, Type type) {
		this(id, type, new JSONObject(), new ArrayList<>());
	}

	LayoutNode(String id, Type type, JSONObject data, List<LayoutNode> children) {
		this.id = id;
		this.type = type;
		this.data = data;
		this.children = children;
	}
}
