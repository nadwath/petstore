FROM node:18
WORKDIR /app
COPY package*.json ./
RUN npm ci && npm install -g @angular/cli@14.2.11
COPY . .
EXPOSE 4200
CMD ["npm","run","start"]
