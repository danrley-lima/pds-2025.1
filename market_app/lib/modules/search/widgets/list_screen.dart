import 'package:flutter/material.dart';

class ListScreen extends StatefulWidget {
  final String title;

  const ListScreen({super.key, required this.title});

  @override
  State<ListScreen> createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Column(),
    );
  }
}