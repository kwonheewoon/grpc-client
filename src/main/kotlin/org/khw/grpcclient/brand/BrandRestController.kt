package org.khw.grpcclient.brand

import lombok.RequiredArgsConstructor
import org.khw.brand.test.BrandRes
import org.khw.grpcclient.brand.dto.BrandResDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping("/client-stream")
    fun findBrandByNames(@RequestBody brandReqeust: BrandReqeust) {
        brandService.findBrandsByNames2(brandReqeust)
    }

    @GetMapping("/{foundedYear}")
    fun findBrandsByFoundedYear(@PathVariable foundedYear: Int): List<BrandResDto> {
        return brandService.findBrandsByFoundedYear(foundedYear)
    }

    @GetMapping("/server-stream")
    fun findBrandsByNames(@RequestParam names: List<String>) {
        brandService.findBrandsByNames(names)
    }
}