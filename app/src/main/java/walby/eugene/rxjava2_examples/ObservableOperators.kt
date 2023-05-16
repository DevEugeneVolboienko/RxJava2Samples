package walby.eugene.rxjava2_examples

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.TestScheduler
import java.util.*
import java.util.concurrent.TimeUnit

class ObservableOperators {
    companion object {
        private val intsList = listOf(1, 2, 1, 3)
        private val stringsList = listOf("A", "B", "C")

        fun callOperators() {
            flatMap()
            switchMap()
            concatMap()
            startWith()
            concat()
            zip()
            combineLatest()
            sequenceEqual()
        }

        private fun flatMap() {
            val scheduler = TestScheduler()
            Observable.fromIterable(intsList)
                .flatMap {
                    val delay = Random().nextInt(10).toLong()
                    Observable.just(it)
                        .delay(delay, TimeUnit.MILLISECONDS, scheduler)
                }
                .toList()
                .doOnSuccess { println("flatMap: $it") }
                .subscribe()
            scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
            // prints [1, 2, 3, 1] or another with no specific order
        }

        private fun switchMap() {
            val scheduler = TestScheduler()
            Observable.fromIterable(intsList)
                .switchMap {
                    val delay = Random().nextInt(10).toLong()
                    Observable.just(it)
                        .delay(delay, TimeUnit.MILLISECONDS, scheduler)
                }
                .toList()
                .doOnSuccess { println("switchMap: $it") }
                .subscribe()
            scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
            // prints [3]
        }

        private fun concatMap() {
            val scheduler = TestScheduler()
            Observable.fromIterable(intsList)
                .concatMap {
                    val delay = Random().nextInt(10).toLong()
                    Observable.just(it)
                        .delay(delay, TimeUnit.MILLISECONDS, scheduler)
                }
                .toList()
                .doOnSuccess { println("switchMap: $it") }
                .subscribe()
            scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
            // prints [1, 2, 1, 3] in the same order
        }

        private fun startWith() {
            Observable.fromIterable(intsList)
                .startWith(0)
                .toList()
                .doOnSuccess { println("startWith: $it") }
                .subscribe()
            // prints [0, 1, 2, 1, 3]
        }

        private fun concat() {
            Observable.concat(
                Observable.fromIterable(intsList),
                Observable.fromIterable(stringsList)
            )
                .toList()
                .doOnSuccess { println("concat: $it") }
                .subscribe()
            // prints [1, 2, 1, 3, A, B, C] merge sequence of items from the first observable with sequence of items from the second one
        }

        private fun zip() {
            Observable.zip(
                Observable.fromIterable(intsList),
                Observable.fromIterable(stringsList),
                BiFunction { int: Int, string: String ->
                    return@BiFunction string + int
                })
                .toList()
                .doOnSuccess { println("zip: $it") }
                .subscribe()
            // prints [A1, B2, C1] merge value from first observable with value from the second one items
        }

        private fun combineLatest() {
            Observable.combineLatest(
                Observable.fromIterable(intsList),
                Observable.fromIterable(stringsList),
                BiFunction { int: Int, string: String ->
                    return@BiFunction string + int
                })
                .toList()
                .doOnSuccess { println("combineLatest: $it") }
                .subscribe()
            // prints [A3, B3, C3] merge each of values form second observable with the last from the first
        }

        private fun sequenceEqual() {
            Observable.sequenceEqual(
                Observable.fromIterable(intsList),
                Observable.just(1, 2, 2, 3)
            )
                .doOnSuccess { println("sequenceEqual: $it") }
                .subscribe()
            // prints false because the sequences are different
        }
    }
}