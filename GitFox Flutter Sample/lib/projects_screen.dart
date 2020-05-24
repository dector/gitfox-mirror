import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:gitfox/auth_screen.dart';
import 'package:gitfox/entity/project.dart';
import 'package:gitfox/ui/kit/error.dart';

/// User's projects list screen.
///
/// Available in authenticated zone only.
class ProjectsListScreen extends StatefulWidget {
  @override
  _ProjectsListScreenState createState() => _ProjectsListScreenState();
}

class _ProjectsListScreenState extends State<ProjectsListScreen> {
  static const platform = const MethodChannel('gitfox/platform');

  bool _isError = false;
  bool _isLoading = false;

  List<Project> _projects = List();

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
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.exit_to_app),
            iconSize: 26,
            onPressed: () {
              _logout();
            },
            splashColor: Colors.orange,
          ),
        ],
      ),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (!_isError) {
      return Stack(
        children: [
          _buildProjectsList(),
          if (_isLoading) _buildLoading(),
        ],
      );
    } else {
      return _buildError();
    }
  }

  Widget _buildProjectsList() {
    return ListView.builder(
        itemCount: _projects.length,
        itemBuilder: (BuildContext context, int index) {
          return _buildProjectListItem(index);
        });
  }

  Widget _buildProjectListItem(int index) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Row(
        children: <Widget>[
          _buildProjectAvatar(_projects[index].avatarUrl),
          _buildProjectName(_projects[index].name),
        ],
      ),
    );
  }

  Widget _buildProjectAvatar(String avatarUrl) {
    return Container(
      width: 50,
      height: 50,
      child: ClipOval(
        child: avatarUrl != null
            ? Container(
                child: Padding(
                  padding: const EdgeInsets.all(8),
                  child: Image.network(
                    avatarUrl,
                    fit: BoxFit.cover,
                  ),
                ),
              )
            : Container(
                color: Colors.black12,
              ),
      ),
    );
  }

  Widget _buildProjectName(String name) {
    return Flexible(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16),
        child: Text(
          name,
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(fontSize: 16),
        ),
      ),
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
        _retrieveProjectsList();
      },
    );
  }

  /// User's projects list retrieving via platform SDK
  Future<String> _retrieveProjectsList() async {
    setState(() {
      _isError = false;
      _isLoading = true;
    });
    String projectsJson;
    try {
      projectsJson = await platform.invokeMethod('retrieveProjectsList');
      var parsedJson = json.decode(projectsJson) as List;
      List<Project> projects = parsedJson
          .map(
            (model) => Project.fromJson(model),
          )
          .toList();
      setState(() {
        _isError = false;
        _isLoading = false;
        _projects = projects;
      });
    } catch (e) {
      setState(() {
        _isError = true;
        _isLoading = false;
      });
    }
    return projectsJson;
  }

  void _logout() async {
    setState(() {
      _isError = false;
      _isLoading = true;
    });
    bool hasOtherAccounts = await platform.invokeMethod('logout');
    if (!hasOtherAccounts) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => AuthScreen(),
        ),
      );
    }
    setState(() {
      _isError = false;
      _isLoading = false;
    });
  }
}
