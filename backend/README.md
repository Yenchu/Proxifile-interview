# Simple Company Search Service

  This is a demo application written in Java 11 with Maven build tool. It provides API for searching companies.

### Architecture

![Architecture Diagram](https://github.com/Proxifile/tech-interview-company-search-andrew/blob/main/backend/images/arch.png)

### API Design

![Sequence Diagram](https://github.com/Proxifile/tech-interview-company-search-andrew/blob/main/backend/images/design.png)

### Database Schema (DynamoDB)

- Primary partition key:
  + `name` (String)
- Primary sort key:
  + `address1` (String)
- Attributes:
  + `address2` (String)
  + `city` (String)
  + `state` (String)
  + `zip` (String)
  + `attention` (String)

### Build and Create Container Image

##### Build container image with Jib

- Build to Docker daemon

```shell
mvn compile jib:dockerBuild
```

- Push to AWS ECR:
  
  To push images to AWS ECR, you need to use an authorization token to authenticate docker with ECR:  

```shell
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin $ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com
```

  Tag image and push it to AWS ECR:

```shell
docker tag backend $ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com/demo-app:latest

docker push $ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com/demo-app:latest
```

- Build and Push to AWS ECR directly

```shell
mvn compile jib:build -Dimage=$ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com/demo-app:latest
```

### REST APIs

##### REST API doc

```
:8080/docs/api-guide.html
```

##### Health probes

- Liveness
    
```
:8080/actuator/health/liveness
```

- Readiness

```
:8080/actuator/health/readiness
```

##### Find company by name

```
:8080/companies/{name}
```