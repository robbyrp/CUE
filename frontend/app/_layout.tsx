/**
 * Root layout for the CUE app.
 *
 * In Expo Router, any folder under `app/` may contain a `_layout.tsx` that wraps
 * the screens inside it. This is the *root* layout, so it wraps the ENTIRE app —
 * which makes it the place where app-wide providers will live later (TanStack
 * Query, theme, auth/session). We'll add those in a dedicated "providers" step.
 *
 * For now it renders a single <Stack> navigator. A Stack shows one screen at a
 * time and lets you push new screens on top and pop back (think: a pile of cards).
 * It is the most common navigation container in mobile apps.
 *
 * @see https://docs.expo.dev/router/basics/layout/  (layout routes)
 * @see https://docs.expo.dev/router/advanced/stack/  (the Stack navigator)
 */

// Importing the Tailwind entry point ONCE here, at the app root, makes every
// `className` in the app work. NativeWind/Metro compiles this into native
// styles (see metro.config.js). This is a side-effect import — there is no
// value to assign, we just need it loaded before any screen renders.
import '../global.css';

import { Stack } from 'expo-router';

export default function RootLayout() {
  // `screenOptions` apply to every screen in this Stack. We keep the default
  // header for now; individual features will customise their own headers later.
  return (
    <Stack
      screenOptions={{ headerShown: false }}>
      <Stack.Screen name="(tabs)" />
    </Stack>
  )
}
