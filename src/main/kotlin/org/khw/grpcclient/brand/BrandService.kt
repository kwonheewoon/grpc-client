package org.khw.grpcclient.brand


import io.grpc.stub.StreamObserver
import lombok.RequiredArgsConstructor
import net.devh.boot.grpc.client.inject.GrpcClient
import org.khw.brand.test.BrandGrpc.BrandBlockingStub
import org.khw.brand.test.BrandGrpc.BrandStub
import org.khw.brand.test.BrandReq
import org.khw.brand.test.BrandRes
import org.khw.grpcclient.brand.dto.BrandResDto
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class BrandService(
    @GrpcClient("test") val brandStub: BrandStub,
    @GrpcClient("test") val brandBlockingStub: BrandBlockingStub
) {

    fun findBrandByName(name: String) {

        // 비동기 호출을 위한 StreamObserver 생성
        val responseObserver = object : StreamObserver<BrandRes> {
            override fun onNext(value: BrandRes) {
                // 서버로부터 응답을 받았을 때 호출됨
                println("Brand: ${value.name}")
            }

            override fun onError(t: Throwable) {
                // 에러 발생 시 호출됨
                println("RPC failed: ${t.message}")
            }

            override fun onCompleted() {
                // 서버로부터 모든 데이터를 받고 호출이 완료되었을 때 호출됨
                println("Finished")
            }
        }

        brandStub.findBrandByName(
            BrandReq.newBuilder().setName(name).build(),
            responseObserver
        )
    }

    fun findBrandsByNames2(brandReqeust: BrandReqeust) {
        val responseObserver = object : StreamObserver<BrandRes> {
            override fun onNext(value: BrandRes) {
                // 서버로부터 응답을 받았을 때 호출됨
                println("Received response: ${value.name}, Explanation: ${value.explanation}")
            }

            override fun onError(t: Throwable) {
                // RPC 도중 오류가 발생했을 때 호출됨
                println("RPC failed: ${t.message}")
            }

            override fun onCompleted() {
                // 모든 데이터를 받고 호출이 완료되었을 때 호출됨
                println("Stream completed.")
            }
        }

        // 클라이언트 스트리밍 RPC 시작
        val requestStreamObserver = brandStub.findBrandByNamesClientStreaming(responseObserver)

        brandReqeust.name.forEach {
            requestStreamObserver.onNext(BrandReq.newBuilder().setName(it).build())
        }

        // 요청의 끝을 표시
        requestStreamObserver.onCompleted()
    }


    fun findBrandsByNames(names: List<String>) {
        // 비동기 호출을 위한 StreamObserver 생성
        val responseObserver = object : StreamObserver<BrandRes> {
            override fun onNext(value: BrandRes) {
                // 서버로부터 응답을 받았을 때 호출됨
                println("Brand: ${value.name}")
            }

            override fun onError(t: Throwable) {
                // 에러 발생 시 호출됨
                println("RPC failed: ${t.message}")
            }

            override fun onCompleted() {
                // 서버로부터 모든 데이터를 받고 호출이 완료되었을 때 호출됨
                println("Finished")
            }
        }

        val requestStreamObserver = brandStub.findBrandByNameClientServerStreaming(responseObserver)

        names.forEach {
            requestStreamObserver.onNext(
                BrandReq.newBuilder().setName(it).build()
            )
        }

        requestStreamObserver.onCompleted()
    }

    fun findBrandsByFoundedYear(year: Int): List<BrandResDto> {
        return brandBlockingStub.findBrandByNameServerStreaming(
            BrandReq.newBuilder().setFoundedYear(year).build()
        ).asSequence()
            .map { BrandResDto(it.name, it.explanation, it.foundedYear, it.displayFlag) }
            .toList()
    }
}