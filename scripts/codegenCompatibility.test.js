const fs = require('fs');
const path = require('path');
const { TypeScriptParser } = require('@react-native/codegen/lib/parsers/typescript/parser');

it('preserves the native codegen schema without private React Native type imports', () => {
  const filename = path.join(__dirname, '../src/adapters/NativeRNNTurboModule.ts');
  const source = fs.readFileSync(filename, 'utf8');
  const legacySource = source.replace('./CodegenTypes', 'react-native/Libraries/Types/CodegenTypes');
  const parser = new TypeScriptParser();

  expect(parser.parseString(source, filename)).toEqual(parser.parseString(legacySource, filename));
});
