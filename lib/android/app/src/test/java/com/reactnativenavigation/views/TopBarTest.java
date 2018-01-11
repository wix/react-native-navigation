package com.reactnativenavigation.views;

import com.reactnativenavigation.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

public class TopBarTest extends BaseTest {
    @Test
    public void title() throws Exception {
        TopBar topBar = new TopBar(newActivity(), null, buttonId -> {}, null);
        assertThat(topBar.getTitle()).isEmpty();

        topBar.setTitle("new title");
        assertThat(topBar.getTitle()).isEqualTo("new title");
    }
}
