import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:gitfox/interactor/platform/platform.dart';
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
  static const platform = const MethodChannel(METHOD_CHANNEL_NAME);

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
      javascriptMode: JavascriptMode.unrestricted,
      onWebViewCreated: (WebViewController webViewController) {
        // cookies clearing is mandatory for Android to drop last session
        CookieManager().clearCookies();
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
        // ignore WebKitErrorDomain error on iOS
        if (error?.errorCode == 102 && error?.domain == "WebKitErrorDomain") {
          return;
        }
        setState(() {
          _isLoading = false;
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
      _webViewController.loadUrl(oAuthUrl);
    } catch (_) {
      setState(() {
        _isError = true;
      });
    }
  }

  /// Check OAuth redirect URL via platform SDK
  Future<bool> _checkOAuthRedirect(String url) async {
    bool isRedirected;
    try {
      isRedirected =
          await platform.invokeMethod('checkOAuthRedirect', <String, dynamic>{
        'url': url,
      });
    } catch (e) {
      setState(() {
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
        _isError = true;
      });
    }
    return isLogin;
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
