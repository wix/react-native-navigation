package com.reactnativenavigation.options.parsers;

import androidx.annotation.NonNull;

import com.reactnativenavigation.options.LayoutNode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LayoutNodeParser {

	@SuppressWarnings("unchecked")
	public static LayoutNode parse(JSONObject layoutTree) {
		String id = layoutTree.optString("id");
		LayoutNode.Type type = LayoutNode.Type.valueOf(layoutTree.optString("type"));
		JSONObject data = parseData(layoutTree);
		List<LayoutNode> children = parseChildren(layoutTree);
		return new LayoutNode(id, type, data, children);
	}

	@NonNull
	private static List<LayoutNode> parseChildren(JSONObject layoutTree) {
		List<LayoutNode> children = new ArrayList<>();
		if (layoutTree.has("children")) {
			JSONArray rawChildren = layoutTree.optJSONArray("children");
			for (int i = 0; i < rawChildren.length(); i++) {
				children.add(parse(rawChildren.optJSONObject(i)));
			}
		}
		return children;
	}

	private static JSONObject parseData(JSONObject layoutTree) {
		return layoutTree.has("data") ? layoutTree.optJSONObject("data") : new JSONObject();
	}
}
