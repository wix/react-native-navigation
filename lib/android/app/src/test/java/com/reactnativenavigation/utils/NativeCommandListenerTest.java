package com.reactnativenavigation.utils;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.react.EventEmitter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NativeCommandListenerTest extends BaseTest {
    private static final String COMMAND_ID = "someCommand";
    private static final String CHILD_ID = "someChild";
    private static final long NOW = 1535374334;

    private EventEmitter eventEmitter;
    private Promise promise;

    private NativeCommandListener uut;

    @Override
    public void beforeEach() {
        promise = Mockito.mock(Promise.class);
        eventEmitter = Mockito.mock(EventEmitter.class);
        uut = new NativeCommandListener(COMMAND_ID, promise, eventEmitter, mockNow());
    }

    @Test
    public void onSuccess() {
        uut.onSuccess(CHILD_ID);
        verify(promise, times(1)).resolve(CHILD_ID);
    }

    @Test
    public void onSuccess_emitsNavigationEvent() {
        uut.onSuccess(CHILD_ID);
        verify(eventEmitter, times(1)).emitCommandCompleted(COMMAND_ID, NOW);
    }

    @Test
    public void onError() {
        uut.onError("something which is wrong");
        ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
        verify(promise, times(1)).reject(captor.capture());
        assertThat(captor.getValue().getMessage().equals("something which is wrong"));
    }

    private Now mockNow() {
        Now now = Mockito.mock(Now.class);
        when(now.now()).then(i -> NOW);
        return now;
    }
}
