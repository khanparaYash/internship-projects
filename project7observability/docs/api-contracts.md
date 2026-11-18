# API contracts

This document describes the HTTP endpoints currently exposed by the three services. Default local base URLs are `http://localhost:8000` (authentication), `http://localhost:8001` (catalog), and `http://localhost:8002` (orders). Send and receive JSON unless noted otherwise.

## Authentication service

Base URL: `http://localhost:8000`

| Method | Path | Request                                                            | Success |
|---|---|--------------------------------------------------------------------|---|
| `POST` | `/auth/register` | `{"name":"Yash","email":"yash@example.com","password":"Yash@123"}` | `201 Created`, response text `User registered successfully` |
| `POST` | `/auth/login` | `{"email":"yash@example.com","password":"Yash@123"}`               | `200 OK`, `LoginResponse` |

Registration requires a nonblank name, valid email, and password of at least six characters. A registered email is rejected with `400 Bad Request`. Invalid login credentials return `401 Unauthorized`.

`LoginResponse`:

```json
{
  "token": "<signed JWT>",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

The token lifetime shown is the current default configuration; deployments may override it. When calling a service that enforces bearer authentication, use `Authorization: Bearer <token>`. At present, only `authservice` validates JWTs at runtime.

## Catalog service

Base URL: `http://localhost:8001`

### Categories

| Method | Path | Request | Success |
|---|---|---|---|
| `POST` | `/api/categories` | `{"name":"Books","description":"Printed books"}` | `201 Created`, `CategoryResponse` |
| `GET` | `/api/categories` | — | `200 OK`, array of `CategoryResponse` |
| `GET` | `/api/categories/{id}` | — | `200 OK`, `CategoryResponse` |

Category names must be nonblank and unique. A missing category returns `404 Not Found`.

### Products

| Method | Path | Request or query parameters | Success |
|---|---|---|---|
| `POST` | `/api/products` | `{"name":"Notebook","price":4.5,"quantity":20,"categoryId":1,"imageUrl":"https://example.test/notebook.png"}` | `201 Created`, `ProductResponse` |
| `PUT` | `/api/products/{id}` | Same body as product creation | `200 OK`, `ProductResponse` |
| `DELETE` | `/api/products/{id}` | — | `200 OK`, empty body |
| `GET` | `/api/products/{id}` | — | `200 OK`, `ProductResponse` |
| `GET` | `/api/products` | `page`, `size`, `sort` | `200 OK`, `ProductPageResponse` |
| `GET` | `/api/products/search` | `name`, `page`, `size`, `sort` | `200 OK`, `ProductPageResponse` |
| `GET` | `/api/products/filter` | `categoryId`, `minPrice`, `maxPrice`, `page`, `size`, `sort` | `200 OK`, `ProductPageResponse` |

Product names must be nonblank, prices must be nonnegative, quantities must be nonnegative, and `categoryId` must refer to an existing category. A missing product or category returns `404 Not Found`.

For list, search, and filter, defaults are `page=0`, `size=10`, and `sort=price,asc`. Pages are zero-based. `sort` is a comma-separated property and direction, such as `price,desc`; only `desc` selects descending order, and other directions use ascending order. Search matches product names case-insensitively; omitted or blank `name` returns the unfiltered page. Filters can be combined; omitted filters are ignored.

Response shapes:

```json
{
  "id": 1,
  "name": "Notebook",
  "price": 4.5,
  "quantity": 20,
  "categoryName": "Books",
  "imageUrl": "https://example.test/notebook.png"
}
```

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

`CategoryResponse` contains `id`, `name`, `description`, and `products` (an array of `ProductResponse`). The catalog controllers declare a bearer requirement in OpenAPI, but this does not currently enforce runtime authentication; see [service boundaries](service-boundaries.md#catalog).

## Order service

Base URL: `http://localhost:8002`

| Method | Path | Request | Success |
|---|---|---|---|
| `POST` | `/orders` | `OrderRequest` | `201 Created`, `OrderResponse` |
| `GET` | `/orders/{orderId}` | — | `200 OK`, `OrderResponse` |
| `GET` | `/orders` | — | `200 OK`, array of `OrderResponse` |
| `PATCH` | `/orders/{orderId}/status` | `{"status":"SHIPPED"}` | `200 OK`, `OrderResponse` |
| `POST` | `/orders/{orderId}/payment` | — | `200 OK`, response text confirming payment |
| `POST` | `/orders/{orderId}/cancel` | — | `200 OK`, response text confirming cancellation |

`OrderRequest` requires a customer email and at least one item. Each item requires a positive `productId`, a positive `quantity`, and a nonnegative integer `unitPrice`:

```json
{
  "customerEmail": "customer@example.com",
  "items": [
    {"productId": 101, "quantity": 2, "unitPrice": 1999}
  ]
}
```

The service calculates each `lineTotal` as `quantity * unitPrice` and the order `totalAmount` from the submitted items. It currently does not validate the product, price, or stock with the catalog service.

### Order → Catalog contract

**Status: the endpoint exists, but `orderservice` does not currently call it.** The following describes the existing catalog endpoint that can be used to look up a product before an order is accepted; it is not a claim that order validation or stock reservation is implemented.

For each requested order item, the order service can request the product from the catalog:

```http
GET http://localhost:8001/api/products/{productId}
Accept: application/json
```

Example response (`ProductResponse`):

```json
{
  "id": 101,
  "name": "Notebook",
  "price": 4.5,
  "quantity": 20,
  "categoryName": "Books",
  "imageUrl": "https://example.test/notebook.png"
}
```

The response `id` identifies the product, `price` is its current catalog price, and `quantity` is the currently recorded catalog quantity. A missing product returns `404 Not Found`; a successful lookup returns `200 OK`. The catalog endpoint does not reserve or decrement inventory, so a successful quantity check is only a snapshot and concurrent orders may still oversell.

An order integration should treat catalog data as authoritative rather than trusting a caller-supplied price: reject an unknown product, check that the returned quantity is sufficient, and snapshot the returned price with the order item.  If reliable stock reservation is required, the catalog API needs a reservation/adjustment operation rather than a read-only lookup.


Order states are `CREATED`, `PAID`, `SHIPPED`, `DELIVERED`, and `CANCELLED`. Allowed forward transitions through the status endpoint are `CREATED` → `PAID` → `SHIPPED` → `DELIVERED`. The payment endpoint changes a `CREATED` order to `PAID`; cancellation is allowed from `CREATED` or `PAID`. Other transitions return `400 Bad Request`; missing orders return `404 Not Found`.

`OrderResponse` contains `id`, `customerEmail`, `status`, integer `totalAmount`, `products` (order-item responses), `createdAt`, and `updatedAt`. Each order item contains `id`, `productId`, `quantity`, integer `unitPrice`, and `lineTotal`.

The order controller has no authentication configured. Do not assume its endpoints require the token issued by `authservice`.

## Error responses

Catalog and authentication APIs return structured errors with `timestamp`, `status`, `error`, `message`, and `path` for handled application errors. Order errors use `timestamp`, `status`, `message`, and `path`. Validation and domain errors are generally `400`; missing resources are `404`. Authentication errors from `authservice` are `401`, and authorization failures handled by its security configuration are `403`.
