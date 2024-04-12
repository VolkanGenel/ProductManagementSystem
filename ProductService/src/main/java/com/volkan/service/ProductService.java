package com.volkan.service;

import com.volkan.dto.request.BaseRequestDto;
import com.volkan.dto.request.FindProductRequestDto;
import com.volkan.dto.request.SaveProductRequestDto;
import com.volkan.dto.request.UpdateProductRequestDto;
import com.volkan.dto.response.UpdateProductResponseDto;
import com.volkan.exception.EErrorType;
import com.volkan.exception.ProductServiceException;
import com.volkan.mapper.IProductMapper;
import com.volkan.repository.IProductRepository;
import com.volkan.repository.entity.Product;
import com.volkan.utility.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProductService extends ServiceManager<Product,String> {
    private final IProductRepository repository;

    public ProductService(IProductRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Boolean saveDto(SaveProductRequestDto dto) {
        save(IProductMapper.INSTANCE.toProduct(dto));
        return true;
    }

    public UpdateProductResponseDto updateProduct(UpdateProductRequestDto dto) {
        Optional<Product> product = repository.findById(dto.getId());
        if (product.isEmpty()) {
            throw new ProductServiceException(EErrorType.PRODUCT_NOT_FOUND);
        }
        if(dto.getDescription().isBlank())
            throw new ProductServiceException(EErrorType.INVALID_PARAMETER, "Product description must be provided");
        if(dto.getName()==null)
            throw new ProductServiceException(EErrorType.INVALID_PARAMETER,"Product name must be provided");

        Product toUpdateProduct = product.get();

        toUpdateProduct.setDescription(dto.getDescription());
        toUpdateProduct.setName(dto.getName());
        toUpdateProduct.setPrice(dto.getPrice());

        return IProductMapper.INSTANCE.toUpdateProductResponseDto(update(toUpdateProduct));
    }

    public String deleteProduct (String id) {
        Optional<Product> product = findById(id);
        if (product.isEmpty()) {
            throw new ProductServiceException(EErrorType.PRODUCT_NOT_FOUND);
        }
        delete(product.get());
        return "The product has been deleted successfully";
    }

    public Page<Product> findAllPagination(BaseRequestDto dto) {

        Pageable pageable;
        Sort sort = null;

        if(dto.getSortParameter()!=null) {
            String direction = dto.getDirection()==null ? "ASC" : dto.getDirection();
            sort = Sort.by(Sort.Direction.fromString(direction), dto.getSortParameter());
        }

        Integer pageSize = dto.getPageSize() == null ? 5 :
                dto.getPageSize() < 1 ? 5 : dto.getPageSize();
        if(sort!=null && dto.getCurrentPage()!=null) {
            pageable = PageRequest.of(dto.getCurrentPage(), pageSize, sort);
        } else if (sort==null && dto.getCurrentPage()!=null) {
            pageable = PageRequest.of(dto.getCurrentPage(), pageSize);
        } else {
            pageable = PageRequest.of(0,pageSize);
        }
        Page<Product> pageProduct = repository.findAll(pageable);
        return pageProduct;
    }
    public Page<Product> findAllPages(Integer numberOfProducts,Integer currentPage,String sortParameter, String direction2) {

        Pageable pageable;
        Sort sort = null;

        if(sortParameter!=null) {
            String direction = direction2==null ? "ASC" : direction2;
            sort = Sort.by(Sort.Direction.fromString(direction), sortParameter);
        }

        Integer pageSize = numberOfProducts == null ? 5 :
                numberOfProducts < 1 ? 5 : numberOfProducts;
        if(sort!=null && currentPage!=null) {
            pageable = PageRequest.of(currentPage, pageSize, sort);
        } else if (sort==null && currentPage!=null) {
            pageable = PageRequest.of(currentPage, pageSize);
        } else {
            pageable = PageRequest.of(0,pageSize);
        }
        Page<Product> pageProduct = repository.findAll(pageable);
        return pageProduct;
    }

    public List<Product> findProduct(FindProductRequestDto dto){
        return   repository.findByIdContainingIgnoreCaseAndNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(dto.getId(), dto.getName(), dto.getDescription());
    }
    public List<Product> findProductByName(String name) {
        return repository.findProductByName(name);
    }

//    public List<Product> findByName(String name) {
//        List<Product> productList = findAll().stream().filter(x-> x.getName().toUpperCase(Locale.ENGLISH).contains(name.toLowerCase().toUpperCase(Locale.ENGLISH))).toList();
//        return productList;
//    }

//    public List<Product> findProductByName(String name) {
//        return repository.findByNameContainingIgnoreCase(name);
//    }
}
