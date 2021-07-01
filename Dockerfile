FROM ubuntu:18.04

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone 

RUN apt update && apt install -y build-essential unzip vim git curl wget zip

RUN apt-get update &&\
	apt-get upgrade -y &&\
    apt-get install -y  software-properties-common

EXPOSE 8080
EXPOSE 9000

RUN useradd -ms /bin/bash dstec
RUN adduser dstec sudo

USER dstec
WORKDIR /home/dstec/

RUN mkdir ebiznes
WORKDIR /home/dstec/ebiznes
COPY . .
WORKDIR /home/dstec/ebiznes/scala

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add
RUN apt-get update
RUN apt-get install -y sbt 1.5.2


CMD sbt run
