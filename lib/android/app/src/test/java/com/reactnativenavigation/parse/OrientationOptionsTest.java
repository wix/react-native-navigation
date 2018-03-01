package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.params.Orientation;

import org.json.JSONArray;
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
        assertThat(options.orientations[0]).isEqualTo(Orientation.Default);
        assertThat(options.orientations[1]).isEqualTo(Orientation.Landscape);
        assertThat(options.orientations[2]).isEqualTo(Orientation.Portrait);
    }

    @Test
    public void unsupportedOrientationsAreIgnored() throws Exception {
        OrientationOptions options = OrientationOptions.parse(create("default", "autoRotate"));
        assertThat(options.orientations).hasSize(1);
        assertThat(options.orientations[0]).isEqualTo(Orientation.Default);
    }

    @Test
    public void getValue_returnsDefaultIfUndefined() throws Exception {
        OrientationOptions options = new OrientationOptions();
        assertThat(options.getValue()).isEqualTo(Orientation.Default.orientationCode);
    }

    private JSONArray create(String... orientations) {
        return new JSONArray(Arrays.asList(orientations));
    }
}
