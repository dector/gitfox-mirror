import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:gitfox/auth_screen.dart';
import 'package:gitfox/interactor/platform/platform.dart';
import 'package:gitfox/projects_screen.dart';

/// Application entry point.
void main() => runApp(App());

/// Main application class.
class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'GitFox Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.green,
      ),
      home: SplashRouterWidget(),
    );
  }
}

/// Widget for initial routing base on the current session state
class SplashRouterWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return SplashRouterState();
  }
}

class SplashRouterState extends State {
  static const platform = const MethodChannel(METHOD_CHANNEL_NAME);

  @override
  void initState() {
    _hasAccount().then((bool hasAccount) {
      if (hasAccount) {
        _signInToLastSession().then(
          (_) => _openProjectsListScreen(),
        );
      } else {
        _openAuthScreen();
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.green,
    );
  }

  /// Check current account status via platform SDK
  Future<bool> _hasAccount() async {
    bool _hasAccount;
    try {
      _hasAccount = await platform.invokeMethod('hasAccount');
    } on PlatformException catch (_) {
      // do nothing
    }
    return _hasAccount;
  }

  /// Sign in to last session via platform SDK
  Future<void> _signInToLastSession() async {
    try {
      await platform.invokeMethod('signInToLastSession');
    } catch (_) {
      // do nothing
    }
  }

  /// Navigate to the [ProjectsListScreen].
  void _openAuthScreen() {
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => AuthScreen(),
      ),
    );
  }

  /// Navigate to the [ProjectsListScreen].
  void _openProjectsListScreen() {
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => ProjectsListScreen(),
      ),
    );
  }
}
