import 'package:dio/dio.dart';

class ApiService {
  Dio diohttp = Dio();
  
  Future<String> getProducts(String prompt) async {
    await Future.delayed(const Duration(seconds: 1));
    return "Aqui estão alguns produtos recomendados com base em: $prompt";
  }

  Future<String> getRecipes(String prompt) async {
    await Future.delayed(const Duration(seconds: 1));
    return "Essas são algumas receitas que combinam com: $prompt";
  }

  Future<String> getPromotions(String prompt) async {
    await Future.delayed(const Duration(seconds: 1));
    return "Veja promoções para ingredientes de: $prompt";
  }
}