(ns practicalli.card-game)



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
  #:practicalli.player-won
  {:name      "Jenny Nada",
   :score     225,
   :delt-hand [[9 :hearts] [4 :clubs] [8 :hearts] [10 :clubs] [:queen :spades]]}

  )
