# Build Stage
FROM node:20 as build-stage

# 폴더 위치
WORKDIR /app

# package.json 이름을 가진 모든 파일을 app 경로로 복사한다.
COPY package*.json ./

# 필요한 종속성을 모두 설치한다.
RUN npm install

# 현재 폴더의 모든 내용을 옮긴다.
COPY . .

# 빌드파일을 생성한다.
RUN npm run build

FROM nginx:stable-alpine as production-stage
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /app/nginx/default.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /usr/share/nginx/html/*
COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx","-g","daemon off;"]