package org.khw.grpcclient.brand


import io.grpc.stub.StreamObserver
import lombok.RequiredArgsConstructor
import net.devh.boot.grpc.client.inject.GrpcClient
import org.khw.brand.test.BrandGrpc.BrandStub
import org.khw.brand.test.BrandReq
import org.khw.brand.test.BrandRes
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class BrandService(
    @GrpcClient("test") val brandStub: BrandStub
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
}