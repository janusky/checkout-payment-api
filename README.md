# checkout-payment-api

API de pagos que permite realizar transacciones asociadas a una sesión de checkout.

## Objetivo

* Implementar una API de pagos que permita realizar transacciones asociadas a una sesión de checkout.
* Registrar el estado de pago (exitoso, pendiente, fallido, etc.).

## Contenido

- Sesión con JWT y Roles (package `security`)
- Documentación con OpenAPI 3
- Cache en servicios
- Registro de log con ticked id para errores
- Persistencia en modelo relacional (JPA)
- Auditoría con @EnableJpaAuditing (package `audit`)

### Directorios

1. `/src/` contiene el código fuente del backend
1. `/config/` contiene las plantillas de los archivos de configuración para los distintos ambientes

### Tecnologías

- Java 17
- [Spring Boot WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
- [Reactor Netty](https://projectreactor.io/docs/netty/snapshot/reference/index.html).
- Authentication x509 (SSL)
- PostgreSQL (database)
- [Maven 3](https://maven.apache.org/)
- [Lombok](https://projectlombok.org)
- ~~[OpenAPI 3](https://spec.openapis.org/oas/v3.1.0)~~

## Uso

Ejecutar utilizado Docker Compose

* [Run Development](DEV.md#run)

```sh
# Descargar la versión deseada (por ejemplo: `master`)
git clone https://github.com/janusky/checkout-payment-api.git

# Docker Compose
docker-compose up -d --build
```

### Observar estado de ejecución en Navegador/Browser

>Debe contar con el certificado cliente [client.localhost.pfx](src/test/resources/ssl/client.localhost.pfx) instalado en el Browser/Navegador utilizado.

* Administrar certificados -> Tus certificados -> importar
* Password `storepass` de [client.localhost.pfx](src/test/resources/ssl/client.localhost.pfx)

Acceder: <https://local.localhost:8443/> o <https://localhost:8443/info>

## Test

Se crea una sesión de checkout y luego se ejecuta el pago.

>Ver estado de aplicación en browser -> https://localhost:8443/status  
>Acceso a base de datos
>```sh
>psql -h localhost -U payment -d payment -p 5432
>```

Se crea una orden de sesión de checkout

```sh
# Acceder donde se encuentran los certificados
cd checkout-payment-api/src/test/resources/ssl

# Create checkout session
curl -k --insecure -X POST https://localhost:8443/api/v1/orders/items \
	-H 'Content-Type: application/json' \
	--cert ./client.pem:storepass --key ./privkey.pem
```

Se paga una orden de sesión de checkout

```sh
# Acceder donde se encuentran los certificados
cd checkout-payment-api/src/test/resources/ssl

# Payment Success (OrderState.paymentSuccessEmailPending)
curl -k --insecure -X POST https://localhost:8443/api/v1/orders/ac0355f9-482a-4d6d-a41b-710725478757/payment/3 \
	-H 'Content-Type: application/json' \
	--cert ./client.pem:storepass --key ./privkey.pem

# Payment Pending (OrderEvent.paymentErrorEmailSent) 
curl -k --insecure -X POST https://localhost:8443/api/v1/orders/ac0355f9-482a-4d6d-a41b-710725478757/payment/0 \
	-H 'Content-Type: application/json' \
	--cert ./client.pem:storepass --key ./privkey.pem
```

## TODO

* Cambiar el acceso a Postgres de JPA por R2DBC reactivo.
* Incorporar TEST UNIT
* OpenApi test

## Documentos

1. [Guía de desarrollo](DEV.md)
