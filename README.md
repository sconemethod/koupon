## 1단계: 로컬 인프라 구성 (Kafka, Redis, MySQL)

Kafka, Redis, MySQL을 `docker-compose`로 로컬에서 실행합니다.

---

### ✅ 사용한 이미지와 버전

| 서비스     | 이미지                         | 버전   | 포트 매핑         |
|------------|----------------------------------|--------|-------------------|
| Zookeeper  | confluentinc/cp-zookeeper       | 7.5.0  | 2181:2181         |
| Kafka      | confluentinc/cp-kafka           | 7.5.0  | 9092:9092         |
| Redis      | redis                           | 7.2    | 6379:6379         |
| MySQL      | mysql                           | 8.3    | 3306:3306         |
| Kafka Connect (Optional) | confluentinc/cp-kafka-connect | 7.5.0 | 8083:8083 |

---

### ⚙️ .env 설정 예시

```env
# Kafka
KAFKA_BROKER_ID=1
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1

# MySQL
MYSQL_ROOT_PASSWORD=root1234
MYSQL_DATABASE=coupon
MYSQL_USER=user
MYSQL_PASSWORD=password
