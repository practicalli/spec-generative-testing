(ns practicalli.spec-generative-testing
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]
   [clojure.spec.test.alpha :as spec-test]))

;; Set up project
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; clj-new now adds org.clojure/test.check as a dependency under the :test alias

;; add a dir-locals.el file and add the :test alias when running the repl from Emacs
;; ((clojure-mode . ((cider-clojure-cli-global-options . "-A:test"))))



;; Generators
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(spec-gen/generate (spec/gen int?))

(spec-gen/generate (spec/gen nil?))

(spec-gen/sample (spec/gen string?))

(spec-gen/sample (spec/gen #{:club :diamond :heart :spade}))

(spec-gen/sample (spec/gen (spec/cat :k keyword? :ns (spec/+ number?))))




;; Player specification
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def suit? #{:clubs :diamonds :hearts :spades})
(def rank? (into #{:jack :queen :king :ace} (range 2 11)))
(spec/def ::playing-card (spec/tuple rank? suit?))
(spec/def ::delt-hand (spec/* ::playing-card))

(spec/def ::name string?)
(spec/def ::score int?)
(spec/def ::player (spec/keys :req [::name ::score ::delt-hand]))


(spec/def ::deck (spec/* ::playing-card))
(spec/def ::players (spec/* ::player))
(spec/def ::game (spec/keys :req [::players ::deck]))


;; generating a random player in our card game?

(spec-gen/generate (spec/gen ::player))
;; => #:practicalli.spec-generative-testing{:name "Yp34KE63vALOeriKN4cBt", :score 225, :delt-hand ([9 :hearts] [4 :clubs] [8 :hearts] [10 :clubs] [:queen :spades] [3 :clubs] [6 :hearts] [8 :hearts] [7 :diamonds] [:king :spades] [:ace :diamonds] [2 :hearts] [4 :spades] [2 :clubs] [6 :clubs] [8 :diamonds] [6 :spades] [5 :spades] [:queen :clubs] [:queen :hearts] [6 :spades])}



(spec-gen/generate (spec/gen ::game))



;; Specification for function definition
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn regulation-card-deck
  [{:keys [::deck ::players] :as game}]
  (apply + (count deck)
         (map #(-> % ::delt-hand count) players)))

(defn deal-cards
  [game]
  game)

(spec/fdef deal-cards
  :args (spec/cat :game ::game)
  :ret ::game
  :fn #(= (regulation-card-deck (-> % :args :game))
          (regulation-card-deck (-> % :ret))))



;; Testing with spec
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Generate 1000 values to run against the specification
;; takes over a minute - are all generative tests this slow?
;; if so, then generative tests should be run less often

;; spec/check takes a fully-qualified symbol so we use ` here to resolve it in the context of the current namespace.


(spec-test/check `deal-cards)

;; => ({:spec #object[clojure.spec.alpha$fspec_impl$reify__2524 0x26debeba "clojure.spec.alpha$fspec_impl$reify__2524@26debeba"],
;; :clojure.spec.test.check/ret
;;  {:result true, :pass? true, :num-tests 1000, :time-elapsed-ms 75054, :seed 1591928968683},
;; :sym practicalli.spec-generative-testing/deal-cards})




;; How to run a specific number of tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; clojure.spec.test.alpha/check API reference describes a second argument to the function,
;; a hash-map that includes options, one of which is :num-tests
;; The key for the options is a fully qualified namespace, specifically
;; :clojure.spec.test.check/opts
;; https://clojure.github.io/spec.alpha/clojure.spec.test.alpha-api.html#clojure.spec.test.alpha/check

;; Run a single check - very quick

(spec-test/check
  `deal-cards
  {:clojure.spec.test.check/opts {:num-tests 1}})

;; => ({:spec #object[clojure.spec.alpha$fspec_impl$reify__2524 0x26debeba "clojure.spec.alpha$fspec_impl$reify__2524@26debeba"],
;; :clojure.spec.test.check/ret
;; {:result true, :pass? true, :num-tests 1, :time-elapsed-ms 0, :seed 1591961775784},
;; :sym practicalli.spec-generative-testing/deal-cards})

;; Run 10 checks - very quick

(spec-test/check
  `deal-cards
  {:clojure.spec.test.check/opts {:num-tests 10}})

;; => ({:spec #object[clojure.spec.alpha$fspec_impl$reify__2524 0x26debeba "clojure.spec.alpha$fspec_impl$reify__2524@26debeba"],
;; :clojure.spec.test.check/ret {:result true, :pass? true, :num-tests 10, :time-elapsed-ms 6, :seed 1591961778220},
;; :sym practicalli.spec-generative-testing/deal-cards})


;; Run 100 checks - takes about 3 seconds

(spec-test/check
  `deal-cards
  {:clojure.spec.test.check/opts {:num-tests 101}})

;; => ({:spec #object[clojure.spec.alpha$fspec_impl$reify__2524 0x26debeba "clojure.spec.alpha$fspec_impl$reify__2524@26debeba"],
;; :clojure.spec.test.check/ret
;; {:result true, :pass? true, :num-tests 101, :time-elapsed-ms 2148, :seed 1591961780863},
;; :sym practicalli.spec-generative-testing/deal-cards})



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Using an alias
;; The API documentation talks about using ::stc/opts
;; Suggestion from Sean Corfield on how to create such an alias
;; as the namespace does not exist
;; (alias 'stc (create-ns 'clojure.spec.test.check))

;; ::stc/opts

;; (spec-test/check `deal-cards
;;                  {::stc/opts {:num-tests 1}})

;; Code seems much cleaner if :clojure.spec.test.check/opts is used as the key name

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;; Test reports
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec-test/summarize-results
  (spec-test/check `deal-cards
                   {:clojure.spec.test.check/opts {:num-tests 10}}))




;; Adding more functions and specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Define a function to calculate the winning hand of a game

(defn winning-player
  [players]
  ;; calculate winning player from each of players hands
  ;; return player
  )


;; Initially we can return an explicit player data structure
(defn winning-player
  [players]
  ;; calculate winning player from each of players hands
  ;; return player
  #:practicalli.player-won
  {:name      "Jenny Nada",
   :score     225,
   :delt-hand [[9 :hearts] [4 :clubs] [8 :hearts] [10 :clubs] [:queen :spades]]})


;; rather than explicitly add the data, we can generate the data
;; until we write the real algorithm


(spec-gen/generate (spec/gen ::player))


;; Then
(defn winning-player
  [players]
  ;; calculate winning player from each of players hands
  ;; return player

  (spec-gen/generate (spec/gen ::player)))

;; This is just a temporary place holder until the algorithm of the function
;; is created
