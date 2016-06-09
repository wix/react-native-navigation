function getRandomId() {
  return (Math.random()*1e20).toString(36);
}

function stripRefs(obj) {
  var cache = [];
  const result = JSON.stringify(obj, function(key, value) {
      if (typeof value === 'object' && value !== null) {
          if (cache.indexOf(value) !== -1) {
              // Circular reference found, discard key
              return;
          }
          cache.push(value);
      }
      return value;
  });

  return JSON.parse(result);
}

export default {
  getRandomId,
  stripRefs
}
