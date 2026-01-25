package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.mapper;

import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document.BranchDocument;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document.FranchiseDocument;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document.ProductDocument;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FranchiseMongoMapper {

    /* =========================
       Document -> Domain
       ========================= */

    public Franchise toDomain(FranchiseDocument document) {
        if (document == null) return null;

        Franchise franchise = new Franchise();
        franchise.setId(document.getId());
        franchise.setName(document.getName());
        franchise.setBranches(
                mapBranchesToDomain(document.getBranches())
        );
        return franchise;
    }

    private List<Branch> mapBranchesToDomain(List<BranchDocument> documents) {
        if (documents == null) return List.of();

        return documents.stream()
                .map(this::toDomainBranch)
                .toList();
    }

    private Branch toDomainBranch(BranchDocument document) {
        Branch branch = new Branch();
        branch.setId(document.getId());
        branch.setName(document.getName());
        branch.setProducts(
                mapProductsToDomain(document.getProducts())
        );
        return branch;
    }

    private List<Product> mapProductsToDomain(List<ProductDocument> documents) {
        if (documents == null) return List.of();

        return documents.stream()
                .map(this::toDomainProduct)
                .toList();
    }

    private Product toDomainProduct(ProductDocument document) {
        Product product = new Product();
        product.setId(document.getId());
        product.setName(document.getName());
        product.setStock(document.getStock());
        return product;
    }

    /* =========================
       Domain -> Document
       ========================= */

    public FranchiseDocument toDocument(Franchise domain) {
        if (domain == null) return null;

        FranchiseDocument document = new FranchiseDocument();
        document.setId(domain.getId());
        document.setName(domain.getName());
        document.setBranches(
                mapBranchesToDocument(domain.getBranches())
        );
        return document;
    }

    private List<BranchDocument> mapBranchesToDocument(List<Branch> domains) {
        if (domains == null) return List.of();

        return domains.stream()
                .map(this::toDocumentBranch)
                .toList();
    }

    private BranchDocument toDocumentBranch(Branch domain) {
        BranchDocument document = new BranchDocument();
        document.setId(domain.getId());
        document.setName(domain.getName());
        document.setProducts(
                mapProductsToDocument(domain.getProducts())
        );
        return document;
    }

    private List<ProductDocument> mapProductsToDocument(List<Product> domains) {
        if (domains == null) return List.of();

        return domains.stream()
                .map(this::toDocumentProduct)
                .toList();
    }

    private ProductDocument toDocumentProduct(Product domain) {
        ProductDocument document = new ProductDocument();
        document.setId(domain.getId());
        document.setName(domain.getName());
        document.setStock(domain.getStock());
        return document;
    }
}
