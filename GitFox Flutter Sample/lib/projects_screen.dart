import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

/// User's projects list screen.
///
/// Available in authenticated zone only.
class ProjectsListScreen extends StatefulWidget {
  @override
  _ProjectsListScreenState createState() => _ProjectsListScreenState();
}

class _ProjectsListScreenState extends State<ProjectsListScreen> {
  static const platform = const MethodChannel('gitfox/oauth-url');

  @override
  void initState() {
    super.initState();
    _retrieveProjectsList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: const Text("My projects"),
      ),
    );
  }

  /// User's projects list retrieving via platform SDK
  Future<String> _retrieveProjectsList() async {
    // TODO change return type to List<Project>
    String projectsJson;
    try {
      projectsJson = await platform.invokeMethod('retrieveProjectsList');
    } on PlatformException catch (e) {
      setState(() {
        // TODO handle error
      });
    }
    return projectsJson;
  }
}
