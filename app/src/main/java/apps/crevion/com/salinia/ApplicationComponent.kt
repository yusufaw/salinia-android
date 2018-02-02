package apps.crevion.com.salinia

import apps.crevion.com.salinia.module.AppModule
import apps.crevion.com.salinia.ui.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by yusufaw on 2/2/18.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface ApplicationComponent {
    fun inject(target: MainActivity)
    fun inject(target: HomeActivity)
}