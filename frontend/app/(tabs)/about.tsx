
import { Text, View, StyleSheet, TextInput } from 'react-native';
import { Image } from 'expo-image';

export default function About() {
  return (
    <View className="flex-1 items-center justify-center bg-white">
      <Text className="text-xl font-semibold text-red-500">
        CUE — NativeWind is working 🎭
      </Text>
      <Image style={styles.image} source={{uri: 'https://reactnative.dev/img/tiny_logo.png'}} />
      <TextInput placeholder="Email" />
    </View>
  );
}

const styles = StyleSheet.create({
  image: {
    width: 100,
    height: 100,
  }
});
