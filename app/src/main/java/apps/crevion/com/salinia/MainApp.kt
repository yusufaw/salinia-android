package apps.crevion.com.salinia

import android.app.Application
import apps.crevion.com.salinia.module.AppModule

/**
 * Created by yusufaw on 2/2/18.
 */

class MainApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = initApplicationComponent()
    }

    fun initApplicationComponent(): ApplicationComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
}