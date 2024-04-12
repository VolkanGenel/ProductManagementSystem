package com.volkan.controller;

import com.volkan.dto.request.BaseRequestDto;
import com.volkan.dto.request.FindProductRequestDto;
import com.volkan.dto.request.SaveProductRequestDto;
import com.volkan.dto.request.UpdateProductRequestDto;
import com.volkan.dto.response.UpdateProductResponseDto;
import com.volkan.repository.entity.Product;
import com.volkan.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.volkan.constants.EndPoints.*;

@RestController
@RequestMapping(PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping(SAVE)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Boolean> save(@RequestBody SaveProductRequestDto dto) {
        return ResponseEntity.ok(productService.saveDto(dto));
    }

    @PutMapping(UPDATE)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE')")
    public ResponseEntity<UpdateProductResponseDto> update(@RequestBody UpdateProductRequestDto dto){
        return ResponseEntity.ok(productService.updateProduct(dto));
    }
    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE','USER_ROLE')")
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping(FIND_ALL_PAGINATION)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE','USER_ROLE')")
    public ResponseEntity<Page<Product>> findAllPagination(@RequestBody BaseRequestDto dto) {
        return ResponseEntity.ok(productService.findAllPagination(dto));
    }

    @GetMapping(FIND_ALL_PAGES)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE','USER_ROLE')")
    public ResponseEntity<Page<Product>> findAllPages(Integer pageSize,Integer currentPage,String sortParameter, String direction) {
        return ResponseEntity.ok(productService.findAllPages(pageSize,currentPage,sortParameter,direction));
    }
    @PostMapping(FIND_PRODUCT)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE','USER_ROLE')")
    public List<Product> findProduct(FindProductRequestDto dto){
        return   productService.findProduct(dto);
    }
    @GetMapping(FIND_BY_NAME)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE','USER_ROLE')")
    public List<Product> findProductByName(@PathVariable String name) {
        return productService.findProductByName(name);
    }

    @DeleteMapping(DELETE)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE')")
    public ResponseEntity<String> delete(@RequestParam String id){
        return ResponseEntity.ok(productService.deleteProduct(id));

    }
}
