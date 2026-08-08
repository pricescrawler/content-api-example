# Prices Crawler - Content API Example

## 💻 Description

Reference implementation of
[prices-crawler-content-base](https://github.com/prices-crawler/content-base),
showing how to build a deployable content API for a catalog. Use it as the starting
point for your own implementation.

Implemented catalogs:

| # | Name    | Reference     |
|---|---------|---------------|
| 1 | example | local.example |

**How it works:** the application depends on `prices-crawler-content-controller` (which
pulls in the whole framework) and registers one `ProductService` bean per catalog,
qualified by `<locale>.<catalog>`. Each service extends `BaseProductService` and
implements three methods:

- `searchItemLogic` — search products by query on the source catalog;
- `searchItemByProductUrlLogic` — fetch a single product by its URL;
- `updateItemLogic` — refresh a product-list item.

Caching, price history, incidents and the REST API are provided by the framework.

## 📁 Requirements

| # | name    | Value   |
|---|---------|---------|
| 1 | `Java`  | `25`    |
| 2 | `Maven` | `3.9.6` |

## 🕹️ Getting Started

```bash
mvn clean package
export DATABASE_URL=mongodb://localhost:27017
export DATABASE_NAME=prices_crawler
java -jar target/*.jar
```

- API: `http://localhost:8080/api/v1/*`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🔗 Related Documentation

- [content-base documentation](https://github.com/prices-crawler/content-base) —
  framework setup, API reference and extension guide.
- [Ecosystem documentation](https://github.com/prices-crawler/documentation) —
  architecture, operations and roadmap.
