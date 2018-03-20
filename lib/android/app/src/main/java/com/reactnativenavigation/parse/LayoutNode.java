package com.reactnativenavigation.parse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LayoutNode {
	public enum Type {
		Component,
        ExternalComponent,
		Stack,
		BottomTabs,
		SideMenuRoot,
		SideMenuCenter,
		SideMenuLeft,
		SideMenuRight,
        TopTabs
	}

	public final String id;
	public final Type type;
	public final JSONObject data;

	final List<LayoutNode> children;

	LayoutNode(String id, Type type) {
		this(id, type, new JSONObject(), new ArrayList<>());
	}

	public LayoutNode(String id, Type type, JSONObject data, List<LayoutNode> children) {
		this.id = id;
		this.type = type;
		this.data = data;
		this.children = children;
	}

    JSONObject getNavigationOptions() {
	    return data.optJSONObject("options");
    }
}
