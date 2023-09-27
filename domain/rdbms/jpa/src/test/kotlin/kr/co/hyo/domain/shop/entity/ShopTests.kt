package kr.co.hyo.domain.shop.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Shop 단위 테스트")
class ShopTests {

    @Test
    fun `매장을 생성한다`() {
        // given
        val shopServiceType = ShopServiceType.convert(value = "SERVICE_B")
        val id = 1L
        val shopId = ShopId(serviceType = shopServiceType, id = id)
        val name = "테스트 매장"

        // when
        val shop = Shop(shopId = shopId, name = name)

        // then
        assertThat(shop.shopId).isEqualTo(shopId)
        assertThat(shop.name).isEqualTo(name)
    }
}
