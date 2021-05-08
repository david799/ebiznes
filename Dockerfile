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

RUN apt-get install -y wget build-essential unzip vim git curl zip

RUN wget www.scala-lang.org/files/archive/scala-2.12.13.deb
RUN dpkg -i scala-2.12.13.deb

RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get update
RUN apt-get install -y nodejs
RUN npm install -g npm@latest

RUN useradd -ms /bin/bash dstec
RUN adduser dstec sudo

USER dstec
WORKDIR /home/dstec/
RUN curl -S "https://get.sdkman.io" | bash
RUN chmod a+x "/home/dstec/.sdkman/bin/sdkman-init.sh"
RUN bash -c "source /home/dstec/.sdkman/bin/sdkman-init.sh && sdk install sbt 1.5.1"

EXPOSE 8080
EXPOSE 9000
EXPOSE 5000

VOLUME /common_vol

RUN mkdir ebiznes
WORKDIR /home/dstec/ebiznes/