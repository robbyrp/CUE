/**
 * NativeWind TypeScript ambient types.
 *
 * This single triple-slash directive pulls in NativeWind's type augmentation,
 * which adds the `className` prop to React Native components (View, Text, etc.).
 * Without it, TypeScript would error: "Property 'className' does not exist on
 * type ...". Our tsconfig.json already globs every ".ts" file, so this file is
 * picked up automatically — no extra config needed.
 */
/// <reference types="nativewind/types" />
