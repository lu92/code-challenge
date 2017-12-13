# code-challenge-twitter

There are two ways to build and run project:
1. Please execute following commands:
- cd /path of project
- mvn clean install
- mvn spring-boot:run
2. Install Docker for your operating system
https://docs.docker.com/engine/installation/#desktop
- cd /path of project
- build docker image: <br><b>docker build -f Dockerfile -t code-challenge .</b>
- run builded image: <br><b>docker run -p 8080:8080 -d  code-challenge</b>



helpfull commands related with Docker:
- show all running containers: <b>docker ps</b>
- stop running container:
    <br>find <b>CONTAINER_ID</b> in result of: <b>docker ps</b>
    <br>stop container by using       :<b>docker stop CONTAINER_ID</b>
- run internal bash layer in container: <b>docker exec -it CONTAINER_ID  /bin/bash</b>

# REST API:
    [POST] CREATE_USER("/createUser/{userNickname}"),
    [POST] SUBSCRIBE("/addFollower/{userNickname}/{followedNickname}"),
    [GET]  GET_FOLLOWERS("/getFollowers/{userNickname})
    [POST] TWEET("/tweet/{userNickname}") [CONTENT] (message),
    [POST] RETWEET("/tweet/{userNickname}/{parentTweetId}") [CONTENT] (message),
    [GET]  GET_TWEETS_WALL("/getWall/{userNickname}"),
    [GET]  GET_TWEETS_TIMELINE("/getTimeline/{userNickname}");
    
userNickname: String</br>
followerNickname: String</br>
parentTweetId: int</br>
message: String
