###############
#  Redis
###############
resource "docker_image" "redis" {
  name = "redis:7.2"
}

resource "docker_container" "redis" {
  name  = "koupon-redis"
  image = docker_image.redis.image_id
  ports { internal = 6379 external = 6379 }
}

###############
#  MySQL
###############
resource "docker_image" "mysql" {
  name = "mysql:8.3"
}

resource "docker_container" "mysql" {
  name  = "koupon-mysql"
  image = docker_image.mysql.image_id

  env = [
    "MYSQL_ROOT_PASSWORD=${var.mysql_root_password}",
    "MYSQL_DATABASE=coupon",
    "MYSQL_USER=user",
    "MYSQL_PASSWORD=password",
    "MYSQL_ROOT_HOST=%"
  ]

  ports { internal = 3306 external = 3306 }
  healthcheck {
    test = ["CMD", "mysqladmin" ,"-uuser", "-ppassword", "ping", "-h", "127.0.0.1"]
  }
}

###############
#  Zookeeper
###############
resource "docker_image" "zookeeper" {
  name = "confluentinc/cp-zookeeper:7.5.0"
}

resource "docker_container" "zookeeper" {
  name  = "koupon-zookeeper"
  image = docker_image.zookeeper.image_id
  env   = ["ZOOKEEPER_CLIENT_PORT=2181"]
  ports { internal = 2181 external = 2181 }
}

###############
#  Kafka
###############
resource "docker_image" "kafka" {
  name = "confluentinc/cp-kafka:7.5.0"
}

resource "docker_container" "kafka" {
  name         = "koupon-kafka"
  image        = docker_image.kafka.image_id
  depends_on   = [docker_container.zookeeper]

  env = [
    "KAFKA_BROKER_ID=1",
    "KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181",
    "KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092,INTERNAL://kafka:29092",
    "KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,INTERNAL:PLAINTEXT",
    "KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL",
    "KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1"
  ]

  ports { internal = 9092 external = 9092 }
}
