FROM node:16-alpine as build-step
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
RUN npm install --force
RUN npm install ngx-cookie-service --force
RUN npm install leaflet --force
RUN npm install @asymmetrik/ngx-leaflet --force
RUN npm install --save-dev @types/leaflet --force
COPY . /app
RUN npm run build --prod
FROM nginx:alpine
COPY --from=build-step /app/dist/rest-client /usr/share/nginx/html
COPY --from=build-step /app/nginx-custom.conf /etc/nginx/conf.d/default.conf
