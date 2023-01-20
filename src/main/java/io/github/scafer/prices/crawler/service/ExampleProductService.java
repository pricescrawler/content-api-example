package io.github.scafer.prices.crawler.service;

import io.github.scafer.prices.crawler.content.common.dto.product.ProductDto;
import io.github.scafer.prices.crawler.content.common.dto.product.ProductListItemDto;
import io.github.scafer.prices.crawler.content.common.dto.product.search.SearchProductDto;
import io.github.scafer.prices.crawler.content.common.dto.product.search.SearchProductsDto;
import io.github.scafer.prices.crawler.content.common.util.DateTimeUtils;
import io.github.scafer.prices.crawler.content.common.util.IdUtils;
import io.github.scafer.prices.crawler.content.repository.catalog.CatalogDataService;
import io.github.scafer.prices.crawler.content.repository.product.ProductDataService;
import io.github.scafer.prices.crawler.content.service.product.base.BaseProductService;
import io.github.scafer.prices.crawler.content.service.product.cache.ProductCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("local.example")
public class ExampleProductService extends BaseProductService {
    Random random = new Random();

    protected ExampleProductService(CatalogDataService catalogDataService,
                                    ProductDataService productDatabaseService,
                                    ProductCacheService productCacheService) {
        super("local", "example", catalogDataService, productDatabaseService, productCacheService);
    }

    @Override
    protected CompletableFuture<SearchProductsDto> searchItemLogic(String query) {
        var products = parseProductsFromContent(query, DateTimeUtils.getCurrentDateTime());
        return CompletableFuture.completedFuture(new SearchProductsDto(localeName, catalogName, products, generateCatalogData()));
    }

    @Override
    protected CompletableFuture<SearchProductDto> searchItemByProductUrlLogic(String productUrl) {
        var product = parseProductFromContent(productUrl, "1", DateTimeUtils.getCurrentDateTime());
        return CompletableFuture.completedFuture(new SearchProductDto(localeName, catalogName, product));
    }

    @Override
    protected CompletableFuture<ProductListItemDto> updateItemLogic(ProductListItemDto productListItem) {
        var product = parseProductFromContent("example", "1", DateTimeUtils.getCurrentDateTime());
        return CompletableFuture.completedFuture(new ProductListItemDto(productListItem.getLocale(), productListItem.getCatalog(), product, productListItem.getData(), productListItem.getQuantity(), productListItem.isHistoryEnabled()));
    }

    @Override
    public List<ProductDto> parseProductsFromContent(String content, String dateTime) {
        var productList = new ArrayList<ProductDto>();

        for (var i = 0; i < 10; i++) {
            productList.add(parseProductFromContent(String.valueOf(i), String.valueOf(i), dateTime));
        }

        return productList;
    }

    @Override
    public ProductDto parseProductFromContent(String content, String query, String dateTime) {
        return ProductDto.builder()
                .id(IdUtils.parse(localeName, catalogName, query))
                .reference(query)
                .name(String.format("Example Product %s", query))
                .brand("Example Brand 1")
                .quantity("1 /un")
                .description("Example Description 1")
                .eanUpcList(List.of("123456789"))
                .regularPrice("1,20€")
                .campaignPrice(random.nextBoolean() ? "1,00€" : null)
                .pricePerQuantity("1,00€ /un")
                .productUrl(String.format("%s/%s", catalog.getBaseUrl(), query))
                .imageUrl("https://via.placeholder.com/150")
                .build();
    }
}
