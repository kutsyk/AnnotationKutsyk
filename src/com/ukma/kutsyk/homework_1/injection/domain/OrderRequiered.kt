package com.ukma.kutsyk.homework_1.injection.domain

import com.ukma.kutsyk.framework.core.Required

class OrderRequiered
{
    private var client: Client? = null
    private var product: Product? = null

    @Required
    fun setProduct(pr: Product) {
        this.product = pr
    }

    @Required
    fun setClient(cl: Client) {
        this.client = cl
    }
}