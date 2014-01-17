(require '[clojure.java.browse :only browse-url])

;; step 0 - it's all about data.


;; step 1 - say hello to all folks at technologie-plauscherl
println "hello, folks at technologie-plauscherl!"


;; step 2 - do it the clojure way

;; function is execute with using a prefix notation
(println "hello, folks at technologie-plauscherl")
(+ 1 1)

;; step 3 - assign it to a variable
(def string "hello, folks at technologie-plauscherl")
(println string)

(println (class string)) ;; look, our old friend "java.lang.String"
(println (class (char 97)))
(println (class 1))
(println (class 1N))
(println (class 1M))
(println (class 1.0))
(println (class (* 2 1/3)))
(println (class :plauscherl))

;; all of the collections are IMMUTABLE and PERSISTENT (structural sharing, enheriently thread-safe)
(println (class []))
(println (class {}))
(println (class #{}))

;; sequences in action
(println (class (seq [1 2 3])))

;; and there is more: list is a special collection ...
(println (class ()))
(println (class '()))

;; list is used everywhere in the source code!
(println '(println "hello, folks at technologie-plauscherl"))
(println `(println "hello, folks at technologie-plauscherl"))

;; the println statement above could be reformulated in code like this
(list ((symbol "println") "hello, folks at technologie-plauscherl"))

;; MACROS an be used to generate code at compile-time by the REPL or the Clojure reader



;; we already used lists to make actual function calls
;; what functions are available?

;; various packages: clojure.core, clojure.java, clojure.data, clojure.string, clojure.test, ...
(browse-url "http://clojuredocs.org")
(browse-url "http://clojuredocs.org/clojure_core")

;; will see more functions in the next 10 minutes ...

;; having a closer look at functions - how is a function DEFINED?

(...)
(fn ...)
(fn [arg1 arg2] ...)
(fn [arg1 arg2] (println arg1 arg2))

;; we have seen 'def' in the variable declaration
(def my-print (fn [arg1 arg2] (println arg1 arg2)))

(defn my-print [arg1 arg2]
  (println arg1 arg2))

;; PLUS: every function is defined within a "namespace" -> "clojure.core", "clojure.string" etc.
(println `(my-print "test"))
(println (class 'my-print))

;; MACROS can be used to generate code
(macroexpand '(println "test"))
(macroexpand '(defn my-print [arg1 arg2] (println arg1 arg2)))
(macroexpand '(def x 42))

;; def is a special thing, it's a form
;; a convertion over the order and type of symbols that is used within the list
;; other examples:

(let [x 42] ;; [x 42] is another special form: a binding. it binds a symbol to a value
  (inc x))


;; step 4 - we want to replace "plauscherl" with "meeting". requires functions from the clojure.string namespace
(require '[clojure.string :as str :only str/replace])
(str/replace string "plauscherl" "meeting")

(use '[clojure.string])
(replace msg "plauscherl" "meeting")

;; It's all about data.

;; step 5 - remember the collection classes?

;; def map
(def andre {:name "André" :age "31" :city "Linz"})
(def tom {:name "Tom" :age "35" :city "Linz"})

;; every key is a function itself
(println (:name andre))
(println (andre :name))

;; def vector
(def dtr-podcast-team [andre tom])
(clojure.pprint/pprint dtr-podcast-team)

;; def set
(def dtr-topics #{"evernote" "breitbandinternet" "groovy"})

(conj dtr-topics "code löschen")
(cons "code löschen" dtr-topics)

;; it's an immutable and persistent data-structure
(println dtr-topics)

;; not stuck with sets, the same goes for maps!
(assoc andre :married true)
(println andre)

;; why is immutability and persistency necessary (or should be)?

;; rich hickey's JVM language summit talk 2009: are we there yet?
(browse-url "http://www.infoq.com/presentations/Are-We-There-Yet-Rich-Hickey")

;; "The future is a function of the past, and doesn’t change it. (Stu Halloway)"


;; MGMT SUMMARY: damn statful stuff, espescially in a concurrent environment.

;; Clojure: everything is thread-safe

;; check this out: lazy sequences can be used to create generators
(take 5 (repeat "x"))

(defn positive-numbers
    "Generate positive numbers for a better world."
    ([] (positive-numbers 1))
    ([n] (cons n (lazy-seq (positive-numbers (inc n))))))


(defn fib [a b]
  (cons a (lazy-seq (fib b (+ b a)))))


;; future - execute the code in the background and make it available some time ...
(def result (future
              (take 50 (fib 1M 1M))))


;; atoms - managing shared, synchronous, independent state
(def duff-man (atom "auf zum atem!"))

;; creating 1.000 thousand computations to compute the first 1.000 fibonacci numbers
(def counter (atom 0))
(loop [x 0]
  (if (< x 1000)
    (do
      (future
        ((take 1000 (fib 0M 1M))
         swap! counter inc))
      (recur (inc x)))))

(deref counter)

;; software transactional memory with refs - STM (there is a Java STM lib around, part of the GPars dist)
;; coordinated, synchronous change of multiple locations
(def cars (ref []))
(dotimes [n 1000]
  (future
    (dosync
     (comment (if (= n 500) (assert (= 1 2))))
     (alter cars conj {:name (str "car no. " n)}))))

(clojure.pprint/pprint cars)
(count @cars)

;; agents - independent, asynchronous change of individual locations
(def cars (agent [])) ;; agents can be used with any data-structure
(send cars conj {:name "ferrari"})
(deref cars)

;; In upcoming Clojure 1.6: clojure.core.async
;;
;; "go" and language-immanent channel stolen from the Go programming language
(remove-ns 'clojure.string)
(use '[clojure.core.async] :reload-all)

;; create a buffered channel holding 10 elements max.
(def c (chan 10))
(>!! c 1)
(<!! c)

;; synchronus example - the main thread is blocked
(let [c (chan)]
  (thread (>!! c "hello"))
  (assert (= "hello" (<!! c)))
  (close! c))

(let [c (chan)]
  (go (>! c "hello"))
  (assert (= "hello" (<!! (go (<! c)))))
  (close! c))

;; clojure.core.reducers ebenfalls besprechen
;; auch macros nicht vergessen, das ist der noch fehlende teil

;; another example for built-in concurrency features: 1.5.0: reducers

;; Reducers provide parallel reduce and combine via Java's fork/join.

(require '[clojure.core.reducers :as r])

(defn my-inc [x] (do
                   (println (inc x))
                   (inc x)))

(r/fold + (r/map inc [1 2 3 4]))
