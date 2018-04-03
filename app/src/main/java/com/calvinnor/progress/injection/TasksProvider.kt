package com.calvinnor.progress.injection

import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.data_layer.TaskRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object TasksProvider {

    @Provides
    @Singleton
    fun dataProxy(): DataProxy = TaskRepo

}
