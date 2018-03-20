package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.params.Orientation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class OrientationOptionsTest extends BaseTest {

    @Override
    public void beforeEach() {

    }

    @Test
    public void parse() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("default"));
        assertThat(options.orientations).hasSize(1);
    }

    @Test
    public void parseOrientations() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("default", "landscape", "portrait"));
        assertThat(options.orientations.get(0)).isEqualTo(Orientation.Default);
        assertThat(options.orientations.get(1)).isEqualTo(Orientation.Landscape);
        assertThat(options.orientations.get(2)).isEqualTo(Orientation.Portrait);
    }

    @Test
    public void parseSingleOrientation() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("landscape"));
        assertThat(options.orientations.get(0)).isEqualTo(Orientation.Landscape);
    }

    @Test
    public void landscapePortrait_regardedAsUserOrientation() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("landscape", "portrait"));
        assertThat(options.getValue()).isEqualTo(Orientation.PortraitLandscape.orientationCode);
    }

    @Test
    public void portraitLandscape_regardedAsUserOrientation() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("portrait", "landscape"));
        assertThat(options.getValue()).isEqualTo(Orientation.PortraitLandscape.orientationCode);
    }

    @Test
    public void unsupportedOrientationsAreIgnored() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("default", "autoRotate"));
        assertThat(options.orientations).hasSize(1);
        assertThat(options.orientations.get(0)).isEqualTo(Orientation.Default);
    }

    @Test
    public void getValue_returnsDefaultIfUndefined() throws Exception {
        OrientationOptions options = new OrientationOptions();
        assertThat(options.getValue()).isEqualTo(Orientation.Default.orientationCode);
    }

    private JSONObject create(String... orientations) {
        JSONObject orientation = new JSONObject();
        try {
            orientation.putOpt("orientation", orientations.length > 1 ? new JSONArray(Arrays.asList(orientations)) : orientations[0]);
        } catch (JSONException e) {
            throw new RuntimeException("Unable to create orientation object");
        }
        return orientation;
    }
}
