package com.example.domain.utils

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Dispatcher(val dispatcher: Dispatchers = Dispatchers.DEFAULT)
