FROM node:latest AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY tsconfig.json ./
COPY src src
RUN npm run build

FROM node:latest
WORKDIR /app
ENV NODE_ENV=production
COPY package*.json ./
RUN npm install --omit=dev
COPY --from=build /app/dist dist
EXPOSE 8005
CMD ["node", "dist/index.js"]