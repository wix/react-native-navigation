package com.reactnativenavigation.options;

import com.reactnativenavigation.*;
import com.reactnativenavigation.options.parsers.LayoutNodeParser;

import org.json.*;
import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

public class LayoutNodeParserTest extends BaseTest {
    @Test
    public void dto() throws Exception {
        LayoutNode node = new LayoutNode("the id", LayoutNode.Type.Component);
        assertThat(node.id).isEqualTo("the id");
        assertThat(node.type).isEqualTo(LayoutNode.Type.Component);
        assertThat(node.data.keys()).isEmpty();
        assertThat(node.children).isEmpty();
    }

    @Test
    public void parseType() throws Exception {
        assertThat(LayoutNode.Type.valueOf("Component")).isEqualTo(LayoutNode.Type.Component);
    }

    @Test(expected = RuntimeException.class)
    public void invalidType() throws Exception {
        LayoutNode.Type.valueOf("some type");
    }

    @Test
    public void parseFromTree() throws Exception {
        JSONObject tree = new JSONObject("{id: node1, " +
                "type: Stack, " +
                "data: {dataKey: dataValue}, " +
                "children: [{id: childId1, type: Component}]}");

        LayoutNode result = LayoutNodeParser.parse(tree);

        assertThat(result).isNotNull();
        assertThat(result.id).isEqualTo("node1");
        assertThat(result.type).isEqualTo(LayoutNode.Type.Stack);
        assertThat(result.data.length()).isEqualTo(1);
        assertThat(result.data.getString("dataKey")).isEqualTo("dataValue");
        assertThat(result.children).hasSize(1);
        assertThat(result.children.get(0).id).isEqualTo("childId1");
        assertThat(result.children.get(0).type).isEqualTo(LayoutNode.Type.Component);
        assertThat(result.children.get(0).data.keys()).isEmpty();
        assertThat(result.children.get(0).children).isEmpty();
    }
}
