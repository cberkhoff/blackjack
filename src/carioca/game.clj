(ns carioca.game
  (:use clojure.set))

(def suits
  [:spades :hearts :diamonds :clubs])

(def cards
  (conj (range 2 11) :ace :jack :queen :king))

(def deck
  (conj (reduce concat
           (map #(into [] (zipmap cards (repeat %)))
                suits))
        [:jocker :red]
        [:jocker :black]))

(defn new-game
  "Creates a hashmap with the state of a starting game. It
requires the names of the players"
  [& players]
  (let [n (count players)
        d (shuffle deck)]
    (let [pd (partition 11 (rest d))]
      {:draw_pile (apply concat (nthrest pd n))
       :discard_pile (first d)
       :players (zipmap players (take n pd))}))) 

(defn winner?
  "Returns the winner of the game or just nil"
  [game]
  (first (filter #(empty? (val %)) (:players game))))

(def tg (new-game "nigro" "imella")) 

(winner? tg)
(winner? (merge tg {:players {"trolus" ()}}))
