package com.reactnativenavigation.presentation;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RenderCheckerTest extends BaseTest {
    private RenderChecker uut;

    @Override
    public void beforeEach() {
        uut = new RenderChecker();
    }

    @Test
    public void areRendered() {
        Collection<ViewController> items = Arrays.asList(
                renderedComponent(),
                renderedComponent(),
                renderedComponent()
        );
        assertThat(uut.areRendered(items)).isTrue();
        forEach(items, i -> verify(i).isRendered());
    }

    @Test
    public void areRendered_reduce() {
        Collection<ViewController> items = Arrays.asList(
                renderedComponent(),
                notRenderedComponent(),
                renderedComponent()
        );
        assertThat(uut.areRendered(items)).isFalse();

    }

    private ViewController renderedComponent() {
        ViewController mock = Mockito.mock(ViewController.class);
        when(mock.isRendered()).then(__ -> true);
        return mock;
    }

    private ViewController notRenderedComponent() {
        ViewController mock = Mockito.mock(ViewController.class);
        when(mock.isRendered()).then(__ -> false);
        return mock;
    }
}
