# Stage 1: build the Angular app
FROM node:24-alpine AS build
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npx ng build

# Stage 2: serve it with Caddy
FROM caddy:2
COPY --from=build /app/dist/frontend/browser /srv
