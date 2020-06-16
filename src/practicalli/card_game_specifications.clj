(ns practicalli.card-game
  (:require
   [practicalli.card-game-specifications]
   [clojure.spec.alpha :as spec]
   [clojure.spec.gen.alpha :as spec-gen]))



(defn regulation-card-deck
  [{:keys [::deck ::players] :as game}]
  (apply + (count deck)
         (map #(-> % ::delt-hand count) players)))


(defn deal-cards
  [game]
  game)


(defn winning-hand?
  [players]
  ;; calculate winning hand from each of players hands
  ;; return player
  (spec-gen/generate (spec/gen :practicalli.card-game/player))

  )

;; REPL testing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment


  ;; Call wining hand with a generated value from the players specification
  (winning-hand? (spec-gen/generate (spec/gen :practicalli.card-game/players)))

  )
