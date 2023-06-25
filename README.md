# Kotlin 멀티 모듈 템플릿

## :round_pushpin: 프로젝트 구조

### application 모듈

> **api 모듈**

- `api` : 컨트롤러와 서비스에 관련된 로직들이 포함되어 있습니다.
    - `controller` : 사용자 요청에 대해 유효성을 검증하고, 요청을 처리합니다.
    - `service` : `common`, `domain` 을 조합하여, 사용자 행위를 처리합니다.
- `exception` : 예외 클래스들을 관리합니다.
- `config` : 설정 클래스들을 관리합니다.
- `filter` : 필터 클래스들을 관리합니다.

> **batch 모듈**

- `batch`: 배치를 수행하는 스케줄러 로직들이 포함되어 있습니다.

### common 모듈

- `response` : 공통으로 사용하는 응답 클래스들을 관리합니다.
- `util` : 공통으로 사용하는 유틸리티 클래스들을 관리합니다.

### domain 모듈

> **nosql:redis 모듈**

- `config` : `redis` 와 관련된 설정 클래스들을 관리합니다.
- `dto` : DTO 클래스들을 관리합니다.
- `entity` : 엔티티 클래스들을 관리합니다.
- `repository` : 리포지토리 클래스들을 관리합니다.
- `service` : 서비스 클래스들을 관리합니다.

> **rdbms:jpa 모듈**

- `config` : `datasource`, `jpa` 와 관련된 설정 클래스들을 관리합니다.
- `dto` : DTO 클래스들을 관리합니다.
- `entity` : 엔티티 클래스들을 관리합니다.
- `mapper` : DTO 클래스와 엔티티를 매핑하는 매퍼 클래스들을 관리합니다.
- `repository` : 리포지토리 클래스들을 관리합니다.
- `service` : 서비스 클래스들을 관리합니다.

## :round_pushpin: 도커 컨테이너 실행

### MySQL

```shell
$ docker compose -f docker/mysql/docker-compose.yml up -d
```

## :round_pushpin: 애플리케이션 실행

### Prod 프로필로 실행

#### Jar 파일 실행

- `clean & build` 를 합니다.
- `-Dspring.profiles.active` 값을 `prod` 로 설정한 후, 실행합니다.

```shell
# clean & build
$ ./gradlew clean build

# start
$ java -jar -Dspring.profiles.active=prod \
application/api/build/libs/api-0.0.1-SNAPSHOT.jar
```

### Dev 프로필로 실행

#### Jar 파일 실행

- `clean & build` 를 합니다.
- `-Dspring.profiles.active` 값을 `dev` 로 설정한 후, 실행합니다.

```shell
# clean & build
$ ./gradlew clean build

# start
$ java -jar -Dspring.profiles.active=dev \
application/api/build/libs/api-0.0.1-SNAPSHOT.jar
```
