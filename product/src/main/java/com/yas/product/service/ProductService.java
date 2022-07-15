package com.yas.product.service;

import com.yas.product.model.Product;
import com.yas.product.repository.ProductRepository;
import com.yas.product.viewmodel.NoFileMediaVm;
import com.yas.product.viewmodel.ProductGetDetailVm;
import com.yas.product.viewmodel.ProductPostVm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MediaService mediaService;

    public ProductService(ProductRepository productRepository, MediaService mediaService) {
        this.productRepository = productRepository;
        this.mediaService = mediaService;
    }

    public ProductGetDetailVm createProduct(ProductPostVm productPostVm){
        Product product = new Product();
        product.setName(productPostVm.name());
        product.setSlug(productPostVm.slug());
        product.setDescription(productPostVm.description());
        product.setShortDescription(productPostVm.shortDescription());
        product.setSpecification(productPostVm.specification());
        product.setSku(productPostVm.sku());
        product.setGtin(productPostVm.gtin());
        product.setMetaKeyword(productPostVm.metaKeyword());
        product.setMetaDescription(productPostVm.metaDescription());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        product.setCreatedBy(auth.getName());
        product.setLastModifiedBy(auth.getName());

        NoFileMediaVm noFileMediaVm = mediaService.SaveFile(productPostVm.thumbnail(), "", "");
        product.setThumbnailMediaId(noFileMediaVm.id());

        productRepository.saveAndFlush(product);
        return ProductGetDetailVm.fromModel(product);
    }
}
