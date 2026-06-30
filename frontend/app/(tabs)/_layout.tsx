import '../../global.css';

import { Tabs } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';

export default function RootLayout() {
  // `screenOptions` apply to every screen in this Stack. We keep the default
  // header for now; individual features will customise their own headers later.
  return (
    <Tabs screenOptions={{ tabBarActiveTintColor: "crimson" }}>
        <Tabs.Screen
            name="index" 
            options={{
                title: "Home" ,
                tabBarIcon: ({color, size, focused}) => (
                    <Ionicons name={focused ? "home" : "home-outline"} color={color} size={size} />
                ),
            }}
        />
        <Tabs.Screen 
            name="about" 
            options={{
                title: "About" ,
                tabBarIcon: ({color, size, focused}) => (
                    <Ionicons name={focused ? "information-circle" : "information-circle-outline"} color={color} size={size} />
                ),
            }}
        />
        <Tabs.Screen
            name="profile" 
            options={{
                title: "Profile" ,
                tabBarIcon: ({color, size, focused}) => (
                    <Ionicons name={focused ? "person" : "person-outline"} color={color} size={size} />
                ),
            }}
        />
    </Tabs>
  )
}
