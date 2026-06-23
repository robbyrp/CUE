
import { Text, View, StyleSheet } from 'react-native';
import { BottomSheet, Button, Host, VStack } from '@expo/ui/jetpack-compose';
import { useState } from 'react';

export default function Profile() {

  const [isPresented, setPresented] = useState(false);

  return (
    <View className="flex-1 items-center justify-center bg-white">
      <Text>Profile Screen</Text>
      <Host>
        <VStack>
          <Button></Button>
        </VStack>
      </Host>
    </View>
  );
}

