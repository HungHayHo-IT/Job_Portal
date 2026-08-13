# syntax=docker/dockerfile:1

# =========================================================
# Stage 1: Build React application
# =========================================================
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files trước để tận dụng cache
COPY package.json package-lock.json ./

# Cài đúng phiên bản trong package-lock.json
RUN npm ci

COPY . .

# Giá trị này được nhúng vào frontend tại thời điểm build
ARG VITE_API_BASE_URL=/api/v1
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

# Kiểm tra lint trước khi build production
RUN npm run lint
RUN npm run build


# =========================================================
# Stage 2: Serve static files using Nginx
# =========================================================
FROM nginx:alpine AS runtime

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost/health > /dev/null || exit 1

CMD ["nginx", "-g", "daemon off;"]