import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/platform_interface.dart';
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

  WebViewController _webViewController;

  bool _isError = false;
  bool _isLoading = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("GitFox Demo"),
      ),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (!_isError) {
      return Stack(
        children: [
          _buildWebView(),
          if (_isLoading) _buildLoading(),
        ],
      );
    } else {
      return _buildError();
    }
  }

  Widget _buildWebView() {
    return WebView(
      onWebViewCreated: (WebViewController webViewController) {
        _webViewController = webViewController;
        _retrieveOAuthUrl();
      },
      onPageStarted: (_) {
        setState(() {
          _isLoading = true;
        });
      },
      onPageFinished: (_) {
        setState(() {
          _isLoading = false;
        });
      },
      onWebResourceError: (WebResourceError error) {
        setState(() {
          _isLoading = false;
          _isError = true;
        });
      },
    );
  }

  Widget _buildLoading() {
    return Center(
      child: CircularProgressIndicator(),
    );
  }

  Widget _buildError() {
    return Center(
      child: RaisedButton(
        onPressed: () {
          _retrieveOAuthUrl();
        },
        child: Text(
          "Повторить",
          style: TextStyle(
            fontSize: 20,
          ),
        ),
      ),
    );
  }

  /// Retrieve OAuth URL from cross-platform SDK
  Future<void> _retrieveOAuthUrl() async {
    setState(() {
      _isError = false;
    });
    String oAuthUrl;
    try {
      oAuthUrl = await platform.invokeMethod('getOAuthUrl');
      _webViewController.loadUrl(oAuthUrl);
    } on PlatformException catch (_) {
      setState(() {
        _isError = true;
      });
    }
  }
}
