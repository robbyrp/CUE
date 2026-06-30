/**
 * Metro bundler configuration.
 *
 * Metro is React Native's bundler (the equivalent of Webpack/Vite on web). By
 * default Expo configures it for us, so most projects don't need this file.
 * NativeWind needs it though: `withNativeWind` teaches Metro how to read our
 * `global.css` (the Tailwind entry point) and compile those utilities into
 * native styles during bundling.
 *
 * `input` MUST match the CSS file we import at the app root — keep this path and
 * the import in app/_layout.tsx in sync.
 *
 * After creating or changing this file, restart Metro with a cleared cache:
 * `npx expo start -c`.
 *
 * @see https://www.nativewind.dev/docs/getting-started/installation
 * @see https://docs.expo.dev/guides/customizing-metro/
 */
const { getDefaultConfig } = require('expo/metro-config');
const { withNativeWind } = require('nativewind/metro');

const config = getDefaultConfig(__dirname);

module.exports = withNativeWind(config, { input: './global.css' });
