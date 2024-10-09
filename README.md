# NFLPool
Check pool results online

To compile the Java project:
1. At the project folder level (src's parent) run: rm target/nflpool-0.0.1-SNAPSHOT.jar
2. run mvn clean install

To build a docker image:
1. sudo docker images (take note of images that make reference to NFL Pool API)
2. sudo docker rmi <image-name>:<version> (for all images found)
3. sudo docker build nflpoolapi:1.0.0 .

To run a docker container:
1. sudo docker run -p 8080:8080 -v /path/to/couchbase/properties:/misc --name nflpoolapi nflpoolapi:1.0.0

To compile the ReactJS project:
1. npm run dev

To build a docker ReactJS image:
1. sudo docker build nflpoolsite:1.0.1 .

To run a docker ReactJS image:
1. sudo docker run -p 4000:4000 --name nflpoolsite nflpoolsite:1.0.1