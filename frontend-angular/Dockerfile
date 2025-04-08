# Build stage
FROM node:20-alpine AS build
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm ci

# Copy remaining project files
COPY . .

# Build the application
RUN npm run build

# Deployment stage
FROM nginx:alpine
COPY --from=build /app/dist/frontend-angular/browser /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"] 