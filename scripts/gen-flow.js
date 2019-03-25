const { compiler } = require('flowgen');
const fs = require('fs');
const glob = require('glob');
const path = require('path');
const prettier = require('prettier');

const ROOT = path.normalize(`${__dirname}/../lib`);

run();

function convertTypeImports(file, typePattern) {
  let flowDef = fs.readFileSync(file, 'utf8');

  const importRegex = /import\s+{([a-zA-Z,\s]+)}\s+from\s+[-'"a-zA-Z]+;/g;

  // Gather non-local imports.
  const externalTypes = new Set([
    'ComponentProvider',
    'EventEmitter',
    'GestureResponderEvent',
    'ImageRequireSource',
    'Insets',
    'NativeSyntheticEvent',
    'NativeTouchEvent'
  ]);
  const importTypes = [];
  const importClasses = [];
  let m;
  while ((m = importRegex.exec(flowDef)) !== null) {
    const imports = m[1].split(',');
    for (let item of imports) {
      item = item.trim();
      if (externalTypes.has(item)) {
        importTypes.push(item);
      } else {
        importClasses.push(item);
      }
    }
  }

  // Strip non-local imports.
  flowDef = flowDef.replace(importRegex, '');

  // Add imports
  let importPath = path.relative(`${file}/..`, `${ROOT}/dist/types`);
  if (!importPath.startsWith('.')) {
    importPath = `./${importPath}`;
  }

  if (importTypes.length > 0) {
    flowDef = `
      import type {${importTypes.join(',')}} from '${importPath}';
      ${flowDef}
    `;
  }

  if (importClasses.length > 0) {
    flowDef = `
      import {${importClasses.join(',')}} from '${importPath}';
      ${flowDef}
    `;
  }

  // Mark local type imports.
  flowDef = flowDef.replace(typePattern, 'import type {$1$2$3');

  // Tidy output.
  flowDef = `
    // @flow
    ${flowDef}
  `;
  flowDef = prettier.format(flowDef, { singleQuote: true, parser: 'flow' });

  fs.writeFileSync(file, flowDef, 'utf8');
}

function processFile(file) {
  console.log(`(gen-flow) ${file.substring(ROOT.length + 1)}`);

  let flowDef = compiler.compileDefinitionFile(file);

  // Fix problematic formatting.
  flowDef = flowDef.replace(/\} (declare|export)/g, '}\n$1');

  // Flow has some trouble with default type parameters.
  flowDef = flowDef.replace(/<\s*([a-zA-Z]+)\s*=[^>]{1,10}>/g, '<$1>');
  flowDef = flowDef.replace(/<>/g, '<any>');

  // Expand wildcard type parameters.
  flowDef = flowDef.replace(
    /React\$Component<any>/g,
    'React$$Component<any, any>'
  );

  // Convert intefaces to types.
  flowDef = flowDef.replace(/interface\s+([a-zA-Z<>]+)/g, 'type $1 =');

  // Convert mixins to inheritance.
  flowDef = flowDef.replace(/mixins/g, 'extends');

  // Translate Record types.
  flowDef = flowDef.replace(
    /\sRecord<\s*([a-zA-Z]+)\s*,\s*([a-zA-Z]+)\s*>/g,
    '{[$1]:$2}'
  );

  // Strip PropTypes.
  flowDef = flowDef.replace(/static\s+propTypes\s*:\s*{[^}]+};?/g, '');

  const flowFile = `${file.slice(0, -5)}.js.flow`;
  fs.writeFileSync(flowFile, flowDef, 'utf8');

  const types = [];
  let m;
  const regex = /type ([a-zA-Z]+)[ <]/g;
  while ((m = regex.exec(flowDef)) !== null) {
    types.push(m[1]);
  }
  return types;
}

function run() {
  const typescriptFiles = glob.sync(`${ROOT}/dist/**/*.d.ts`);
  let knownTypes = [];
  for (const file of typescriptFiles) {
    if (file.endsWith('types.d.ts') || file.endsWith('.test.d.ts')) {
      // Skip.
      continue;
    }

    const types = processFile(file);
    knownTypes = knownTypes.concat(types);
  }

  const flowFiles = glob.sync(`${ROOT}/dist/**/*.js.flow`);
  const typePattern = new RegExp(
    `import {(\\s*)(${knownTypes.join('|')})(\\s|,|})`,
    'g'
  );
  for (const file of flowFiles) {
    if (file.endsWith('types.js.flow')) {
      // Skip.
      continue;
    }

    convertTypeImports(file, typePattern);
  }

  fs.copyFileSync(`${ROOT}/src/types.js.flow`, `${ROOT}/dist/types.js.flow`);
}
