package com.nequi.api_franquicias.infrastructure.entrypoint.mapper;

import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.CreateProductRequest;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.BranchResponse;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.FranchiseResponse;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.ProductMaxStockResponse;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.ProductResponse;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

import java.util.List;

@Component
public class FranchiseMapper {
    public Product toProduct(CreateProductRequest request){
        return new Product("", request.name(), request.stock());
    }

    public ProductMaxStockResponse toResponse(Tuple2<Branch, Product> tuple){
        Branch branch = tuple.getT1();
        Product product = tuple.getT2();

        return new ProductMaxStockResponse(
                branch.getId(),
                branch.getName(),
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }

    public FranchiseResponse toFranchiseResponse(Franchise franchise){
        return new FranchiseResponse(franchise.getId(), franchise.getName(), toBranchResponse(franchise.getBranches()));
    }

    private List<BranchResponse> toBranchResponse(List<Branch> branches){
        return branches.stream()
                .map(branch -> new BranchResponse(branch.getId(), branch.getName(), toProductResponse(branch.getProducts())))
                .toList();
    }

    private List<ProductResponse> toProductResponse(List<Product> products){
        return products
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getStock()))
                .toList();
    }
}
