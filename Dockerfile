FROM ubuntu:18.04


RUN apt-get update
RUN apt-get -y install software-properties-common
RUN add-apt-repository -y ppa:openjdk-r/ppa
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y openjdk-8-jre
RUN update-alternatives --config java
RUN update-alternatives --config javac

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre

RUN apt-get install -y wget

RUN wget www.scala-lang.org/files/archive/scala-2.12.13.deb
RUN dpkg -i scala-2.12.13.deb

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
RUN apt-get update

RUN apt-get install -y sbt

RUN curl -fsSL https://deb.nodesource.com/setup_15.x | bash -
RUN apt-get update
RUN apt-get install -y nodejs

EXPOSE 9000
EXPOSE 3000

VOLUME /common_vol
