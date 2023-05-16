package walby.eugene.rxjava2_examples

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.TimeUnit

class RxJava2Operators {
    companion object {
        init {
            setupSchedulers()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            ObservableOperators.callOperators()
        }

        private val immediate = object : Scheduler() {
            override fun scheduleDirect(
                run: Runnable,
                delay: Long, unit: TimeUnit
            ): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker { it.run() }
            }
        }

        private fun setupSchedulers() {
            RxJavaPlugins.setInitIoSchedulerHandler { immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
        }
    }
}