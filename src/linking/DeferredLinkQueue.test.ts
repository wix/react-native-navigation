import { DeferredLinkQueue } from './DeferredLinkQueue';

describe('DeferredLinkQueue', () => {
  let uut: DeferredLinkQueue;
  let flushed: string[];

  beforeEach(() => {
    uut = new DeferredLinkQueue();
    flushed = [];
    uut.setFlushCallback((url) => flushed.push(url));
  });

  it('starts not ready', () => {
    expect(uut.isReady()).toBe(false);
  });

  it('enqueues URLs while not ready', () => {
    uut.enqueue('myapp://a');
    uut.enqueue('myapp://b');
    expect(uut.pending()).toEqual(['myapp://a', 'myapp://b']);
    expect(flushed).toEqual([]);
  });

  it('flushes queued URLs in order when becoming ready', () => {
    uut.enqueue('myapp://a');
    uut.enqueue('myapp://b');
    uut.setReady(true);
    expect(flushed).toEqual(['myapp://a', 'myapp://b']);
    expect(uut.pending()).toEqual([]);
  });

  it('does not re-flush when already ready', () => {
    uut.setReady(true);
    uut.enqueue('myapp://a');
    uut.setReady(true);
    expect(flushed).toEqual([]);
    expect(uut.pending()).toEqual(['myapp://a']);
  });

  it('flushes again after toggling not-ready → ready', () => {
    uut.setReady(true);
    uut.setReady(false);
    uut.enqueue('myapp://x');
    uut.setReady(true);
    expect(flushed).toEqual(['myapp://x']);
  });

  it('clear() drops pending URLs', () => {
    uut.enqueue('myapp://a');
    uut.clear();
    uut.setReady(true);
    expect(flushed).toEqual([]);
  });

  it('flush() is a no-op when no callback is registered', () => {
    const fresh = new DeferredLinkQueue();
    fresh.enqueue('myapp://a');
    expect(() => fresh.flush()).not.toThrow();
  });
});
