import 'package:flutter/material.dart';
import 'package:gitfox/auth_screen.dart';

/// Application entry point.
void main() => runApp(App());

/// Main application class.
class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'GitFox Demo',
      theme: ThemeData(
        primarySwatch: Colors.green,
      ),
      home: AuthScreen(),
    );
  }
}
