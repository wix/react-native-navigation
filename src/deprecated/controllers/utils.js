/*eslint-disable*/
var INFINITY = 1 / 0;
var MAX_SAFE_INTEGER = 9007199254740991;

function flattenDeep(array) {
  var length = array ? array.length : 0;
  return length ? baseFlatten(array, INFINITY) : [];
}

function baseFlatten(array, depth, isStrict, result) {
  result || (result = []);
  var index = -1, length = array.length;
  while (++index < length) {
    var value = array[index];
    if (depth > 0 && isArrayLikeObject(value) &&
        (isStrict || isArray(value) || isArguments(value))) {
      if (depth > 1) {
        // Recursively flatten arrays (susceptible to call stack limits).
        baseFlatten(value, depth - 1, isStrict, result);
      } else {
        arrayPush(result, value);
      }
    } else if (!isStrict) {
      result[result.length] = value;
    }
  }
  return result;
}

function isArguments(value) {
  // Safari 8.1 incorrectly makes `arguments.callee` enumerable in strict mode.
  return isArrayLikeObject(value) && hasOwnProperty.call(value, 'callee') &&
    (!propertyIsEnumerable.call(value, 'callee') || objectToString.call(value) == argsTag);
}

var isArray = Array.isArray;

function isArrayLikeObject(value) {
  return isObjectLike(value) && isArrayLike(value);
}

function isObjectLike(value) {
  return !!value && typeof value == 'object';
}

function isArrayLike(value) {
  return value != null &&
    !(typeof value == 'function' && isFunction(value)) && isLength(getLength(value));
}

function isFunction(value) {
  // The use of `Object#toString` avoids issues with the `typeof` operator
  // in Safari 8 which returns 'object' for typed array constructors, and
  // PhantomJS 1.9 which returns 'function' for `NodeList` instances.
  var tag = isObject(value) ? objectToString.call(value) : '';
  return tag == funcTag || tag == genTag;
}

function isLength(value) {
  return typeof value == 'number' &&
    value > -1 && value % 1 == 0 && value <= MAX_SAFE_INTEGER;
}

var getLength = baseProperty('length');

function baseProperty(key) {
  return function(object) {
    return object == null ? undefined : object[key];
  };
}

function arrayPush(array, values) {
  var index = -1, length = values.length, offset = array.length;
  while (++index < length) {
    array[offset + index] = values[index];
  }
  return array;
}

module.exports = {
  flattenDeep: flattenDeep
};
