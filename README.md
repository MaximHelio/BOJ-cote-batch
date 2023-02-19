# Cote-User-Computed Alarm
> 설치 방법
>SBApplication 실행 
> MySQL client 에서 kknni_tb_usr 테이블에 다음과 같이 입력
> localhost:9040/user/rank 로 보기

# Settings
Default Project Database Port 1366 - MySQL
Web Application Server Port - 8087

# How it works

```bash
|____common          # Project Common
|____config          # Spring Config
|____ctrl            # Controller
|____entity          # DB Table Entity
|____job             # Spring Batch
|____repo            # DB Table Repository
|____service         # Service
|SBApplication.java  # SpringBoot Application Main
```

# Getting started

```bash
# maven build
$ mvn clean package

# servce
$ java -jar target/**.war
```