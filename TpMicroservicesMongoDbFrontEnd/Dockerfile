FROM node:8.11.2-alpine as node
#create a directory in container
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
