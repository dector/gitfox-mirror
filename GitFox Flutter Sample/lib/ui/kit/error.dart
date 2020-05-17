import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

/// Error state widget
class ErrorStateWidget extends StatelessWidget {
  const ErrorStateWidget({
    Key key,
    @required this.onPressed,
  }) : super(key: key);

  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: FlatButton(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(4),
          side: BorderSide(color: Colors.green),
        ),
        onPressed: onPressed,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(
            "Повторить",
            style: TextStyle(
              fontSize: 20,
              color: Colors.green,
            ),
          ),
        ),
      ),
    );
  }
}
