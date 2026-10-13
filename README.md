# Cripto Radar

App web/PWA em português para acompanhar cotações e gráficos de criptomoedas em BRL.

## Dados e imagens

O app usa endpoints públicos da CoinGecko e da Binance; a versão atual não exige chave de API. As chamadas são feitas pelo navegador e precisam de internet. O service worker mantém apenas os arquivos do app para abertura offline; não armazena cotações em cache.

## Publicação

O projeto pode ser servido como site estático por GitHub Pages. `index.html` referencia `manifest.webmanifest`, ícone local e `sw.js`.
