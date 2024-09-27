package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class CurrencyComponent : Component {
    var money = 0
    var totalMoney = 0

    companion object {
        val mapper = mapperFor<CurrencyComponent>()
    }
}
