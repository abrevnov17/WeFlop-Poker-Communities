FROM node:12

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY package.json ./

RUN npm install
RUN npm ci --only=production
ENV NODE_ENV production

ENV PGDATABASE accounts
ENV PGUSER weflop

# Bundle app source
COPY . .

EXPOSE 8082
CMD [ "node", "app.js" ]