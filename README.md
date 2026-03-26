# Microservices

This project demonstrates a microservices architecture using Spring Boot. It includes multiple services such as User Service, Order Service, and Product Service, each running in its own container. The infrastructure is set up using Docker Compose, which includes Redis for caching, Kafka for messaging, Elasticsearch for logging, Logstash for log processing, and Kibana for log visualization.

## Services

- **User Service**: Manages user information and authentication.
- **Business Service**: Handles business processing and management.

## Infrastructure

The infrastructure is defined in the `docker-compose-infra.yml` file, which includes the following services:

- **Redis**: Used for caching frequently accessed data to improve performance.
- **Kafka**: Used for asynchronous communication between services.
- **Elasticsearch**: Used for storing and searching logs.
- **Logstash**: Used for processing logs from the Spring Boot applications and sending them to Elasticsearch.
- **Kibana**: Used for visualizing logs stored in Elasticsearch.

## Running the Application

1. Make sure you have Docker and Docker Compose installed on your machine.
2. Navigate to the project directory and run the following command to start the infrastructure:
    ```bash
    docker-compose -f docker-compose-infra.yml up -d
    ```
3. Start each of the Spring Boot services (User Service, Order Service, Product Service) using your IDE or by running the respective JAR files.
4. Access the services via their respective endpoints (e.g., `http://localhost:8080/users` for User Service).
5. To view logs in Kibana, navigate to `http://localhost:5601`
