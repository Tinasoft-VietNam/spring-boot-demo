# Run Kong Gate way

## Run with Docker Compose

```yaml
services:
    kong:
        image: kong:3.7
        container_name: kong-gw
        environment:
            KONG_DATABASE: "off"
            KONG_DECLARATIVE_CONFIG: /kong/declarative/kong.yml
            KONG_PROXY_ACCESS_LOG: /dev/stdout
            KONG_ADMIN_ACCESS_LOG: /dev/stdout
            KONG_PROXY_ERROR_LOG: /dev/stderr
            KONG_ADMIN_ERROR_LOG: /dev/stderr
        ports:
            - "8000:8000"
            - "8001:8001"
        volumes:
            - ./kong.yml:/kong/declarative/kong.yml:ro
```

To run the above Docker Compose configuration, save it to a file named `docker-compose.yml` and execute the following command in the terminal:

```bash
docker-compose up -d
```
