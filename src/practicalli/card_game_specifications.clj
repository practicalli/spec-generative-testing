;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Specifications for a simple card game
;;
;; Author(s): John Stevenson
;;
;; Specifications and custom predicate functions for
;; a simple playing card game
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(ns practicalli.card-game-specifications
  (:require
   [practicalli.card-game :as card-game]
   [clojure.spec.alpha :as spec]
   [clojure.spec.test.alpha :as spec-test]))



;; Card Specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Custom predicate functions to be used as specifications

(def suit? #{:clubs :diamonds :hearts :spades})
(def rank? (into #{:jack :queen :king :ace} (range 2 11)))


(spec/def ::playing-card (spec/tuple rank? suit?))


;; Player specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec/def ::name string?)
(spec/def ::score int?)

(spec/def ::player
  (spec/keys :req [::name ::score ::delt-hand]))


;; Game specifications
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec/def ::deck (spec/* ::playing-card))

(spec/def ::players (spec/* ::player))

(spec/def ::game (spec/keys :req [::players ::deck]))

