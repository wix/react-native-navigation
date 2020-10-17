// @ts-check

/**
 * @param {string} to
 * @param {number} fromIndex
 * @param {string} what
 */
function insertString(to, fromIndex, what) {
  return to.substring(0, fromIndex) + what + to.substring(fromIndex, to.length);
}

module.exports = {
  insertString,
};
