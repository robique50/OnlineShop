# Etapa de build: Folosim Node.js pentru a compila aplicația Angular
FROM node:18 AS build

# Setăm directorul de lucru în container
WORKDIR /app

# Copiem fișierele package.json și package-lock.json (sau yarn.lock)
COPY package*.json ./

# Instalăm dependențele cu flag pentru a ignora conflictele de dependențe
RUN npm install --legacy-peer-deps

# Copiem restul fișierelor proiectului
COPY . .

# Construim aplicația pentru producție
RUN npm run build --prod

# Etapa de rulare: Folosim nginx pentru a servi aplicația Angular
FROM nginx:alpine

# Copiem fișierele compilate din etapa de build în directorul nginx
COPY --from=build /app/dist/online-shop/browser /usr/share/nginx/html

# Copiem configurația nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expunem portul 80
EXPOSE 80

# Nginx pornește automat când containerul este rulat