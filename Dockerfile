FROM ubuntu:18.04

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone 

RUN apt update && apt install -y build-essential unzip vim git curl wget zip

RUN apt-get update &&\
	apt-get upgrade -y &&\
    apt-get install -y  software-properties-common

RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g npm@latest

EXPOSE 8080
EXPOSE 3000

RUN useradd -ms /bin/bash dstec
RUN adduser dstec sudo

USER dstec
WORKDIR /home/dstec/
RUN mkdir ebiznes
WORKDIR /home/dstec/ebiznes
COPY . .
WORKDIR /home/dstec/ebiznes/react/ebiznes
RUN npm install

CMD ["npm", "start"]
