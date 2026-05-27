/**
 * Holds URLs while the linking pipeline is not ready and replays them in
 * order once it becomes ready.
 *
 * Readiness is set by the owning `LinkingHandler` and reflects the combined
 * state of:
 *   - whether the first `setRoot` has resolved (so a modal can be presented),
 *   - whether the user-supplied `isReady` predicate (if any) returns `true`.
 */
export class DeferredLinkQueue {
  private queue: string[] = [];
  private ready = false;
  private flushCallback: ((url: string) => void) | null = null;

  public setFlushCallback(callback: (url: string) => void): void {
    this.flushCallback = callback;
  }

  public setReady(ready: boolean): void {
    const becameReady = !this.ready && ready;
    this.ready = ready;
    if (becameReady) {
      this.flush();
    }
  }

  public isReady(): boolean {
    return this.ready;
  }

  /**
   * Enqueue a URL for later processing. Should only be called when the
   * queue is not ready; the handler decides when to enqueue vs process.
   */
  public enqueue(url: string): void {
    this.queue.push(url);
  }

  public flush(): void {
    if (!this.flushCallback) return;
    const pending = [...this.queue];
    this.queue = [];
    for (const url of pending) {
      this.flushCallback(url);
    }
  }

  public clear(): void {
    this.queue = [];
  }

  public pending(): string[] {
    return [...this.queue];
  }
}
