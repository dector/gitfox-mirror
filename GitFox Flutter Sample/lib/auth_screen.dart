import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:gitfox/projects_screen.dart';
import 'package:gitfox/ui/kit/error.dart';
import 'package:webview_flutter/platform_interface.dart';
import 'package:webview_flutter/webview_flutter.dart';

/// Gitlab OAuth [WebView] screen.
class AuthScreen extends StatefulWidget {
  @override
  _AuthScreenState createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  static const platform = const MethodChannel('gitfox/platform');

  WebViewController _webViewController;

  bool _isError = false;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    print("1111 current url = ${_webViewController?.currentUrl()}");
    _hasAccount().then((bool hasAccount) {
      print("1111 _hasAccount = $hasAccount");
      if (hasAccount) {
        print("1111 _openProjectsListScreen 1");
        _signInToLastSession().then(
          (_) => _openProjectsListScreen(),
        );
      }
    });
  }

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
      javascriptMode: JavascriptMode.unrestricted,
      onWebViewCreated: (WebViewController webViewController) {
        _webViewController = webViewController;
        print("1111 onWebViewCreated = ${_webViewController.hashCode}");
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
          print("1111 _isError 2 = ${error.description}");
          _isError = true;
        });
      },
      navigationDelegate: (NavigationRequest action) {

        String url = action.url;
        return _checkOAuthRedirect(url).then((bool isRedirected) {
          if (isRedirected) {
            setState(() {
              _isLoading = true;
            });
            _login(url).then((bool isLogin) {
              if (isLogin) {
                print("1111 _openProjectsListScreen 2");
                _openProjectsListScreen();
              }
              setState(() {
                _isLoading = false;
              });
            });
            return NavigationDecision.prevent;
          } else {
            return NavigationDecision.navigate;
          }
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
    return ErrorStateWidget(
      onPressed: () {
        _retrieveOAuthUrl();
      },
    );
  }

  /// Retrieve OAuth URL via platform SDK
  Future<void> _retrieveOAuthUrl() async {
    setState(() {
      _isError = false;
    });
    String oAuthUrl;
    try {
      oAuthUrl = await platform.invokeMethod('getOAuthUrl');
      print("1111 oAuthUrl = $oAuthUrl");
      //_webViewController.clearCache();
      _webViewController.loadUrl(oAuthUrl);
    } catch (_) {
      setState(() {
        print("1111 _isError 3");
        _isError = true;
      });
    }
  }

  /// Check OAuth redirect URL via platform SDK
  Future<bool> _checkOAuthRedirect(String url) async {
    print("1111 check redirect = $url");
    bool isRedirected;
    try {
      isRedirected =
          await platform.invokeMethod('checkOAuthRedirect', <String, dynamic>{
        'url': url,
      });
    } catch (e) {
      setState(() {
        print("1111 _isError 4");
        _isError = true;
      });
    }
    return isRedirected;
  }

  /// Login via platform SDK
  Future<bool> _login(String url) async {
    bool isLogin;
    try {
      isLogin = await platform.invokeMethod('login', <String, dynamic>{
        'url': url,
      });
    } catch (_) {
      setState(() {
        print("1111 _isError 1");
        _isError = true;
      });
    }
    return isLogin;
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
  void _openProjectsListScreen() {
    print("1111 _openProjectsListScreen");
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => ProjectsListScreen(),
      ),
    );
  }
}
