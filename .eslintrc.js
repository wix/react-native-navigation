module.exports = {
  root: true,
  extends: ['@react-native', 'prettier', 'prettier/@typescript-eslint', 'prettier/react'],
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint'],
  env: {
    jest: true,
  },
};

