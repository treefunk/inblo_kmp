package com.colinjp.inblo




class ApiSettings(){
    companion object {

        private const val BASE_URL = "https://colinphl.xsrv.jp/inblo-api/api"
        private const val STAGING_BASE_URL = "https://colinphl.xsrv.jp/inblo-api-staging/api"
        private const val LOCAL_HOST = "http://localhost/api"

        val activeUrl = BASE_URL
    }
}
