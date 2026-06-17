# Build stage
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Run stage
FROM nginx:alpine
# Copy file build từ Vite (thường nằm ở thư mục dist) vào nginx
COPY --from=build /app/dist /usr/share/nginx/html
# Mở port
EXPOSE 80
# Chạy nginx
CMD ["nginx", "-g", "daemon off;"]