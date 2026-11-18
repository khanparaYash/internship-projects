# Service boundaries

This repository contains three independently runnable Spring Boot applications. Each service owns its API, domain model, persistence mapping, and database schema. There is no shared application module or service-to-service client in the current implementation.

| Service | Default port | Owns | Persistence |
|---|---:|---|---|
| `authservice` | `8000` | User registration, password hashing, login, and JWT creation/validation | MySQL database `project5_Authentication`; `users` table |
| `catalogservice` | `8001` | Product categories and products, including search, filtering, and pagination | MySQL database `project5_Catalog`; `categories` and `products` tables |
| `orderservice` | `8002` | Order records, order items, order status transitions, and the current payment placeholder | MySQL database `project5_orders`; `orders` and `order_items` tables |

The configured databases are separate schemas on a local MySQL server by default. Database credentials and the JWT signing key are configuration.

## Ownership and responsibilities

### Authentication

`authservice` owns user accounts and credentials. It hashes passwords with BCrypt, authenticates email/password pairs, and issues signed bearer JWTs. Its security filter validates bearer tokens for requests handled by this application. Registration and login are public; other paths require authentication according to `SecurityConfig`.

### Catalog

`catalogservice` owns categories and products. A product belongs to a category within the catalog database; the category-to-product association is a local JPA relationship. Product listing, name search, and category/price filtering are catalog responsibilities.

The product and category controllers carry OpenAPI bearer-security annotations, but the catalog application currently has no Spring Security dependency or runtime JWT filter. Those annotations describe API documentation and do not enforce authentication. 

### Orders

`orderservice` owns order and order-item records and controls order lifecycle transitions. Orders store product IDs and price snapshots as order-item data; they do not have a database relationship to catalog products. Order creation currently trusts the submitted unit prices and does not call the catalog or check stock. Payment is currently a local status change from `CREATED` to `PAID`, not an integration with a payment provider.

The order controller currently has no authentication integration. Customer email is supplied by the caller, rather than derived from an authenticated identity.

## Cross-service responsibilities

- No service currently calls another service, publishes events, or shares a persistence schema.
- The authentication service is not currently integrated as a token issuer/validator for the catalog or order services.
- Catalog is the source of product and category data; orders retain only product IDs and submitted price/quantity snapshots.
- If cross-service consistency or authorization is added, use explicit APIs/events and service-owned data. 

## Boundary caveats

- Catalog product prices are represented as decimal values, while order item prices and totals are integers. Define a shared currency and amount convention before wiring order creation to the catalog.
- Order status updates are limited to the transitions documented in [the API contracts](api-contracts.md).
- The three services use independent configuration, Maven projects, and application lifecycles. There is no gateway, service discovery, centralized configuration.
