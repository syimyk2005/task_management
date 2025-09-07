FROM ubuntu:latest
LABEL authors="syimyk"

ENTRYPOINT ["top", "-b"]