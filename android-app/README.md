# Cripto Radar Android

Projeto Android instalável (APK de teste e pacote AAB de release) para o Cripto Radar. A interface existente é empacotada localmente no aplicativo Android; cotações e gráficos consultam CoinGecko/Binance via HTTPS, sem chave de API.

- Application ID: `com.breno.criptoradar`
- Min SDK: 23; Target SDK: 36
- O workflow do GitHub Actions gera o APK de debug e o AAB de release como artefatos.
- Para publicação definitiva na Play Store, a versão AAB precisa ser assinada com a chave de upload do titular da conta Play Console.
