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

RUN curl -s "https://get.sdkman.io" | bash
RUN chmod a+x "/home/dstec/.sdkman/bin/sdkman-init.sh"
RUN bash -c "source /home/dstec/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.272.hs-adpt"
RUN bash -c "source /home/dstec/.sdkman/bin/sdkman-init.sh && sdk install sbt 1.5.2"
RUN bash -c "source /home/dstec/.sdkman/bin/sdkman-init.sh && sdk install scala 2.12.13"

CMD sbt run
