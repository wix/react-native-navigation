package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;

import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LayoutNodeParserTest extends BaseTest {
	@Test
	public void dto() throws Exception {
		LayoutNode node = new LayoutNode("the id", LayoutNode.Type.Container);
		assertThat(node.id).isEqualTo("the id");
		assertThat(node.type).isEqualTo(LayoutNode.Type.Container);
		assertThat(node.data.keys()).isEmpty();
		assertThat(node.children).isEmpty();
	}

	@Test
	public void parseType() throws Exception {
		assertThat(LayoutNode.Type.valueOf("Container")).isEqualTo(LayoutNode.Type.Container);
	}

	@Test(expected = RuntimeException.class)
	public void invalidType() throws Exception {
		LayoutNode.Type.valueOf("some type");
	}

	@Test
	public void parseFromTree() throws Exception {
		JSONObject tree = new JSONObject("{id: node1, " +
				"type: ContainerStack, " +
				"data: {dataKey: dataValue}, " +
				"children: [{id: childId1, type: Container}]}");

		LayoutNode result = LayoutNodeParser.parse(tree);

		assertThat(result).isNotNull();
		assertThat(result.id).isEqualTo("node1");
		assertThat(result.type).isEqualTo(LayoutNode.Type.ContainerStack);
		assertThat(result.data.length()).isEqualTo(1);
		assertThat(result.data.getString("dataKey")).isEqualTo("dataValue");
		assertThat(result.children).hasSize(1);
		assertThat(result.children.get(0).id).isEqualTo("childId1");
		assertThat(result.children.get(0).type).isEqualTo(LayoutNode.Type.Container);
		assertThat(result.children.get(0).data.keys()).isEmpty();
		assertThat(result.children.get(0).children).isEmpty();
	}
}
