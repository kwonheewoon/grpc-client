package org.khw.grpcclient.brand

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/brands")
@RequiredArgsConstructor
class BrandRestController(
    val brandService: BrandService
) {

    @GetMapping
    fun findBrandByName(@RequestParam name: String) {
        brandService.findBrandByName(name)
    }
}