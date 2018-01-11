package com.reactnativenavigation.viewcontrollers;

import com.reactnativenavigation.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

public class IdStackTest extends BaseTest {

    private IdStack<Integer> uut;

    @Override
    public void beforeEach() {
        super.beforeEach();
        uut = new IdStack<>();
    }

    @Test
    public void isEmpty() throws Exception {
        assertThat(uut.isEmpty()).isTrue();
        uut.push("123", 123);
        assertThat(uut.isEmpty()).isFalse();
    }

    @Test
    public void size() throws Exception {
        assertThat(uut.size()).isEqualTo(0);
        uut.push("123", 123);
        assertThat(uut.size()).isEqualTo(1);
    }

    @Test
    public void peek() throws Exception {
        assertThat(uut.peek()).isNull();
        uut.push("123", 123);
        uut.push("456", 456);
        assertThat(uut.peek()).isEqualTo(456);
    }

    @Test
    public void pop() throws Exception {
        assertThat(uut.pop()).isNull();
        uut.push("123", 123);
        uut.push("456", 456);
        assertThat(uut.pop()).isEqualTo(456);
    }

    @Test
    public void peekId() throws Exception {
        assertThat(uut.peekId()).isNull();
        uut.push("123", 123);
        assertThat(uut.peekId()).isEqualTo("123");
    }

    @Test
    public void clear() throws Exception {
        uut.push("123", 123);
        uut.push("456", 456);
        uut.clear();
        assertThat(uut.isEmpty()).isTrue();
    }

    @Test
    public void getById() throws Exception {
        assertThat(uut.get("123")).isNull();
        uut.push("123", 123);
        uut.push("456", 456);
        assertThat(uut.get("123")).isEqualTo(123);
    }

    @Test
    public void containsId() throws Exception {
        assertThat(uut.containsId("123")).isFalse();
        uut.push("123", 123);
        assertThat(uut.containsId("123")).isTrue();
    }

    @Test
    public void remove() throws Exception {
        assertThat(uut.remove("123")).isNull();

        uut.push("123", 123);
        uut.push("456", 456);

        assertThat(uut.remove("123")).isEqualTo(123);
    }

    @Test
    public void iterableIds() throws Exception {
        assertThat(uut).isInstanceOf(Iterable.class);
        assertThat(uut).isEmpty();
        uut.push("123", 123);
        uut.push("456", 456);
        assertThat(uut).containsExactly("456", "123");
    }

    @Test
    public void isTop() throws Exception {
        assertThat(uut.isTop("123")).isFalse();
        uut.push("123", 123);
        assertThat(uut.isTop("123")).isTrue();
        uut.push("456", 456);
        assertThat(uut.isTop("123")).isFalse();
    }

    @Test
    public void values() throws Exception {
        assertThat(uut.values()).isNotNull().isEmpty();
        uut.push("123", 123);
        uut.push("456", 456);
        assertThat(uut.values()).isNotNull().containsExactlyInAnyOrder(123, 456);
    }
}
