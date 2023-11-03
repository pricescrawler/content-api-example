package io.github.pricescrawler.service;

import io.github.pricescrawler.content.common.dao.catalog.CatalogDao;
import io.github.pricescrawler.content.common.dto.product.ProductDto;
import io.github.pricescrawler.content.common.dto.product.ProductListItemDto;
import io.github.pricescrawler.content.common.dto.product.filter.FilterProductByQueryDto;
import io.github.pricescrawler.content.common.dto.product.filter.FilterProductByUrlDto;
import io.github.pricescrawler.content.common.dto.product.search.SearchProductDto;
import io.github.pricescrawler.content.common.dto.product.search.SearchProductsDto;
import io.github.pricescrawler.content.common.util.DateTimeUtils;
import io.github.pricescrawler.content.common.util.IdUtils;
import io.github.pricescrawler.content.repository.catalog.CatalogDataService;
import io.github.pricescrawler.content.repository.product.history.ProductHistoryService;
import io.github.pricescrawler.content.service.product.base.BaseProductService;
import io.github.pricescrawler.content.service.product.cache.ProductCacheService;
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
                                    ProductHistoryService productHistoryService,
                                    ProductCacheService productCacheService) {
        super("local", "example", catalogDataService, productHistoryService, productCacheService);
    }

    @Override
    protected CompletableFuture<SearchProductsDto> searchItemLogic(FilterProductByQueryDto filterProduct) {
        var products = parseProductsFromContent(filterProduct.getComposedCatalogKey(), filterProduct.getQuery(), DateTimeUtils.getCurrentDateTime());
        return CompletableFuture.completedFuture(new SearchProductsDto(localeId, filterProduct.getComposedCatalogKey(), products, generateCatalogData(filterProduct.getStoreId())));
    }

    @Override
    protected CompletableFuture<SearchProductDto> searchItemByProductUrlLogic(FilterProductByUrlDto filterProductByUrl) {
        var product = parseProductFromContent(filterProductByUrl.getComposedCatalogKey(), filterProductByUrl.getUrl(), "1", DateTimeUtils.getCurrentDateTime());
        return CompletableFuture.completedFuture(new SearchProductDto(localeId, filterProductByUrl.getComposedCatalogKey(), product));
    }

    @Override
    protected CompletableFuture<ProductListItemDto> updateItemLogic(ProductListItemDto productListItem) {
        var product = parseProductFromContent(productListItem.getCatalog(), "example", "1", DateTimeUtils.getCurrentDateTime());
        return CompletableFuture.completedFuture(new ProductListItemDto(productListItem.getLocale(), productListItem.getCatalog(), product, productListItem.getData(), productListItem.getQuantity(), productListItem.isHistoryEnabled()));
    }

    @Override
    public List<ProductDto> parseProductsFromContent(String catalogKey, String content, String dateTime) {
        var productList = new ArrayList<ProductDto>();

        for (var i = 0; i < 10; i++) {
            productList.add(parseProductFromContent(catalogKey, String.valueOf(i), String.valueOf(i), dateTime));
        }

        return productList;
    }

    @Override
    public ProductDto parseProductFromContent(String catalogKey, String query, String content, String dateTime) {
        return ProductDto.builder()
                .id(IdUtils.parse(localeId, catalogKey, query))
                .reference(query)
                .name(String.format("Example Product %s", query))
                .brand("Example Brand 1")
                .quantity("1 /un")
                .description("Example Description 1")
                .eanUpcList(List.of("123456789"))
                .regularPrice("1,20€")
                .campaignPrice(random.nextBoolean() ? "1,00€" : null)
                .pricePerQuantity("1,00€ /un")
                .productUrl(String.format("%s/%s", optionalCatalog.map(CatalogDao::getBaseUrl), query))
                .imageUrl("https://via.placeholder.com/150")
                .date(DateTimeUtils.getCurrentDateTime())
                .build();
    }
}
