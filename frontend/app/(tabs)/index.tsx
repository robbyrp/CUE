/**
 * Home screen — route "/".
 *
 * A file named `index.tsx` inside `app/` maps to the root URL "/". This is the
 * first screen users see. Right now it is a placeholder whose only job is to
 * prove that file-based routing works; once we add the tab navigator it becomes
 * the "Discover" tab.
 *
 * Styling note: this screen now uses NativeWind `className`s instead of inline
 * styles. The mapping from the old inline style is 1:1 and worth memorising:
 *   { flex: 1 }                  -> "flex-1"
 *   { alignItems: 'center' }     -> "items-center"   (cross axis)
 *   { justifyContent: 'center' } -> "justify-center" (main axis)
 * Unlike web, React Native's default flex-direction is `column`, so this centers
 * vertically (justify) and horizontally (items) — the same classes behave
 * differently than they would on a web `div`.
 *
 * @see https://docs.expo.dev/router/create-pages/   (creating pages/routes)
 * @see https://www.nativewind.dev/docs/core-concepts/usage  (className usage)
 */
import { Text, View, StyleSheet, TextInput, ActivityIndicator } from 'react-native';
import { Button, Host } from '@expo/ui/jetpack-compose';
import { Link, useRouter } from 'expo-router';
import { Image } from 'expo-image';

export default function HomeScreen() {
  const router = useRouter();
  return (
    <View className="flex-1 items-center justify-center bg-white">
      <Text className="text-xl font-semibold text-red-500">
        CUE — NativeWind is working 🎭
      </Text>
      <TextInput placeholder="Email" />
      <ActivityIndicator size={"large"}/>
      <Host matchContents>
        <Button onClick={() => router.push("/about")}>
          <Text>Navigate</Text>
        </Button>
      </Host>
    </View>
  );
}

const styles = StyleSheet.create({
  image: {
    width: 100,
    height: 100,
  },
  text: {
    color: "red",
  }
});
