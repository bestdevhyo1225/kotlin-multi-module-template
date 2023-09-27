package kr.co.hyo.domain.shop.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.shop.entity.Shop
import kr.co.hyo.domain.shop.entity.ShopId
import kr.co.hyo.domain.shop.entity.ShopServiceType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class])
@DisplayName("ShopRepository 단위 테스트")
class ShopRepositoryTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var shopRepository: ShopRepository

    @Test
    fun `매장을 저장한다`() {
        // given
        val shopServiceType = ShopServiceType.convert(value = "SERVICE_B")
        val id = 1L
        val shopId = ShopId(serviceType = shopServiceType, id = id)
        val name = "테스트 매장"
        val shop = Shop(shopId = shopId, name = name)

        // when
        shopRepository.save(shop)
        entityManager.flush()
        entityManager.clear()

        // then
        val findShop: Shop? = shopRepository.findByIdOrNull(id = shopId)

        assertThat(findShop).isNotNull
        assertThat(findShop).isEqualTo(shop)
    }
}
