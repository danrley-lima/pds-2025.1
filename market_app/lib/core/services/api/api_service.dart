import 'package:dio/dio.dart';
import '../../../modules/chat/models/assistant_recipe_model.dart';
import '../../models/category.dart';
import '../../models/product.dart';
import '../../models/promotion.dart';

class ApiService {
  Dio diohttp = Dio();

  Future<AssistantRecipeModel> getSuggestionRecipe(String requestMessage) async {
    try {
      final response = await diohttp.post('http://10.0.2.2:3000/api/recipes/suggest', data: {"receita": requestMessage});

      return AssistantRecipeModel.fromJson(response.data);
    } on DioException catch(e) {
      throw DioException(requestOptions: e.requestOptions);
    }
  }

  Future<List<Product>> getAllProducts() async {
    try {
      final response = await diohttp.get('http://10.0.2.2:3000/api/products');

      final List<dynamic> data = response.data;

      return data.map((element) => Product.fromJson(element)).toList();
    } on DioException catch(e) {
      throw DioException(requestOptions: e.requestOptions);
    }
  }

  Future<List<Promotion>> getActivePromotions() async {
    try {
      final response = await diohttp.get('http://10.0.2.2:3000/api/promotions');

      final List<dynamic> data = response.data;

      return data.map((element) => Promotion.fromJson(element)).toList();
    } on DioException catch(e) {
      throw DioException(requestOptions: e.requestOptions);
    }
  }

  Future<List<ProductCategory>> getAllCategories() async {
    try {
      final response = await diohttp.get('http://10.0.2.2:3000/api/categories');

      final List<dynamic> data = response.data;

      return data.map((element) => ProductCategory.fromJson(element)).toList();
    } on DioException catch(e) {
      throw DioException(requestOptions: e.requestOptions);
    }
  }
}