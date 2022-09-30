package io.github.scafer.prices.crawler.service;

import io.github.scafer.prices.crawler.content.common.dto.product.ProductDto;
import io.github.scafer.prices.crawler.content.common.dto.product.search.SearchProductDto;
import io.github.scafer.prices.crawler.content.common.dto.product.search.SearchProductsDto;
import io.github.scafer.prices.crawler.content.common.util.DateTimeUtils;
import io.github.scafer.prices.crawler.content.repository.catalog.CatalogDataService;
import io.github.scafer.prices.crawler.content.repository.product.ProductDataService;
import io.github.scafer.prices.crawler.content.service.product.base.BaseProductService;
import io.github.scafer.prices.crawler.content.service.product.cache.ProductCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("local.example")
public class ExampleProductService extends BaseProductService {
    protected ExampleProductService(CatalogDataService catalogDataService, ProductDataService productDatabaseService, ProductCacheService productCacheService) {
        super("local", "example", catalogDataService, productDatabaseService, productCacheService);
    }

    @Override
    protected CompletableFuture<SearchProductsDto> searchItemLogic(String query) {
        var products = getExampleProductList();
        return CompletableFuture.completedFuture(new SearchProductsDto(localeName, catalogName, products, generateCatalogData()));
    }

    @Override
    protected CompletableFuture<SearchProductDto> searchItemByProductUrlLogic(String productUrl) {
        var product = getExampleProduct(productUrl);
        return CompletableFuture.completedFuture(new SearchProductDto(localeName, catalogName, product));
    }

    @Override
    protected CompletableFuture<SearchProductDto> updateItemLogic(SearchProductDto query) {
        var product = getExampleProduct("example");
        return CompletableFuture.completedFuture(new SearchProductDto(query.getLocale(), query.getCatalog(), product, query.getData()));
    }

    private List<ProductDto> getExampleProductList() {
        var productList = new ArrayList<ProductDto>();

        for (var i = 0; i < 10; i++) {
            productList.add(getExampleProduct(String.valueOf(i)));
        }

        return productList;
    }

    private ProductDto getExampleProduct(String query) {
        var product = new ProductDto();
        product.setReference(query);
        product.setName(String.format("Example Product %s", query));
        product.setBrand("Example Brand 1");
        product.setQuantity("1 /un");
        product.setDescription("Example Description 1");
        product.setEanUpcList(List.of("123456789"));
        product.setDate(DateTimeUtils.getCurrentDateTime());
        product.setRegularPrice("1,20€");
        product.setCampaignPrice("1,00€");
        product.setPricePerQuantity("1,00€ /un");
        product.setProductUrl(String.format("%s/%s", catalog.getBaseUrl(), query));
        product.setImageUrl(String.format("%s/%s.png", catalog.getBaseUrl(), query));
        return product;
    }
}
