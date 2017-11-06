package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;

import org.json.JSONObject;
import org.junit.Test;

import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.True;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class OverlayOptionsTest extends BaseTest {

	@Test
	public void parsesNullAsDefaultEmptyOptions() throws Exception {
		assertThat(OverlayOptions.parse(null)).isNotNull();
	}

	@Test
	public void parsesJson() throws Exception {
		JSONObject json = new JSONObject();
		json.put("title", "the title");
		json.put("text", "the text");

		OverlayOptions result = OverlayOptions.parse(json);
		assertThat(result.getTitle()).isEqualTo("the title");
		assertThat(result.getText()).isEqualTo("the text");
	}

	@Test
	public void defaultEmptyOptions() throws Exception {
		OverlayOptions uut = new OverlayOptions();
		assertThat(uut.getTitle()).isEmpty();
	}
}
