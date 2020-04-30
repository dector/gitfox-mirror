import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'GitFox Demo',
      theme: ThemeData(
        primarySwatch: Colors.green,
      ),
      home: MainScreen(),
    );
  }
}

class MainScreen extends StatefulWidget {
  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  static const platform = const MethodChannel('gitfox/oauth-url');
  String _oAuthUrl = "";

  @override
  void initState() {
    _retrieveOAuthUrl();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("GitFox Demo"),
      ),
      body: Text(
        "URL: $_oAuthUrl",
      ) /*todo вернуть обратно WebView(
        onWebViewCreated: (webViewController) {
          webViewController.loadUrl("https://yandex.ru");
        },
      )*/
      ,
    );
  }

  /// Retrieve OAuth URL from cross-platform SDK
  Future<void> _retrieveOAuthUrl() async {
    String oAuthUrl;
    try {
      oAuthUrl = await platform.invokeMethod('getOAuthUrl');
    } on PlatformException catch (e) {
      oAuthUrl = "";
    }

    setState(() {
      _oAuthUrl = oAuthUrl;
    });
  }
}
