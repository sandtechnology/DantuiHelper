package sandtechnology.utils;

public interface IndexIterator {
    int next();

    class RangeIndexIterator implements IndexIterator {
        private int min;
        private int max;
        private int now;
        private boolean first = true;

        private RangeIndexIterator(int min, int max, boolean includeMin, boolean includeMax) {
            this.min = includeMin ? min : min + 1;
            this.max = includeMax ? max : max - 1;
            now = this.min;
        }

        public int next() {
            if (first) {
                first = false;
                return now;
            } else {
                return now = now + 1 > max ? min : now + 1;
            }
        }
    }

    class SingleValueIterator implements IndexIterator {

        int value;

        private SingleValueIterator(int value) {
            super();
            this.value = value;
        }

        @Override
        public int next() {
            return value;
        }
    }

    class IndexIteratorBuilder {
        private int min = 0;
        private int max;
        private boolean includeMin = true;
        private boolean includeMax = true;

        public IndexIteratorBuilder(int max) {
            this.max = max;
        }

        public IndexIteratorBuilder(int min, int max) {
            this.min = min;
            this.max = max;
        }


        public IndexIteratorBuilder excludeMax() {
            this.includeMax = false;
            return this;
        }

        public IndexIteratorBuilder excludeMin() {
            this.includeMin = false;
            return this;
        }

        public IndexIterator build() {
            if (min > max) {
                throw new IllegalArgumentException("the min value larger than the max value!" + toString());
            }
            //为相等做优化
            if (min == max) {
                if (!includeMax || !includeMin) {
                    throw new IllegalArgumentException("None value will be included!" + toString());
                } else {
                    return new SingleValueIterator(min);
                }
            } else if (min + 1 == max) {
                if (!includeMax && !includeMin) {
                    throw new IllegalArgumentException("None value will be included!" + toString());
                }
                if (!includeMin) {
                    return new SingleValueIterator(max);
                }
                if (!includeMax) {
                    return new SingleValueIterator(min);
                }
            }
            return new RangeIndexIterator(min, max, includeMin, includeMax);
        }

        @Override
        public String toString() {
            return "IndexIteratorBuilder{" +
                    "min=" + min +
                    ", max=" + max +
                    ", includeMin=" + includeMin +
                    ", includeMax=" + includeMax +
                    '}';
        }
    }
}
