/**
 * Tailwind / NativeWind configuration — the SOURCE OF TRUTH for design tokens.
 *
 * NativeWind reuses the Tailwind config you already know from the web, but the
 * output is native styles (not CSS). Two things make this file "NativeWind" and
 * not plain web Tailwind:
 *
 *   1. `content`  — the files Tailwind scans for `className` strings so it knows
 *                   which utilities to generate. We point it at our routes
 *                   (`app/`) and our code (`src/`). If a class never appears in
 *                   these globs, it won't exist at runtime.
 *   2. `presets`  — `nativewind/preset` swaps Tailwind's web defaults for ones
 *                   that make sense on React Native (e.g. no `rem`, RN-safe
 *                   colors and spacing).
 *
 * Per CLAUDE.md §6, design tokens (colors, spacing, fonts) belong in
 * `theme.extend` here — components must read these, never hardcode hex/spacing.
 *
 * @see https://www.nativewind.dev/docs/getting-started/installation
 * @see https://tailwindcss.com/docs/configuration
 * @type {import('tailwindcss').Config}
 */
module.exports = {
  // Scan routes and feature/source code. Add new top-level code folders here.
  content: [
    './app/**/*.{js,jsx,ts,tsx}',
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  presets: [require('nativewind/preset')],
  theme: {
    // Design tokens go under `extend` so we keep Tailwind's defaults AND add
    // CUE's own. We'll populate this when we write DESIGN.md (see CLAUDE.md §10).
    extend: {},
  },
  plugins: [],
};
