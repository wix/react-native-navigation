
export const mockDetox = () => {
    it.e2e = (name, fn) => {
        if (process.env.DETOX_START_TIMESTAMP) {
          it(name, fn)
        }
      }
}