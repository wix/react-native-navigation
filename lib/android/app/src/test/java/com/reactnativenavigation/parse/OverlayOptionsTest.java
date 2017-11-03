package com.reactnativenavigation.parse;

import android.graphics.Color;

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
		JSONObject nestedButton = new JSONObject();
		nestedButton.put("text", "OK");
		nestedButton.put("action", "action");
		json.put("positiveButton", nestedButton);
		json.put("textColor", Color.RED);
		json.put("duration", "short");
		json.put("backgroundColor", Color.RED);

		OverlayOptions result = OverlayOptions.parse(json);
		assertThat(result.getTitle()).isEqualTo("the title");
		assertThat(result.getText()).isEqualTo("the text");
		assertThat(result.getPositiveButton().getText()).isEqualTo("OK");
		assertThat(result.getPositiveButton().getAction()).isEqualTo("action");
		assertThat(result.getTextColor()).isEqualTo(Color.RED);
		assertThat(result.getDuration()).isEqualTo("short");
		assertThat(result.getBackgroundColor()).isEqualTo(Color.RED);
	}

	@Test
	public void defaultEmptyOptions() throws Exception {
		OverlayOptions uut = new OverlayOptions();
		assertThat(uut.getTitle()).isEmpty();
	}
}
