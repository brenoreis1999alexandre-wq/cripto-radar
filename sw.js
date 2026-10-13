const CACHE = "cripto-radar-shell-v1";
const SHELL = ["./", "./index.html", "./manifest.webmanifest", "./icons/cripto-radar.svg"];
self.addEventListener("install", event => { event.waitUntil(caches.open(CACHE).then(cache => cache.addAll(SHELL)).then(() => self.skipWaiting())); });
self.addEventListener("activate", event => { event.waitUntil(caches.keys().then(keys => Promise.all(keys.filter(key => key !== CACHE).map(key => caches.delete(key)))).then(() => self.clients.claim())); });
self.addEventListener("fetch", event => {
  const req = event.request;
  if (req.method !== "GET") return;
  const url = new URL(req.url);
  if (url.origin !== self.location.origin) return; // Nunca guardar preços/API: manter dados ao vivo.
  event.respondWith(fetch(req).then(response => {
    if (response.ok && (req.mode === "navigate" || SHELL.some(path => new URL(path, self.location.href).pathname === url.pathname))) {
      const copy = response.clone(); caches.open(CACHE).then(cache => cache.put(req, copy));
    }
    return response;
  }).catch(async () => (await caches.match(req)) || (req.mode === "navigate" ? await caches.match("./index.html") : Response.error())));
});
