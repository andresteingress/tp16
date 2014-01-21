;; {BEGIN: step0}

;; step 0 - intro


       111111111
      111111111111
    1111111111111111
   1    1111111111111
                11111
    111    1111  11111
  1111  1  11111  1111
  111  111  11111 11111
 1111 1111  11111  1111
 1111 11111 111111 1111
 1111 1111   11111 1111
 1111 1111   11111 1111
 1111 111  1 1111  1111
 1111  11 11  111  1111
 11111    11  11  1111
  11111   111    11
  111111
   1111111    1111111
    1111111111111111
     11111111111111
       1111111111
          111

   http://clojure.org

;; {END}

;; {BEGIN: step1}
;; step 1 - say hello to all folks at technologie-plauscherl
println "hello, folks at technologie-plauscherl!"
;; {END}

;; {BEGIN: step2}
;; step 2 - say hello take 2

;; so-called symbol-expressions (s-expressions) are used to execute functions
;; everything in Clojure is formulated via S-expressions

;; paranthesis are used to denote a list - LIstProcessing
;; LISP is "homoiconc": lists repesenting actual code can be manipulated like lists in a collection sense
(println "hello, folks at technologie-plauscherl")
;; {END}

;; {BEGIN: step3}
;; step 3 - applying/calling functions
(+ 1 1)
(* 2 (+ 1 1))

;; look how lists can be used to represent trees
;;
;; comparison to Groovy AST transformtions, ASM, Java agents
;; {END}

;; {BEGIN: step4}
;; step 4 - Clojure data types

(println (class string)) ; look, our old friend "java.lang.String"
(println (class (char 97)))
(println (class 1))
(println (class 1N))
(println (class 1M))
(println (class 1.0))
(println (class (* 2 1/3)))

(println (class :plauscherl))

; all of the collections are immutable and persistent (structural sharing, inherently thread-safe)
(println (class []))
(println (class {}))
(println (class #{}))

; sequences in action
(println (class (seq [1 2 3])))

;; Clojure internally uses some interfaces and a little class hierarchy
(ancestors (class []))
;; {END}

;; {BEGIN: step5}
;; step 5 - list: a special data type in Clojure

;; every line of code is represented in a list
(+ 1 2)
(filter pos? [-1 0 1 2 3])

(println (class (+ 1 2)))
(println (class '(+)))

;; every list represents an S-expression
(browse-url "http://en.wikipedia.org/wiki/S-expression")

;; code is data - lists can be both: data or code and code to generate code
(println '(println "hello, folks at technologie-plauscherl"))

;; the println statement above could be reformulated in code like this
(eval (list (symbol "println") "test"))

;; {END}

;; {BEGIN: step6}
;; step 6 - special forms

;; the order of values in the list is pre-defined, needs to be correctly interpreted by the runtime
(browse-url "http://clojure.org/special_forms")

(def x 42) ;; def is a special form which creates a bound or unbound VAR
(def x)

(let [x 41] (println (inc x)))

(class (var x))
(class #'x)

(class (symbol "x"))

;; defn also defines a var which is a symol attached to a function

;; variable visibility is a key difference between all the Lisps, Clojure is a Lisp-1 (can't be a function and a var
;; with the same name)
(browse-url "http://clojure.org/lisps")
;; {END}

;; {BEGIN: step7}
;; step 7 - available functions

;; 1) JDK classes and all Java libraries, frameworks ...

;; JDK classes via Java interoperability layer
(import 'java.util.ArrayList)

(-> (new ArrayList)
    (.size))

;; implementing against a Java interface
(def p (proxy
           [java.lang.Runnable]
           []
         (run [] (println "hello, world!"))))

(.run p)

;; 2) Clojure-specific packages

;; various packages: clojure.core, clojure.java, clojure.data, clojure.string, clojure.test, ...
(browse-url "http://clojuredocs.org")
(browse-url "http://clojuredocs.org/clojure_core")

;; or use helper functions
(clojure.repl/find-doc "reduce")
(clojure.repl/doc map)
;; {END}


;; {BEGIN: step8}
;; step 8 - defining functions

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

;; there is another powerful mechanism besides function definitions at runtime ...

;; {END}

;; {BEGIN: step9}
1;; step 9 - macros: usage and expansion

;; MACROS can be used to generate code at compile-time
(macroexpand '(println "test"))
(macroexpand '(defn my-print [arg1 arg2] (println arg1 arg2)))
(macroexpand '(def x 42))


;; {END}

;; {BEGIN: step10}
;; step 10 - it's all about data. Immutable data.

;; sequences
(def s (seq [1 2 3 4 5]))
(first s)
(rest s)


(cons 0 s)

;; sequences are especially interesting when being generated lazily
(take 5 (repeat "x"))

;; positive number generator
(defn positive-numbers
    "Generate positive numbers for a better world."
    ([] (positive-numbers 1))
    ([n] (cons n (lazy-seq (positive-numbers (inc n))))))

(take 42 (positive-numbers))

;; fibonacci generator
(defn fib [a b]
  (cons a (lazy-seq (fib b (+ b a)))))

;; maps as data containers - maps use "structural sharing"
(def andre {:name "André" :age "31" :city "Linz"})
(def tom {:name "Tom" :age "35" :city "Linz"})

(clojure.pprint/print-table [andre tom])

;; every key is a function itself
(println (:name andre))
(println (andre :name))

;; records as map alternatives
(defrecord Podcaster [name age city])

(def andre (Podcaster. "André" 31 "Linz"))
(def tom   (Podcaster. "Tom" 35 "Linz"))

(clojure.pprint/print-table [andre tom])

(defprotocol Podcast
  "A simple protocol for podcasters"
  (talk [this x] "Let the podcaster do the talking."))

(defrecord Podcaster [name age city]
  Podcast
  (talk [this x] (str (:name this) " says: " x))
)

(.talk andre "hello world!")

;; vectors as data containers
(def dtr-podcast-team [andre tom])
(clojure.pprint/pprint dtr-podcast-team)

;; sets as data containers
(def dtr-topics #{"evernote" "breitbandinternet" "groovy"})

;; collections are immutable
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
;; {END}

;; {BEGIN: step11}
;; step 11 - concurrency tools

;; toolset: vars, refs, atoms, agents (and clojure.core.async)

;; future - execute the code in the background and make it available some time ...
(def result (future
              (take 50 (fib 1M 1M))))

(println (deref result))

;; atoms - managing shared, synchronous, independent state
(def duff-man (atom "auf zum atem!"))

;; creating thousand computations to compute the first 1.000 fibonacci numbers
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
;; {END}

;; {BEGIN: step12}
;; step 12 - clojure.core.async

;; upcoming in Clojure 1.6: clojure.core.async
;;
;; "go" and language-immanent channel stolen from Google's Go programming language
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
;; {END}

;; {BEGIN: step13}
;; step 13 - parallel reducers in Clojure 1.5

;; Reducers provide parallel reduce and combine via Java's fork/join.

(require '[clojure.core.reducers :as r])

(defn my-inc [x] (do
                   (println (inc x))
                   (inc x)))

(r/fold + (r/map inc [1 2 3 4]))
;; {END}
