import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';

class ApiService {
  Dio diohttp = Dio();

  Future<void> getAllProducts() async {
    try {
      final response = await diohttp.get('localhost:3000/api/products');

      debugPrint(response.data);
    } on DioException catch(e) {
      debugPrint(e.message);
    }
  }

  Future<void> getActivePromotions() async {
    try {
      final response = await diohttp.get('localhost:3000/api/promocoes-ativas');

      debugPrint(response.data);
    } on DioException catch(e) {
      debugPrint(e.message);
    }
  }

  Future<void> getAllCategories() async {
    try {
      final response = await diohttp.get('localhost:3000/api/categories');

      debugPrint(response.data);
    } on DioException catch(e) {
      debugPrint(e.message);
    }
  }
}