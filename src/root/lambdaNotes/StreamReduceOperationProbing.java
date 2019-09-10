package root.lambdaNotes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Code excerpt to probe/ inspect internal working of
 * {@code Stream.collect(source,
 * accumulator, combiner)}
 *
 */
public class StreamReduceOperationProbing {

	static int count = 0;

	public static void main(String[] args) {
		Supplier<String> currThreadInfo = () -> "[ ThreadId: " + Thread.currentThread().getId() + ", ThreadName: "
				+ Thread.currentThread().getName() + ", GroupName: " + Thread.currentThread().getThreadGroup().getName()
				+ " ] ";

		Supplier<List<String>> supplier = () ->
		{
			System.out.println(currThreadInfo.get() + "Supplier.get()");

			return new ArrayList<String>() {

				private static final long serialVersionUID = 1L;
				int objectId = count++;

				@Override
				public boolean add(String e) {
					System.out.println(currThreadInfo.get() + "ArrayListObject" + objectId + ".add(" + e + ")");
					return super.add(e);
				}

				@Override
				public boolean addAll(Collection e) {
					System.out.println(currThreadInfo.get() + "ArrayListObject" + objectId + ".addAll(" + e + ")");
					return super.addAll(e);
				}

				@Override
				public String toString() {
					return "ArrayListObject" + objectId + super.toString();
				}
			};
		};

		BiConsumer<List<String>, String> accumulator = (r, e) ->
		{
			System.out.println(currThreadInfo.get() + "accumulator.accept(" + r + ", " + e + ")");
			r.add(e);
		};

		BiConsumer<List<String>, List<String>> combiner = (r, r1) ->
		{
			System.out.println(currThreadInfo.get() + "combiner.accept(" + r + ", " + r1 + ")");
			r.addAll(r1);
		};

		System.out.println("<<===========================Sequential Reduce Operation==============================>>");

		/**@formatter:off 
		 *  stream.collect(supplier, accumulator, combiner) produces a result equivalent to:
		 *  
		   	R result = supplier.get();
     		for (T element : this stream)
         		accumulator.accept(result, element);
     		return result;
     		
     		- The supplier function to construct new instances of the result container, it is both an initial seed value for the reduction and a default result if there are no input elements
     		- The accumulator function takes a partial result and the next element, and produces a new partial result, incorporate an input element into a result container
     		- The combiner function combines two partial results to produce a new partial result
     		- The combiner is necessary in parallel reductions, where the input is partitioned, a partial accumulation computed for each partition, 
     		   and then the partial results are combined to produce a final result
     		- For sequential stream combiner is not used
     		@formatter:on
		 */

		System.out.println("Sequential Collect: "
				+ Stream.<String>of("a", "b", "c", "d", "e").sequential().collect(supplier, accumulator, combiner));

		/* @formatter:off
		 * <<===========================Sequential Reduce Operation==============================>>
			[ ThreadId: 1, ThreadName: main, GroupName: main ] Supplier.get()
			[ ThreadId: 1, ThreadName: main, GroupName: main ] accumulator.accept(ArrayListObject0[], a)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] ArrayListObject0.add(a)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] accumulator.accept(ArrayListObject0[a], b)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] ArrayListObject0.add(b)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] accumulator.accept(ArrayListObject0[a, b], c)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] ArrayListObject0.add(c)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] accumulator.accept(ArrayListObject0[a, b, c], d)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] ArrayListObject0.add(d)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] accumulator.accept(ArrayListObject0[a, b, c, d], e)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] ArrayListObject0.add(e)
			Sequential Collect: ArrayListObject0[a, b, c, d, e]
			@formatter:on
		 */

		System.out.println("<<===========================Parallel Reduce Operation================================>>");

		System.out.println("Parallel Collect: "
				+ Stream.<String>of("a", "b", "c", "d", "e").parallel().collect(supplier, accumulator, combiner));

		/*  @formatter:off
		 *	<<===========================Parallel Reduce Operation================================>>
			[ ThreadId: 1, ThreadName: main, GroupName: main ] Supplier.get()
			[ ThreadId: 1, ThreadName: main, GroupName: main ] accumulator.accept(ArrayListObject4[], c)
			[ ThreadId: 1, ThreadName: main, GroupName: main ] ArrayListObject4.add(c)
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] Supplier.get()
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] accumulator.accept(ArrayListObject5[], e)
			[ ThreadId: 12, ThreadName: ForkJoinPool.commonPool-worker-3, GroupName: main ] Supplier.get()
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] ArrayListObject5.add(e)
			[ ThreadId: 13, ThreadName: ForkJoinPool.commonPool-worker-5, GroupName: main ] Supplier.get()
			[ ThreadId: 13, ThreadName: ForkJoinPool.commonPool-worker-5, GroupName: main ] accumulator.accept(ArrayListObject7[], a)
			[ ThreadId: 13, ThreadName: ForkJoinPool.commonPool-worker-5, GroupName: main ] ArrayListObject7.add(a)
			[ ThreadId: 12, ThreadName: ForkJoinPool.commonPool-worker-3, GroupName: main ] accumulator.accept(ArrayListObject6[], b)
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] Supplier.get()
			[ ThreadId: 12, ThreadName: ForkJoinPool.commonPool-worker-3, GroupName: main ] ArrayListObject6.add(b)
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] accumulator.accept(ArrayListObject8[], d)
			[ ThreadId: 12, ThreadName: ForkJoinPool.commonPool-worker-3, GroupName: main ] combiner.accept(ArrayListObject7[a], ArrayListObject6[b])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] ArrayListObject8.add(d)
			[ ThreadId: 12, ThreadName: ForkJoinPool.commonPool-worker-3, GroupName: main ] ArrayListObject7.addAll(ArrayListObject6[b])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] combiner.accept(ArrayListObject8[d], ArrayListObject5[e])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] ArrayListObject8.addAll(ArrayListObject5[e])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] combiner.accept(ArrayListObject4[c], ArrayListObject8[d, e])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] ArrayListObject4.addAll(ArrayListObject8[d, e])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] combiner.accept(ArrayListObject7[a, b], ArrayListObject4[c, d, e])
			[ ThreadId: 14, ThreadName: ForkJoinPool.commonPool-worker-7, GroupName: main ] ArrayListObject7.addAll(ArrayListObject4[c, d, e])
			Parallel Collect: ArrayListObject7[a, b, c, d, e]
 			@formatter:on
		 */
	}

}
