/**
 * Babel configuration.
 *
 * Babel transforms our JSX/TS before Metro bundles it. NativeWind needs two
 * things here:
 *
 *   1. `babel-preset-expo` with `jsxImportSource: 'nativewind'`
 *      -> rewires JSX so every element can accept a `className` prop and have it
 *         turned into native styles. Without this, `className` is ignored.
 *
 *   2. `'nativewind/babel'`
 *      -> NativeWind's own transform that wires the compiled styles in.
 *
 * NOTE on Reanimated v4: its required Babel plugin (`react-native-worklets/
 * plugin`) is added AUTOMATICALLY by `babel-preset-expo` when it detects
 * reanimated/worklets installed. So we deliberately do NOT list it here —
 * adding it twice causes a "plugin already added" build error.
 *
 * This file is new, so Metro's transform cache must be cleared once after
 * adding it: `npx expo start -c`.
 *
 * @see https://www.nativewind.dev/docs/getting-started/installation
 * @see https://docs.expo.dev/versions/v56.0.0/config/babel/
 */
module.exports = function (api) {
  api.cache(true);
  return {
    presets: [
      ['babel-preset-expo', { jsxImportSource: 'nativewind' }],
      'nativewind/babel',
    ],
  };
};
