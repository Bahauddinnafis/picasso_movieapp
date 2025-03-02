package com.nafis.picassomovieapp.common.data

interface ApiMapper<Domain, Entity> {

    fun mapToDomain(apiDto: Entity): Domain

}